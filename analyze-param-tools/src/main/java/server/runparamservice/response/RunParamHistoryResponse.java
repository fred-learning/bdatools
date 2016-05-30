package server.runparamservice.response;

import server.runparamservice.pojo.ParamHistoryItem;

import java.util.List;

public class RunParamHistoryResponse {
    private int value;
    private String message;
    private List<ParamHistoryItem> items;
    private int activePage;
    private int pageCount;

    public RunParamHistoryResponse(RunParamHistoryResponseStatus status) {
        switch (status) {
            case SUCCESS:
                value = 0;
                message = "success";
                break;
            case INVALID_ARGUMENT:
                value = 1;
                message = "invalid argument";
                break;
            case UNDEFINED_ERROR:
                value = -1;
                message = "undefined error";
                break;
        }
    }

    public void setItems(List<ParamHistoryItem> items) {
        this.items = items;
    }

    public void setActivePage(int activePage) {
        this.activePage = activePage;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
