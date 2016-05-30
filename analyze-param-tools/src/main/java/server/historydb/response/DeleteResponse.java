package server.historydb.response;

public class DeleteResponse {
    private int value;
    private String message;

    public DeleteResponse(DeleteResonseStatus status) {
        switch (status) {
            case SUCCESS:
                this.value = 0;
                this.message = "success";
                break;
            case APPLICATION_NOT_EXIST:
                this.value = 1;
                this.message = "application not exist";
                break;
            case NETWORK_ERROR:
                this.value = 2;
                this.message = "network error";
                break;
            case LACK_OF_ARGUMENT_ERROR:
                this.value = 3;
                this.message = "lack of argument";
                break;
            default:
                this.value = -1;
                this.message = "unexpected error";
                break;
        }
    }
}
