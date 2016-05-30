package server.recommendservice.response;

public class ProgressViewItem {
    private String progressid;
    private String clusterid;
    private String appid;
    private String startTime;
    private String endTime;
    private String status;
    private String result;

    public ProgressViewItem(String progressid, String clusterid, String appid,
                            String startTime, String endTime, String status,
                            String result) {
        this.progressid = progressid;
        this.clusterid = clusterid;
        this.appid = appid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.result = result;
    }
}
