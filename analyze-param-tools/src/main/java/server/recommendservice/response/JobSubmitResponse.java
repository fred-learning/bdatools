package server.recommendservice.response;

public class JobSubmitResponse {
    private int value;
    private String message;

    public JobSubmitResponse(JobSubmitStatus status) {
        switch (status) {
            case SUCCESS:
                value = 0;
                message = "success";
                break;
            case LACK_OF_ARGUMENT_ERROR:
                value = 1;
                message = "lack of argument error";
                break;
            case UNDEFINED_ERROR:
                value = -1;
                message = "undefined error";
                break;
        }
    }
}
