package server.historydb.response;

public class AddResponse {
    private int value;
    private String message;

    public AddResponse(AddResponseStatus status) {
        switch (status) {
            case SUCCESS:
                this.value = 0;
                this.message = "success";
                break;
            case APP_EXIST_IN_DB:
                this.value = 1;
                this.message = "application already in history database";
                break;
            case APP_NOT_FOUND_IN_SPARK:
                this.value = 2;
                this.message = "application not found in spark";
                break;
            case LACK_OF_ARGUMENT_ERROR:
                this.value = 3;
                this.message = "lack of argument";
                break;
            case NETWORK_ERROR:
                this.value = 4;
                this.message = "network error";
                break;
            case APPLICATION_INFORMATION_ERROR:
                this.value = 5;
                this.message = "apllication information error";
                break;
            case APPLICATION_NOT_FINISH:
                this.value = 6;
                this.message = "application not finished";
                break;
            default:
                this.value = -1;
                this.message = "unexpected error";
                break;
        }
    }
}
