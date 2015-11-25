package bin;

import historydb.App;
import historydb.AppIterator;
import historydb.Client;
import org.apache.log4j.Logger;
import recommend.basic.AppResult;
import recommend.strategy.*;

import java.util.ArrayList;
import java.util.List;

public class RecommendParams {
    private static Logger logger = Logger.getLogger(RecommendParams.class);

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: RecommendParams application_id");
            System.exit(0);
        }

        String appid = args[0];
        logger.info("ApplicationID " + appid);
        List<AppResult> rankResult = run(appid);
        logger.info("Recommend result:");
        printResult(rankResult);
    }

    public static List<AppResult> run(String appid) {
        logger.info("Connect client.");
        Client client = new Client();
        client.connect();

        App app = client.findApp(appid);
        if (app == null) {
            logger.fatal("Application " + appid + " doesn't exist!");
            logger.fatal("System exit");
            System.exit(-1);
        }

        logger.info("Searching for hardware matched results.");
        List<App> yarnMatchedApps = getYarnMatchedApps(app, client.getIterator());
        logger.info("Searching size " + yarnMatchedApps.size());
        client.close();

        logger.info("Searchng for datasize limited results.");
        List<App> dataMatchedApps = getDatasizeMatchedApps(app, yarnMatchedApps);
        logger.info("Searching size " + dataMatchedApps.size());

        logger.info("Calculating DAG similarities.");
        List<AppResult> appSimiarities = calculateSimilarity(app, dataMatchedApps);

        logger.info(String.format("Filtering similarity below %f", 0.3));
        List<AppResult> similarAppResultList = getSimilarApps(appSimiarities, 0.3);
        logger.info("Result size " + similarAppResultList.size());

        logger.info("Ranking recommend parameters.");
        List<AppResult> rankResult = RankStrategyBySimilarityTime.rank(similarAppResultList);

        return rankResult;
    }

    private static List<App> getYarnMatchedApps(App app, AppIterator iterator) {
        List<App> apps = new ArrayList<App>();
        while (iterator.hasNext()) {
            App other = iterator.next();
            if (app.getAppid().equals(other.getAppid())) continue;
            if (!FilterStrategyByYarnResources.matched(app, other)) continue;
            apps.add(other);
        }
        return apps;
    }

    private static List<App> getDatasizeMatchedApps(App app, List<App> yarnMatchedApps) {
        List<App> ret = new ArrayList<App>();
        for (App other : yarnMatchedApps) {
            if (FilterStrategyByDataset.matched(app, other))
                ret.add(other);
        }
        return ret;
    }

    private static List<AppResult> calculateSimilarity(App app, List<App> others) {
        List<AppResult> appResult = new ArrayList<AppResult>();
        for (App other : others) {
            logger.info(String.format("\tCalculate similarity between (%s, %s)",
                    app.getAppid(), other.getAppid()));
            double similarity = ScoreStrategyByBiggestJobDAGSim.score(app, other);
            appResult.add(new AppResult(other, similarity));
        }
        return appResult;
    }

    private static List<AppResult> getSimilarApps(List<AppResult> list, double threshold) {
        List<AppResult> result = new ArrayList<AppResult>();
        for (AppResult e : list) {
            if (e.getSimilarity() >= threshold)
                result.add(e);
        }
        return result;
    }

    private static void printResult(List<AppResult> appResults) {
        String FORMAT = "%d. appid:%s, appname:%s, similarity:%f, time:%fs, datasize:%f MB, appparams:[%s]";
        int count_sofar = 1;
        for (AppResult appResult : appResults) {
            App app = appResult.getApp();
            String msg = String.format(FORMAT,
                    count_sofar, app.getAppid(), app.getAppName(),
                    appResult.getSimilarity(), app.getRuntime()/1000.0, app.getInputSizeMB(),
                    app.getRecommendParamsStr());
            logger.info(msg);
            count_sofar ++;
        }
    }

}
