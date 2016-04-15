package server.servlet;

import common.Config;
import historydb.App;
import historydb.HistoryClient;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import server.runparamservice.RunParamJob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunParamServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(RecommendServlet.class);
    private static Config conf = Config.getInstance();
    private static HistoryClient client;
    private static ExecutorService executorService;

    public RunParamServlet() {
        super();
        client = new HistoryClient();
        logger.info("ParamsRunServlet connect to mongo spark history dbs.");
        client.connect();
        executorService = Executors.newFixedThreadPool(conf.getSSHConcurrency());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String progressid = req.getParameter("progressid");
        String appid = req.getParameter("appid");
        String params = req.getParameter("params");

        logger.info(String.format("run_params: {progressid:%s, appid:%s, params:%s}",
                    progressid, appid, params));

        if (progressid == null || params == null || appid == null) {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println("params invalid.");
            return;
        }

        App app = client.findApp(conf.getClusterID(), appid);
        if (app == null) {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println("app not exist.");
            return;
        }

        logger.info("original command:" + app.getJavaCMD());
        String cmd = parseToCMD(app, params);
        logger.info("after recommend command:" + cmd);
        RunParamJob job = new RunParamJob(appid, cmd);
        executorService.submit(job);
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println("success");
    }

    private String parseToCMD(App app, String rawRecParams) {
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
}
