package bin;

import com.alexmerz.graphviz.ParseException;
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

import java.util.List;

public class SyncAppInfo implements Runnable {
    private static Config conf = Config.getInstance();
    private static Logger logger = Logger.getLogger(SyncAppInfo.class);

    public void run() {
        assert conf.getHistorySyncIntevalSec() > 0;

        while (true) {
            try {
                sync();
                Thread.sleep(conf.getHistorySyncIntevalSec() * 1000);
            } catch (Exception e) {
                logger.fatal("Unexpected error:", e);
                System.exit(-1);
            }
        }
    }

    public void sync() {
        List<SparkSummary> appSummaryList;
        try {
            logger.info("Get application summary");
            appSummaryList = EnvUtils.getAppSummarys();
        } catch (UnirestException e) {
            logger.error("Can't get app summarys:", e);
            return;
        }

        logger.info("Connect to mongodb");
        HistoryClient client = new HistoryClient();
        client.connect();

        logger.info("Sync applications into mongodb.");
        for (SparkSummary appSummary : appSummaryList) {
            if (!appSummary.getAttempts().get(0).getCompleted()) continue;
            if (client.findApp(conf.getClusterID(), appSummary.getId()) != null) continue;

            try {
                logger.info("Crawling apllication " + appSummary.getId());
                logger.info("\tGeting DAG.");
                List<JobDAG> jobDAGList = DAGUtils.getJobDAGsByAppSummary(appSummary);
                if (jobDAGList.size() == 0) continue;

                logger.info("\tGeting spark environment.");
                Env sparkEnv = EnvUtils.getSparkEnvByAppSummary(appSummary);
                logger.info("\tGeting yarn environment.");
                Env yarnEnv = EnvUtils.getYarnEnv();
                logger.info("\tGeting spark executor information.");
                List<SparkExecutor> executors = EnvUtils.getSparkExecutorsByAppSummary(appSummary);

                App app = new App(conf.getClusterID(), appSummary.getId(), jobDAGList,
                        appSummary, sparkEnv, yarnEnv, executors);
                client.insertApp(app);
            } catch (Exception e) {
                logger.error("Can't sync " + appSummary.getId(), e);
            }

        }
        logger.info("Sync finished.");
    }


}
