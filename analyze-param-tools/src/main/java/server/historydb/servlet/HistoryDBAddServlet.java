package server.historydb.servlet;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import common.Config;
import crawler.DAG.DAGUtils;
import crawler.env.Env;
import crawler.env.EnvUtils;
import crawler.env.pojo.SparkExecutor;
import crawler.env.pojo.SparkSummary;
import historydb.App;
import historydb.HistoryClient;
import org.apache.log4j.Logger;
import recommend.DAG.JobDAG;
import server.historydb.response.AddResponse;
import server.historydb.response.AddResponseStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static common.ServletUtil.setResponse;

public class HistoryDBAddServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(HistoryDBAddServlet.class);
    private static Config config = Config.getInstance();
    private static Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String appid = req.getParameter("appid");

        AddResponse response;

        HistoryClient client = new HistoryClient();
        client.connect();
        if (appid == null || appid.trim().length() == 0) {
            response = new AddResponse(AddResponseStatus.LACK_OF_ARGUMENT_ERROR);
        } else if (client.findApp(config.getClusterID(), appid) != null) {
            response = new AddResponse(AddResponseStatus.APP_EXIST_IN_DB);
        } else {
            try {
                SparkSummary summary = EnvUtils.getAppSummary(appid);
                if (summary == null) {
                    response = new AddResponse(AddResponseStatus.APP_NOT_FOUND_IN_SPARK);
                } else if (!summary.getAttempts().get(0).getCompleted()) {
                    response = new AddResponse(AddResponseStatus.APPLICATION_NOT_FINISH);
                } else {
                    List<JobDAG> jobDAGList = DAGUtils.getJobDAGsByAppSummary(summary);
                    if (jobDAGList.size() == 0) {
                        response = new AddResponse(AddResponseStatus.APPLICATION_INFORMATION_ERROR);
                    } else {
                        Env sparkEnv = EnvUtils.getSparkEnvByAppSummary(summary);
                        Env yarnEnv = EnvUtils.getYarnEnv();
                        List<SparkExecutor> executors = EnvUtils.getSparkExecutorsByAppSummary(summary);
                        App app = new App(config.getClusterID(), summary.getId(), jobDAGList,
                                summary, sparkEnv, yarnEnv, executors);
                        client.insertApp(app);
                        response = new AddResponse(AddResponseStatus.SUCCESS);
                    }
                }
            } catch (UnirestException e) {
                logger.error("Error in crawling application " + appid, e);
                response = new AddResponse(AddResponseStatus.NETWORK_ERROR);
            }
        }
        client.close();

        String msg = gson.toJson(response);
        setResponse(resp, msg);
    }


}
