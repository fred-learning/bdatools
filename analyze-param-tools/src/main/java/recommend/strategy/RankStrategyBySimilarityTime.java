package recommend.strategy;

import historydb.App;
import recommend.basic.AppResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankStrategyBySimilarityTime {
    private static int BEST_K = 3;

    public static List<AppResult> rank(final List<AppResult> apps) {
        Collections.sort(apps, new Comparator<AppResult>() {
            public int compare(AppResult o1, AppResult o2) {
                /*
                Double o1sim = o1.getSimilarity();
                Double o2sim = o2.getSimilarity();

                if (Math.abs(o1sim - o2sim) >= 1e-5) {
                    return -o1sim.compareTo(o2sim);
                } else if (!o1.getApp().getAppName().equals(o2.getApp().getAppName())) {
                    return o1.getApp().getAppName().compareTo(o2.getApp().getAppName());
                } else {
                    Long o1Runtime = o1.getApp().getRuntime();
                    Long o2Runtime = o2.getApp().getRuntime();
                    return o1Runtime.compareTo(o2Runtime);
                }*/
                Double score1 = o1.getSimilarity() / o1.getApp().getRuntime();
                Double score2 = o2.getSimilarity() / o2.getApp().getRuntime();
                return -score1.compareTo(score2);
            }
        });

        return keepBest(apps);
    }

    private static List<AppResult> keepBest(List<AppResult> appResultList) {
        List<AppResult> ret = new ArrayList<AppResult>();

        AppResult lastAppResult = null;
        int keep_sofar = 0;

        for (AppResult appResult : appResultList) {
            if (lastAppResult == null) {
                ret.add(appResult);
                lastAppResult = appResult;
                keep_sofar += 1;
            } else if (Math.abs(lastAppResult.getSimilarity() - appResult.getSimilarity()) < 1e-5 &&
                    lastAppResult.getApp().getAppName().equals(appResult.getApp().getAppName())) {
                if (keep_sofar >= BEST_K) {
                    continue;
                } else {
                    ret.add(appResult);
                    keep_sofar += 1;
                }
            } else {
                ret.add(appResult);
                lastAppResult = appResult;
                keep_sofar = 1;
            }
        }

        return ret;
    }

}
