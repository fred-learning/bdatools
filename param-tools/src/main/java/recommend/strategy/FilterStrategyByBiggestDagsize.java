package recommend.strategy;

import historydb.App;
import recommend.DAG.JobDAG;

import java.util.List;

public class FilterStrategyByBiggestDagsize {
    private static double INPUT_DAGSIZE_DISCREPANCY_RATIO = 0.3;

    public static boolean matched(App origin, App target) {
        List<JobDAG> aJobDAGList = origin.getSortedJobDAGs();
        List<JobDAG> bJobDAGList = target.getSortedJobDAGs();
        if (aJobDAGList.size() == 0 || bJobDAGList.size() == 0) return false;

        JobDAG aDAG = ScoreStrategyByBiggestJobDAGSim.getBiggestJobDAG(aJobDAGList);
        JobDAG bDAG = ScoreStrategyByBiggestJobDAGSim.getBiggestJobDAG(bJobDAGList);

        double frac = 1.0 * aDAG.getNodeSet().size() / bDAG.getNodeSet().size();
        return Math.abs(1 - frac) <= INPUT_DAGSIZE_DISCREPANCY_RATIO;
    }
}
