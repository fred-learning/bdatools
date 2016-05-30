package server.historydb.response;

public enum AddResponseStatus {
    SUCCESS,
    APP_EXIST_IN_DB,
    APP_NOT_FOUND_IN_SPARK,
    LACK_OF_ARGUMENT_ERROR,
    NETWORK_ERROR,
    APPLICATION_INFORMATION_ERROR,
    APPLICATION_NOT_FINISH;
}
