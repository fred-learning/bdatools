package server.recommendservice;

import historydb.App;
import recommend.basic.AppResult;

import java.util.Map;

public class RecommendItem {
    private String clusterid;
    private String appid;
    private String appname;
    private double similarity;
    private double runtime;
    private double datasizeMB;
    private Map<String, String> params;

    public RecommendItem(AppResult result) {
        App app = result.getApp();
        clusterid = app.getClusterid();
        appid = app.getAppid();
        appname = app.getAppName();
        similarity = result.getSimilarity();
        runtime = app.getRuntime();
        datasizeMB = app.getInputSizeMB();
        params = app.getRecommendParams();
    }

    public String getClusterid() {
        return clusterid;
    }

    public String getAppid() {
        return appid;
    }

    public String getAppname() {
        return appname;
    }

    public double getSimilarity() {
        return similarity;
    }

    public double getRuntime() {
        return runtime;
    }

    public double getDatasizeMB() {
        return datasizeMB;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
