package server.historydb.response;

import historydb.App;

import java.util.ArrayList;
import java.util.List;

public class ViewResponse {
    private int value;
    private String message;
    private List<ViewItem> viewItems;
    private int pageCount;
    private int activePage;

    public ViewResponse(ViewResonseStatus status) {
        switch (status) {
            case SUCCESS:
                this.value = 0;
                this.message = "success";
                break;
            case ARGUMENT_ERROR:
                this.value = 1;
                this.message = "argument error";
                break;
            case ERROR:
                this.value = 2;
                this.message = "error";
                break;
            default:
                this.value = 3;
                this.message = "unexpected error";
                break;
        }
    }

    public void setViewItems(List<App> apps) {
        viewItems = new ArrayList<ViewItem>();
        for (App app : apps) {
            ViewItem item = new ViewItem(app);
            viewItems.add(item);
        }
    }

    public void setPageCount(int count) {
        pageCount = count;
    }

    public void setActivePage(int activePage1) {
        activePage = activePage1;
    }
}
