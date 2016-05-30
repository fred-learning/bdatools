package server.recommendservice.response;

import java.util.List;

public class ProgressViewResponse {
    private int value;
    private String message;
    private List<ProgressViewItem> items;
    private int activePage;
    private int pageCount;

    public ProgressViewResponse(ProgressViewResponseStatus status) {
        switch (status) {
            case SUCCESS:
                value = 0;
                message = "success";
                break;
            case LACK_OF_ARGUMENT_ERROR:
                value = 1;
                message = "lack of argument error";
                break;
            case ERROR:
                value = -1;
                message = "undefined error";
        }
    }

    public void setItems(List<ProgressViewItem> items) {
        this.items = items;
    }

    public void setActivePage(int activePage) {
        this.activePage = activePage;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
