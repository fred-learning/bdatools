package recommend.strategy;

import historydb.App;
import recommend.DAG.JobDAG;
import recommend.graphmatch.GraphMatching;

import java.util.List;

public class ScoreStrategyByBiggestJobDAGSim {

    public static double score(App a, App b) {
        List<JobDAG> aJobDAGList = a.getSortedJobDAGs();
        List<JobDAG> bJobDAGList = b.getSortedJobDAGs();
        if (aJobDAGList.size() == 0 || bJobDAGList.size() == 0) return 0;

        JobDAG aDAG = getBiggestJobDAG(aJobDAGList);
        JobDAG bDAG = getBiggestJobDAG(bJobDAGList);
        return GraphMatching.similarity(aDAG, bDAG);
    }

    private static JobDAG getBiggestJobDAG(List<JobDAG> jobDAGList) {
        JobDAG ret = null;
        for (JobDAG jobDAG : jobDAGList) {
            if (ret == null) {
                ret = jobDAG;
            } else if (ret.getEdgeMap().getEdgeNum() <= jobDAG.getEdgeMap().getEdgeNum()) {
                ret = jobDAG;
            }
        }
        return ret;
    }

}
