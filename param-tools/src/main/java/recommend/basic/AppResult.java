package recommend.basic;

import historydb.App;

public class AppResult {
    private App app;
    private Double similarity;

    public AppResult(App app, double similarity) {
        this.app = app;
        this.similarity = similarity;
    }

    public App getApp() {
        return app;
    }

    public Double getSimilarity() {
        return similarity;
    }
}
