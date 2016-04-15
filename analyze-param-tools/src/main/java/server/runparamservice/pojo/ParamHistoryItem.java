package server.runparamservice.pojo;

import java.io.Serializable;

public class ParamHistoryItem implements Serializable, Comparable<ParamHistoryItem> {
    private String startTime;
    private String appid;
    private String filePrefix;
    private String cmd;
    private Long runTimeInSec;
    private ItemStatus succeed;

    public ParamHistoryItem(String startTime, String appid, String filePrefix,
                            String cmd, Long runTimeInSec, ItemStatus succeed) {
        this.startTime = startTime;
        this.appid = appid;
        this.filePrefix = filePrefix;
        this.cmd = cmd;
        this.runTimeInSec = runTimeInSec;
        this.succeed = succeed;
    }

    public String getStartTime() {
        return startTime;
    }

    public int compareTo(ParamHistoryItem o) {
        return -startTime.compareTo(o.getStartTime());
    }
}
