package server.historydb.response;

import historydb.App;

import java.util.Map;

public class ViewItem {
    private String appid;
    private String appName;
    private Map<String, String> recommendParams;
    private Long runTime;
    private Double inputSizeMB;
    private String javaCMD;

    public ViewItem(App app) {
        this.appid = app.getAppid();
        this.appName = app.getAppName();
        this.recommendParams = app.getRecommendParams();
        this.runTime = app.getRuntime();
        this.inputSizeMB = app.getInputSizeMB();
        this.javaCMD = app.getJavaCMD();
    }
}
