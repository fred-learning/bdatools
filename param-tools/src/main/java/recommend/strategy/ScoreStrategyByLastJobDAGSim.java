package recommend.strategy;

import historydb.App;
import recommend.DAG.JobDAG;
import recommend.graphmatch.GraphMatching;

import java.util.List;

public class ScoreStrategyByLastJobDAGSim {

    public static double score(App a, App b) {
        List<JobDAG> aJobDAGList = a.getSortedJobDAGs();
        List<JobDAG> bJobDAGList = b.getSortedJobDAGs();
        if (aJobDAGList.size() == 0 || bJobDAGList.size() == 0) return 0;

        JobDAG aDAG = aJobDAGList.get(aJobDAGList.size() - 1);
        JobDAG bDAG = bJobDAGList.get(bJobDAGList.size() - 1);
        return GraphMatching.similarity(aDAG, bDAG);
    }

}
