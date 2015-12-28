package cn.edu.thu.thss.clusteranalyzetools.bin;

import cn.edu.thu.thss.clusteranalyzetools.common.Config;
import cn.edu.thu.thss.clusteranalyzetools.common.MiscUtil;
import cn.edu.thu.thss.clusteranalyzetools.metricdb.ClusterMetric;
import cn.edu.thu.thss.clusteranalyzetools.metricdb.DBClient;
import cn.edu.thu.thss.clusteranalyzetools.metricdb.MetricCrawler;
import org.apache.log4j.Logger;

public class MetricSyncDaemon implements Runnable {
    private static Logger logger = Logger.getLogger(MetricSyncDaemon.class);
    private static Config config = Config.getInstance();

    public void run() {
        DBClient client = new DBClient();
        if (!client.connect()) {
            logger.info("Can't connect to mongodb. MetricSync exit.");
        } else {
            while (true) {
                ClusterMetric metric = MetricCrawler.getClusterMetric();
                if (metric == null) {
                    logger.warn("Can't get metric.");
                } else {
                    client.insertMetric(metric);
                }
                MiscUtil.sleepBySec(config.getMongoSyncInterval());
            }
        }
    }

    public static void main(String[] args) {
        MetricSyncDaemon daemon = new MetricSyncDaemon();
        daemon.run();
    }
}
