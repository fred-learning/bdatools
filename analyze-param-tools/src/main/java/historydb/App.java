package historydb;

import common.Config;
import common.DateUtil;
import crawler.env.Env;
import crawler.env.pojo.SparkExecutor;
import crawler.env.pojo.SparkSummary;
import recommend.DAG.JobDAG;

import java.util.*;

public class App {
    private static Config conf = Config.getInstance();
    private String clusterid;
    private String appid;
    private List<JobDAG> jobDAGs;
    private SparkSummary summary;
    private Env sparkEnv;
    private Env yarnEnv;
    private List<SparkExecutor> executors;

    public App(String clusterid, String appid, List<JobDAG> jobDAGs, SparkSummary summary,
               Env sparkEnv, Env yarnEnv, List<SparkExecutor> executors) {
        this.clusterid = clusterid;
        this.appid = appid;
        this.jobDAGs = jobDAGs;
        this.summary = summary;
        this.sparkEnv = sparkEnv;
        this.yarnEnv = yarnEnv;
        this.executors = executors;
    }

    public String getAppid() {
        return appid;
    }

    public String getClusterid() { return clusterid; }

    public List<JobDAG> getJobDAGs() {
        return jobDAGs;
    }

    public Env getSparkEnv() {
        return sparkEnv;
    }

    public Env getYarnEnv() {
        return yarnEnv;
    }

    public SparkSummary getSummary() {
        return summary;
    }

    public String getAppName() {
        return sparkEnv.get("spark.app.name");
    }

    public Long getRuntime() {
        String startTime = summary.getAttempts().get(0).getStartTime();
        String endTime = summary.getAttempts().get(0).getEndTime();
        return DateUtil.stringToTime(endTime) - DateUtil.stringToTime(startTime);
    }

    public List<JobDAG> getSortedJobDAGs() {
        Collections.sort(jobDAGs, new Comparator<JobDAG>() {
            public int compare(JobDAG o1, JobDAG o2) {
                return o1.getJobid().compareTo(o2.getJobid());
            }
        });
        return jobDAGs;
    }

    public String getRecommendParamsStr() {
        StringBuffer sb = new StringBuffer();
        for (String recommendKey : conf.getSparkRecommendParams()) {
            if (sparkEnv.get(recommendKey) == null) continue;
            if (sb.length() != 0) sb.append(", ");
            String pair = String.format("%s:%s", recommendKey, sparkEnv.get(recommendKey));
            sb.append(pair);
        }
        return sb.toString();
    }

    public Map<String, String> getRecommendParams() {
        Map<String, String> ret = new HashMap<String, String>();
        for (String recommendKey : conf.getSparkRecommendParams()) {
            if (sparkEnv.get(recommendKey) == null) continue;
            ret.put(recommendKey, sparkEnv.get(recommendKey));
        }
        return ret;
    }

    public Double getInputSizeMB() {
        Long ret = 0L;
        for (SparkExecutor executor : executors) {
            ret += executor.getTotalInputBytes();
        }
        return ret / 1024.0 / 1024.0;
    }
}
