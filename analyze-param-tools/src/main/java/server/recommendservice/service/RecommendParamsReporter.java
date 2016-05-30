package server.recommendservice.service;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import recommend.basic.AppResult;
import server.recommendservice.pojo.RecommendParamsJobResult;

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

    public void setFinished(List<AppResult> appResultList, Long originRunTime) {
        logger.info(String.format("[%s] params recommendation finished", progressid));
        RecommendParamsJobResult result = new RecommendParamsJobResult(appResultList);
        result.setOriginRunTime(originRunTime);
        client.setFinished(progressid, gson.toJson(result));
    }

    public void setError(String errMsg) {
        logger.info(String.format("[%s] params recommendation error", progressid));
        RecommendParamsJobResult result = new RecommendParamsJobResult(errMsg);
        client.setError(progressid, gson.toJson(result));
    }

    public String getProgressid() {
        return progressid;
    }
}
