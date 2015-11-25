package recommend.strategy;

import historydb.App;
import recommend.DAG.JobDAG;
import recommend.graphmatch.GraphMatching;

import java.util.List;

public class ScoreStrategyByAverageJobDAGSim {

    public static double score(App a, App b) {
        List<JobDAG> aJobDAGList = a.getSortedJobDAGs();
        List<JobDAG> bJobDAGList = b.getSortedJobDAGs();
        if (aJobDAGList.size() == 0 || bJobDAGList.size() == 0) return 0;

        double acc_score = 0;
        int count_sofar = 0;

        for (JobDAG jobDAG1 : aJobDAGList) {
            for (JobDAG jobDAG2 : bJobDAGList) {
                acc_score += GraphMatching.similarity(jobDAG1, jobDAG2);
                count_sofar += 1;
            }
        }

        return acc_score / count_sofar;
    }

}
