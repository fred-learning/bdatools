package server.recommendservice;

import recommend.basic.AppResult;

import java.util.ArrayList;
import java.util.List;

public class RecommendResult {
    private boolean succeed;
    private String errMsg;
    private List<RecommendItem> recommendItemList;

    public RecommendResult(List<AppResult> appResultList) {
        if (appResultList == null || appResultList.size() == 0) {
            succeed = false;
            errMsg = "No result matched";
        } else {
            succeed = true;
            recommendItemList = new ArrayList<RecommendItem>();
            for (AppResult result : appResultList)
                recommendItemList.add(new RecommendItem(result));
        }
    }

    public RecommendResult(String errMsg) {
        succeed = false;
        this.errMsg = errMsg;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
