package server.service;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import recommend.basic.AppResult;

import java.util.List;

public class RecommendParamsReporter {
    private static Logger logger = Logger.getLogger(RecommendParamsReporter.class);
    private static Gson gson = new Gson();
    private ProgressClient client;
    private String progressid;

    public RecommendParamsReporter(String progressid, ProgressClient client) {
        this.client = client;
        this.progressid = progressid;
    }

    public void setRunning(String appid) {
        logger.info(String.format("[%s] params recommendation start", progressid));
        client.setRunning(progressid, appid);
    }

    public void setFinished(List<AppResult> appResultList) {
        logger.info(String.format("[%s] params recommendation finished", progressid));
        RecommendResult result = new RecommendResult(appResultList);
        client.setFinished(progressid, gson.toJson(result));
    }

    public void setError(String errMsg) {
        logger.info(String.format("[%s] params recommendation error", progressid));
        RecommendResult result = new RecommendResult(errMsg);
        client.setError(progressid, gson.toJson(result));
    }

    public String getProgressid() {
        return progressid;
    }
}
