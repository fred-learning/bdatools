package server.runparamservice.response;

public class RunParamSubmitResponse {
    private int value;
    private String message;

    public RunParamSubmitResponse(RunParamSubmitResponseStatus status) {
        switch (status) {
            case SUCCESS:
                value = 0;
                message = "success";
                break;
            case LACK_OF_ARGUMENT_ERROR:
                value = 1;
                message = "lack of argument";
                break;
            case UNDEFINED_ERROR:
                value = -1;
                message = "undefined error";
                break;
        }
    }

    public RunParamSubmitResponse(int v, String msg){
        value = v;
        message = msg;
    }
}
