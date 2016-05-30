package server.recommendservice.pojo;

import recommend.basic.AppResult;
import server.recommendservice.pojo.RecommendItem;

import java.util.ArrayList;
import java.util.List;

public class RecommendParamsJobResult {
    private boolean succeed;
    private String errMsg;
    private Long originRunTime = 0L;
    private List<RecommendItem> recommendItemList;

    public RecommendParamsJobResult(List<AppResult> appResultList) {
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

    public RecommendParamsJobResult(String errMsg) {
        succeed = false;
        this.errMsg = errMsg;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setOriginRunTime(Long originRunTime) {
        this.originRunTime = originRunTime;
    }
}
