package crawler.app;

import historydb.App;

public class CrawlAppResult {
    private int value;
    private String message;
    private App app;

    public CrawlAppResult(CrawlAppStatus status) {
        this(null, status);
    }

    public CrawlAppResult(App app, CrawlAppStatus status) {
        switch (status) {
            case SUCCESS:
                this.app = app;
                value = 0;
                message = "success";
                break;
            case APP_NOT_EXIST_IN_SPARK_HIST_SERVER:
                value = 1;
                message = "app not exist in spark history server";
                break;
            case APP_DAG_ERROR:
                value = 2;
                message = "app dag information error";
                break;
            case APP_RUNNING:
                value = 3;
                message = "app is running";
                break;
            case NETWORK_ERROR:
                value = 4;
                message = "internal network error";
                break;
        }
    }

    public App getApp() {
        return app;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
