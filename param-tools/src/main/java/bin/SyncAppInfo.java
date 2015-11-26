package bin;

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

import java.util.List;

public class SyncAppInfo {
    private static Config conf = Config.getInstance();
    private static Logger logger = Logger.getLogger(SyncAppInfo.class);

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        logger.info("Get application summary");
        List<SparkSummary> appSummaryList = EnvUtils.getAppSummarys();

        logger.info("Connect to mongodb");
        HistoryClient client = new HistoryClient();
        client.connect();

        logger.info("Sync applications into mongodb.");
        for (SparkSummary appSummary : appSummaryList) {
            if (!appSummary.getAttempts().get(0).getCompleted()) continue;
            if (client.findApp(conf.getClusterID(), appSummary.getId()) != null) continue;
            logger.info("Crawling apllication " + appSummary.getId());

            logger.info("\tGeting DAG.");
            List<JobDAG> jobDAGList = DAGUtils.getJobDAGsByAppSummary(appSummary);
            logger.info("\tGeting spark environment.");
            Env sparkEnv = EnvUtils.getSparkEnvByAppSummary(appSummary);
            logger.info("\tGeting yarn environment.");
            Env yarnEnv = EnvUtils.getYarnEnv();
            logger.info("\tGeting spark executor information.");
            List<SparkExecutor> executors = EnvUtils.getSparkExecutorsByAppSummary(appSummary);

            App app = new App(conf.getClusterID(), appSummary.getId(), jobDAGList,
                    appSummary, sparkEnv, yarnEnv, executors);
            client.insertApp(app);
        }
        logger.info("Sync finished.");
    }


}
