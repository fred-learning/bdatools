package recommend.strategy;

import historydb.App;
import org.apache.log4j.Logger;
import recommend.DAG.JobDAG;
import recommend.graphmatch.GraphMatching;

import java.util.List;

public class ScoreStrategyByBiggestJobDAGSim {
    private static Logger logger = Logger.getLogger(ScoreStrategyByBiggestJobDAGSim.class);

    public static double score(App a, App b) {
        List<JobDAG> aJobDAGList = a.getSortedJobDAGs();
        List<JobDAG> bJobDAGList = b.getSortedJobDAGs();
        if (aJobDAGList.size() == 0 || bJobDAGList.size() == 0) return 0;

        JobDAG aDAG = getBiggestJobDAG(aJobDAGList);
        JobDAG bDAG = getBiggestJobDAG(bJobDAGList);
        return GraphMatching.similarity(aDAG, bDAG);
    }

    public static JobDAG getBiggestJobDAG(List<JobDAG> jobDAGList) {
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
