package crawler.app;

import com.mashape.unirest.http.exceptions.UnirestException;
import common.Config;
import crawler.DAG.DAGUtils;
import crawler.env.Env;
import crawler.env.EnvUtils;
import crawler.env.pojo.SparkExecutor;
import crawler.env.pojo.SparkSummary;
import historydb.App;
import org.apache.log4j.Logger;
import recommend.DAG.JobDAG;

import java.util.List;

public class AppUtils {
    private static Logger logger = Logger.getLogger(AppUtils.class);
    private static Config conf = Config.getInstance();

    public static CrawlAppResult getApp(String appid) {
        CrawlAppResult result;
        try {
            SparkSummary summary = EnvUtils.getAppSummary(appid);
            if (summary == null) {
                result = new CrawlAppResult(CrawlAppStatus.APP_NOT_EXIST_IN_SPARK_HIST_SERVER);
            } else if (!summary.getAttempts().get(0).getCompleted()) {
                result = new CrawlAppResult(CrawlAppStatus.APP_RUNNING);
            } else {
                List<JobDAG> jobDAGList = DAGUtils.getJobDAGsByAppSummary(summary);
                if (jobDAGList.size() == 0) {
                    result = new CrawlAppResult(CrawlAppStatus.APP_DAG_ERROR);
                } else {
                    Env sparkEnv = EnvUtils.getSparkEnvByAppSummary(summary);
                    Env yarnEnv = EnvUtils.getYarnEnv();
                    List<SparkExecutor> executors = EnvUtils.getSparkExecutorsByAppSummary(summary);
                    App app = new App(conf.getClusterID(), summary.getId(), jobDAGList,
                            summary, sparkEnv, yarnEnv, executors);
                    result = new CrawlAppResult(app, CrawlAppStatus.SUCCESS);
                }
            }
        } catch (UnirestException e) {
            logger.error("Error in crawling application " + appid, e);
            result = new CrawlAppResult(CrawlAppStatus.NETWORK_ERROR);
        }
        return result;
    }

}
