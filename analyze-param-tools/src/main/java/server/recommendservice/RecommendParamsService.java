package server.recommendservice;

import common.Config;
import org.apache.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecommendParamsService {
    private static Logger logger = Logger.getLogger(RecommendParamsService.class);
    private static Config conf = Config.getInstance();
    private static RecommendParamsService service;
    private static ExecutorService executorService;
    private static ProgressClient progressClient;

    public static synchronized RecommendParamsService getInstance() {
        if (service == null) {
            progressClient = new ProgressClient();
            logger.info("connect to progress db.");
            progressClient.connect();
            logger.info("create recommend threadpool with thread " + conf.getRecommendServiceNum());
            executorService = Executors.newFixedThreadPool(conf.getRecommendServiceNum());
            service = new RecommendParamsService();
        }
        return service;
    }

    public void addJob(String appid) {
        String progressid = UUID.randomUUID().toString();
        RecommendParamsReporter reporter = new RecommendParamsReporter(progressid, progressClient);
        reporter.setRunning(appid);
        RecommendParamsJob job = new RecommendParamsJob(reporter, appid);
        executorService.submit(job);
    }
}
