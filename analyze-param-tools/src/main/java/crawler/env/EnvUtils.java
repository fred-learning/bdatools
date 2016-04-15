package crawler.env;

import com.mashape.unirest.http.exceptions.UnirestException;
import common.Config;
import crawler.DAG.pojo.Stage;
import crawler.env.pojo.SparkExecutor;
import crawler.env.pojo.SparkSummary;
import crawler.request.JsonArrayRequest;
import crawler.request.TextRequest;
import crawler.request.XmlRequest;
import org.apache.log4j.Logger;

import java.util.List;

public class EnvUtils {
    private static Logger logger = Logger.getLogger(EnvUtils.class);
    private static Config conf = Config.getInstance();

    public static Env getSparkEnvByAppSummary(SparkSummary summary) throws UnirestException {
        String attemptId = summary.getAttempts().get(0).getAttemptId();
        String apiPath;
        if (attemptId == null) {
            apiPath = String.format("%s/history/%s/environment/",
                    conf.getHistServerPath(), summary.getId());
        } else {
            apiPath = String.format("%s/history/%s/%s/environment/",
                    conf.getHistServerPath(), summary.getId(), attemptId);
        }

        TextRequest request = new TextRequest();
        String html = request.handle(apiPath);
        SparkEnvParser parser = new SparkEnvParser(html);
        Env env = parser.getEnv();
        logger.debug("Get environment: " + env.toString());
        return env;
    }

    public static List<SparkExecutor> getSparkExecutorsByAppSummary(SparkSummary summary)
            throws UnirestException {
        String attemptId = summary.getAttempts().get(0).getAttemptId();
        String apiPath;
        if (attemptId == null) {
            apiPath = String.format("%s/api/v1/applications/%s/executors/",
                    conf.getHistServerPath(), summary.getId());
        } else {
            apiPath = String.format("%s/api/v1/applications/%s/%s/executors/",
                    conf.getHistServerPath(), summary.getId(), attemptId);
        }

        JsonArrayRequest<SparkExecutor> request = new JsonArrayRequest<SparkExecutor>(SparkExecutor.class);
        return request.handle(apiPath);
    }

    public static List<SparkSummary> getAppSummarys() throws UnirestException {
        String apiPath = String.format("%s/api/v1/applications/", conf.getHistServerPath());
        JsonArrayRequest<SparkSummary> request = new JsonArrayRequest<SparkSummary>(SparkSummary.class);
        return request.handle(apiPath);
    }

    public static Env getYarnEnv() throws UnirestException {
        String apiPath = String.format("%s/ws/v1/cluster/metrics", conf.getYarnPath());
        XmlRequest request = new XmlRequest();
        String xml = request.handle(apiPath);
        YarnEnvParser parser = new YarnEnvParser(xml);
        Env env = parser.getEnv();
        logger.debug("Get environment: " + env.toString());
        return env;
    }

}
