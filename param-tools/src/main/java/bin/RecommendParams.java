package bin;

import historydb.App;
import historydb.AppIterator;
import historydb.HistoryClient;
import org.apache.log4j.Logger;
import recommend.basic.AppResult;
import recommend.strategy.FilterStrategyByDataset;
import recommend.strategy.FilterStrategyByYarnResources;
import recommend.strategy.RankStrategyBySimilarityTime;
import recommend.strategy.ScoreStrategyByBiggestJobDAGSim;
import server.service.RecommendParamsJob;

import java.util.ArrayList;
import java.util.List;

public class RecommendParams {
    private static double SIM_THRESHOLD = 0.3f;
    private static Logger logger = Logger.getLogger(RecommendParams.class);

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: RecommendParams cluster_id application_id");
            System.exit(0);
        }

        String clusterid = args[0];
        String appid = args[1];
        logger.info(String.format("ClusterID: %s, AppID: %s", clusterid, appid));
        List<AppResult> rankResult = calculate(clusterid, appid);

        if (rankResult == null) {
            logger.warn("Application not exist. System exit.");
            System.exit(0);
        } else {
            logger.info("Recommend result:");
            printResult(rankResult);
        }
    }


    public static List<AppResult> calculate(String clusterid, String appid) {
        HistoryClient historyClient = new HistoryClient();
        logger.info("Connect history db");
        historyClient.connect();

        List<AppResult> ret;
        App app = historyClient.findApp(clusterid, appid);
        if (app == null) {
            logger.info("apllication doesn't exist");
            ret = null;
        } else {
            List<App> yarnMatchedApps = getYarnMatchedApps(app, historyClient.getIterator());
            List<App> dataMatchedApps = getDatasizeMatchedApps(app, yarnMatchedApps);
            List<AppResult> appSimiarities = calculateSimilarity(app, dataMatchedApps);
            List<AppResult> similarAppResultList = getSimilarApps(appSimiarities, SIM_THRESHOLD);
            logger.info("Ranking recommend parameters.");
            List<AppResult> rankResult = RankStrategyBySimilarityTime.rank(similarAppResultList);
            ret = rankResult;
        }

        logger.info("Close history db");
        historyClient.close();
        return ret;
    }


    public static List<App> getYarnMatchedApps(App app, AppIterator iterator) {
        logger.info("Search hardware matched results.");
        List<App> apps = new ArrayList<App>();
        while (iterator.hasNext()) {
            App other = iterator.next();
            if (app.getAppid().equals(other.getAppid())) continue;
            if (!FilterStrategyByYarnResources.matched(app, other)) continue;
            apps.add(other);
        }

        logger.info("Matched result size:" + apps.size());
        return apps;
    }

    public static List<App> getDatasizeMatchedApps(App app, List<App> yarnMatchedApps) {
        logger.info("Search datasize matched results.");
        List<App> ret = new ArrayList<App>();
        for (App other : yarnMatchedApps) {
            if (FilterStrategyByDataset.matched(app, other))
                ret.add(other);
        }
        logger.info("Matched result size:" + ret.size());
        return ret;
    }

    public static List<AppResult> calculateSimilarity(App app, List<App> others) {
        logger.info("Calculate DAG similarity");
        List<AppResult> appResult = new ArrayList<AppResult>();
        String clusterAppID = app.getClusterid() + "#" + app.getAppid();
        for (App other : others) {
            String otherClusterAppID = app.getClusterid() + "#" + app.getAppid();
            logger.info(String.format("\tCalculate similarity between (%s, %s)",
                    clusterAppID, otherClusterAppID));
            double similarity = ScoreStrategyByBiggestJobDAGSim.score(app, other);
            appResult.add(new AppResult(other, similarity));
        }
        return appResult;
    }

    public static List<AppResult> getSimilarApps(List<AppResult> list, double threshold) {
        logger.info("Filtering similarity below " + SIM_THRESHOLD);
        List<AppResult> result = new ArrayList<AppResult>();
        for (AppResult e : list) {
            if (e.getSimilarity() >= threshold)
                result.add(e);
        }
        logger.info("Filter result size " + result.size());
        return result;
    }

    public static void printResult(List<AppResult> appResults) {
        String FORMAT = "%d. clusterid:%s, appid:%s, appname:%s, similarity:%f, time:%fs, datasize:%f MB, appparams:[%s]";
        int count_sofar = 1;
        for (AppResult appResult : appResults) {
            App app = appResult.getApp();
            String msg = String.format(FORMAT,
                    count_sofar, app.getClusterid(), app.getAppid(), app.getAppName(),
                    appResult.getSimilarity(), app.getRuntime()/1000.0, app.getInputSizeMB(),
                    app.getRecommendParamsStr());
            logger.info(msg);
            count_sofar ++;
        }
    }
}
