package crawler.app;

public enum CrawlAppStatus {
    APP_NOT_EXIST_IN_SPARK_HIST_SERVER,
    APP_RUNNING,
    APP_DAG_ERROR,
    NETWORK_ERROR,
    SUCCESS
}
