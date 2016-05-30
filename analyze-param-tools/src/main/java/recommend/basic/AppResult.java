package recommend.basic;

import historydb.App;

public class AppResult {
    private App app;
    private Double similarity; // DAG图相似度
    private Double score; // 经过计算后得到相似度

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

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getScore() {
        return score;
    }
}
