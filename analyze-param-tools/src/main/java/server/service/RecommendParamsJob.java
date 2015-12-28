package server.service;

import common.Config;
import historydb.App;
import historydb.AppIterator;
import historydb.HistoryClient;
import org.apache.log4j.Logger;
import recommend.basic.AppResult;
import recommend.strategy.*;

import java.util.ArrayList;
import java.util.List;

public class RecommendParamsJob implements Runnable {
    private static Logger logger = Logger.getLogger(RecommendParamsJob.class);
    private static Config conf = Config.getInstance();
    private static double SIM_THRESHOLD = 0.3;
    private RecommendParamsReporter reporter;
    private String appid;

    public RecommendParamsJob(RecommendParamsReporter reporter, String appid) {
        this.reporter = reporter;
        this.appid = appid;
    }

    public void run() {
        List<AppResult> appResults = calculate();
        if (appResults == null) {
            reporter.setError("application doesn't exist.");
        } else {
            reporter.setFinished(appResults);
        }
    }

    public List<AppResult> calculate() {
        HistoryClient historyClient = new HistoryClient();
        info("Connect history db");
        historyClient.connect();

        List<AppResult> ret;
        App app = historyClient.findApp(conf.getClusterID(), appid);
        if (app == null) {
            info("application doesn't exist");
            ret = null;
        } else {
            List<App> yarnMatchedApps = getYarnMatchedApps(app, historyClient.getIterator());
            List<App> dataMatchedApps = getDatasizeMatchedApps(app, yarnMatchedApps);
            List<App> dagMatchedApps = getDagsizeMatchedApps(app, dataMatchedApps);
            List<AppResult> appSimiarities = calculateSimilarity(app, dagMatchedApps);
            List<AppResult> similarAppResultList = getSimilarApps(appSimiarities, SIM_THRESHOLD);
            info("Ranking recommend parameters.");
            List<AppResult> rankResult = RankStrategyBySimilarityTime.rank(similarAppResultList);
            ret = rankResult;
        }

        info("Close history db");
        historyClient.close();
        return ret;
    }

    private void info(String msg) {
        logger.info(String.format("[%s] %s", reporter.getProgressid(), msg));
    }

    public List<App> getYarnMatchedApps(App app, AppIterator iterator) {
        info("Search hardware matched results.");

        List<App> apps = new ArrayList<App>();
        while (iterator.hasNext()) {
            App other = iterator.next();
            if (app.getAppid().equals(other.getAppid())) continue;
            if (!FilterStrategyByYarnResources.matched(app, other)) continue;
            apps.add(other);
        }

        info("Matched result size:" + apps.size());
        return apps;
    }

    public List<App> getDatasizeMatchedApps(App app, List<App> yarnMatchedApps) {
        info("Search datasize matched results.");
        List<App> ret = new ArrayList<App>();
        for (App other : yarnMatchedApps) {
            if (FilterStrategyByDataset.matched(app, other))
                ret.add(other);
        }
        info("Matched result size:" + ret.size());
        return ret;
    }

    public List<App> getDagsizeMatchedApps(App app, List<App> datasizeMatched) {
        info("Search dag size matched results.");
        List<App> ret = new ArrayList<App>();
        for (App other : datasizeMatched) {
            if (FilterStrategyByBiggestDagsize.matched(app, other))
                ret.add(other);
        }
        info("Matched result size:" + ret.size());
        return ret;
    }

    public List<AppResult> calculateSimilarity(App app, List<App> others) {
        info("Calculate DAG similarity");
        List<AppResult> appResult = new ArrayList<AppResult>();
        String clusterAppID = app.getClusterid() + "#" + app.getAppid();
        for (App other : others) {
            String otherClusterAppID = other.getClusterid() + "#" + other.getAppid();
            info(String.format("\tCalculate similarity between (%s, %s)",
                    clusterAppID, otherClusterAppID));
            double similarity = ScoreStrategyByBiggestJobDAGSim.score(app, other);
            appResult.add(new AppResult(other, similarity));
        }
        return appResult;
    }

    public List<AppResult> getSimilarApps(List<AppResult> list, double threshold) {
        info("Filtering similarity below " + SIM_THRESHOLD);
        List<AppResult> result = new ArrayList<AppResult>();
        for (AppResult e : list) {
            if (e.getSimilarity() >= threshold)
                result.add(e);
        }
        info("Filter result size " + result.size());
        return result;
    }

    /*
    public static String resultToStr(List<AppResult> appResults) {
        StringBuilder sb = new StringBuilder();
        String FORMAT = "%d. clusterid:%s, appid:%s, appname:%s, similarity:%f, time:%fs, datasize:%f MB, appparams:[%s]";
        int count_sofar = 1;
        for (AppResult appResult : appResults) {
            App app = appResult.getApp();
            String msg = String.format(FORMAT,
                    count_sofar, app.getClusterid(), app.getAppid(), app.getAppName(),
                    appResult.getSimilarity(), app.getRuntime()/1000.0, app.getInputSizeMB(),
                    app.getRecommendParamsStr());
            sb.append(msg + "\n");
            count_sofar ++;
        }
        return sb.toString();
    }
   */
}
