package recommend.strategy;

import historydb.App;
import org.apache.log4j.Logger;
import recommend.basic.AppResult;

import java.util.*;

public class RankStrategyByAll {
    private static int BEST_K = 3;
    private static Logger logger = Logger.getLogger(RankStrategyByAll.class);

    /**
     * 对推荐结果进行降序排序。每个推荐结果的得分由以下公式计算：
     * score(j) = sim(i, j) * (1 - datasize_ratio(i, j)) * (1 - runtime_normalize(j))
     * sim(i, j)为i和j的dag图相似度
     * datasize_ratio(i, j) = \frac{ max(abs(datasize(i) - datasize(j)), 1)}{ max(datasize(i), datasize(j), 1) }
     * runtime_normalize(j) = \frac{ runtime(j) - min_k{runtime(k)} }{ max_k{runtime(k)} - min_k{runtime{k}} }
     * 除此之外，相同名字的任务只保留得分最高的BEST_K个。
     * @param i, 待参数优化任务
     * @param, js, 参数优化推荐结果
     * @return 参数优化经过排序和过滤的结果。
     */
    public static List<AppResult> rank(final App i, final List<AppResult> results) {
        setScore(i, results);
        return keepBest(results);
    }

    private static void setScore(App i, List<AppResult> results) {
        Long minRuntime = Long.MAX_VALUE, maxRuntime = Long.MIN_VALUE;
        for (AppResult result : results) {
            App j = result.getApp();
            minRuntime = Math.min(j.getRuntime(), minRuntime);
            maxRuntime = Math.max(j.getRuntime(), maxRuntime);
        }

        for (AppResult result : results) {
            App j = result.getApp();
            double dsize_ratio_num = Math.max(Math.abs(i.getInputSizeMB() - j.getInputSizeMB()), 1);
            double dsize_ratio_den = Math.max(Math.max(i.getInputSizeMB(), j.getInputSizeMB()), 1);
            double dsize_ratio = dsize_ratio_num / dsize_ratio_den;
            double runtime_normalized = 1.0 * (j.getRuntime() - minRuntime) / (maxRuntime - minRuntime + 1);
            double score = result.getSimilarity() * (1 - dsize_ratio) * (1 - runtime_normalized);
            result.setScore(score);
            //logger.info(String.format("appid:%s, sim:%s, dsize_score:%s, runtime_score:%s, total:%s",
            //        j.getAppid(), result.getSimilarity(), 1 - dsize_ratio, 1 - runtime_normalized, score));
        }
    }

    private static List<AppResult> keepBest(final List<AppResult> results) {
        Map<String, List<AppResult>> M = new HashMap<String, List<AppResult>>();
        for (AppResult result : results) {
            String key = result.getApp().getAppName();
            if (M.get(key) == null) M.put(key, new ArrayList<AppResult>());
            M.get(key).add(result);
        }

        List<AppResult> ret = new ArrayList<AppResult>();
        for (Map.Entry<String, List<AppResult>> e : M.entrySet()) {
            List<AppResult> temp = e.getValue();
            Collections.sort(temp, new Comparator<AppResult>() {
                public int compare(AppResult o1, AppResult o2) {
                    return -o1.getScore().compareTo(o2.getScore());
                }
            });
            List<AppResult> tempSubList = temp.subList(0, Math.min(BEST_K, temp.size()));
            ret.addAll(tempSubList);
        }

        Collections.sort(ret, new Comparator<AppResult>() {
            public int compare(AppResult o1, AppResult o2) {
                return -o1.getScore().compareTo(o2.getScore());
            }
        });

        return ret;
    }


}
