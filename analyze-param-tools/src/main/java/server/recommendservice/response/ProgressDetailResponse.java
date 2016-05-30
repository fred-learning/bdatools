package server.recommendservice.response;

public class ProgressDetailResponse {
    private int value;
    private String message;
    private ProgressViewItem item;

    public ProgressDetailResponse(ProgressDetailResponseStatus status) {
        switch (status) {
            case SUCCESS:
                value = 0;
                message = "success";
                break;
            case LACK_OF_ARGUMENT_ERROR:
                value = 1;
                message = "lack of argument";
                break;
            case PROGRESS_NOT_FOUND:
                value = 2;
                message = "progress not found";
                break;
            case UNDEFINED_ERROR:
                value = -1;
                message = "undefined error";
                break;
        }
    }

    public void setItem(ProgressViewItem item) {
        this.item = item;
    }
}
