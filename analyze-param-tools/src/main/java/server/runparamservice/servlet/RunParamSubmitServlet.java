package server.runparamservice.servlet;

import com.google.gson.Gson;
import common.Config;
import common.ServletUtil;
import crawler.app.AppUtils;
import crawler.app.CrawlAppResult;
import historydb.App;
import org.apache.log4j.Logger;
import server.runparamservice.RunParamJob;
import server.recommendservice.servlet.RecommendJobSubmitServlet;
import server.runparamservice.response.RunParamSubmitResponse;
import server.runparamservice.response.RunParamSubmitResponseStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunParamSubmitServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(RecommendJobSubmitServlet.class);
    private static Config conf = Config.getInstance();
    private static Gson gson = new Gson();
    private static ExecutorService executorService =
            Executors.newFixedThreadPool(conf.getSSHConcurrency());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String appid = req.getParameter("appid");
        String params = req.getParameter("params");
        logger.info(String.format("run_params: {appid:%s, params:%s}", appid, params));

        RunParamSubmitResponse response;
        if (params == null || appid == null) {
            response = new RunParamSubmitResponse(RunParamSubmitResponseStatus.LACK_OF_ARGUMENT_ERROR);
        } else {
            CrawlAppResult crawlAppResult = AppUtils.getApp(appid);
            if (crawlAppResult.getValue() != 0) {
                response = new RunParamSubmitResponse(crawlAppResult.getValue(), crawlAppResult.getMessage());
            } else {
                App app = crawlAppResult.getApp();
                logger.info("original command:" + app.getJavaCMD());
                String cmd = parseToCMD(app, params);
                logger.info("after recommend command:" + cmd);
                RunParamJob job = new RunParamJob(appid, cmd);
                executorService.submit(job);
                response = new RunParamSubmitResponse(RunParamSubmitResponseStatus.SUCCESS);
            }
        }

        String msg = gson.toJson(response, RunParamSubmitResponse.class);
        ServletUtil.setResponse(resp, msg);
    }

    private String parseToCMD(App app, String rawRecParams) {
        if (app.getJavaCMD().startsWith("org.apache.spark.deploy.yarn.ApplicationMaster")) {
            return parseToCMDAppMaster(app, rawRecParams);
        } else {
            return parseToCMDSparkSubmit(app, rawRecParams);
        }
    }

    private String parseToCMDSparkSubmit(App app, String rawRecParams) {
        String javaCMD = app.getJavaCMD();
        String[] args = javaCMD.split(" ");

        // convert params to map
        Map<String, String> recParams = new HashMap<String, String>();
        for (String s : rawRecParams.split(" ")) {
            if (s.length() == 0 || s.trim().length() == 0) continue;
            String[] kv = s.split(":");
            recParams.put(kv[0], kv[1]);
        }

        // add user params to map
        for (int i = 1; i < args.length; ++ i) {
            if (args[i].equals("--conf") && i != args.length - 1) {
                String[] kv = args[i+1].split("=");
                String key = kv[0], val = kv[1];
                if (recParams.get(key) == null) recParams.put(key, val);
                ++ i;
            }
        }

        // find .jar index
        int jarIndex = 1;
        for (int i = 1; i < args.length; ++ i) {
            if (args[i].endsWith(".jar")) {
                jarIndex = i;
                break;
            }
        }

        // make into a command
        StringBuilder sb = new StringBuilder();
        sb.append("spark-submit ");
        for (int i = 1; i < jarIndex; ++ i) {
            if (args[i].equals("--conf")) {
                ++ i;
            } else if (args[i].equals("--executor-memory") &&
                    recParams.containsKey("spark.executor.memory")) {
                ++ i;
            } else if (args[i].equals("--num-executors") &&
                    recParams.containsKey("spark.executor.instances")){
                ++ i;
            } else if (args[i].equals("--driver-memory") &&
                    recParams.containsKey("spark.driver.memory")) {
                ++ i;
            } else {
                sb.append(args[i]);
                sb.append(" ");
            }
        }

        for (Map.Entry<String, String> e : recParams.entrySet()) {
            String next = String.format("--conf %s=%s", e.getKey(), e.getValue());
            sb.append(next);
            sb.append(" ");
        }

        for (int i = jarIndex; i < args.length; ++ i) {
            sb.append(args[i]);
            sb.append(" ");
        }

        return sb.toString();
    }

    private String parseToCMDAppMaster(App app, String rawRecParams) {
        String javaCMD = app.getJavaCMD();
        String[] args = javaCMD.split(" ");

        // convert params to map
        Map<String, String> recParams = new HashMap<String, String>();
        for (String s : rawRecParams.split(" ")) {
            if (s.length() == 0 || s.trim().length() == 0) continue;
            String[] kv = s.split(":");
            recParams.put(kv[0], kv[1]);
        }

        // add user params to map
        String jarName = "undefined", className = "undefined", master = "undefined";
        for (int i = 1; i < args.length - 1; ++ i) {
            if (args[i].equals("--conf") && i != args.length - 1) {
                String[] kv = args[i+1].split("=");
                String key = kv[0], val = kv[1];
                if (recParams.get(key) == null) recParams.put(key, val);
                ++ i;
            } else if (args[i].equals("--executor-memory") &&
                    recParams.get("spark.executor.memory") == null) {
                recParams.put("spark.executor.memory", args[i+1]);
                ++ i;
            } else if (args[i].equals("--num-executors") &&
                    recParams.get("spark.executor.instances") == null) {
                recParams.put("spark.executor.instances", args[i+1]);
                ++ i;
            } else if (args[i].equals("--driver-memory") &&
                    recParams.get("spark.driver.memory") == null) {
                recParams.put("spark.driver.memory", args[i+1]);
                ++ i;
            } else if (args[i].equals("--jar")) {
                jarName = args[i+1];
                ++ i;
            } else if (args[i].equals("--class")) {
                className = args[i+1];
                ++ i;
            } else if (args[i].equals("--master")) {
                master = args[i+1];
                ++ i;
            }
        }

        // make into a command
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("spark-submit --class %s ", className));

        if (!master.equals("undefined")) {
            sb.append(String.format("--master %s ", master));
        }

        for (Map.Entry<String, String> e : recParams.entrySet()) {
            String next = String.format("--conf %s=%s", e.getKey(), e.getValue());
            sb.append(next);
            sb.append(" ");
        }

        sb.append(jarName + " ");

        for (int i = 1; i < args.length - 1; ++ i) {
            if (args[i].equals("--arg")) {
                sb.append(args[i+1] + " ");
                ++ i;
            }
        }

        return sb.toString();
    }

}
