package crawler.env.pojo;

import java.util.List;

public class SparkExecutor {
    private String id;
    private String hostPort;
    private Long rddBlocks;
    private Long memoryUsed;
    private Long diskUsed;
    private Long activeTasks;
    private Long failedTasks;
    private Long completedTasks;
    private Long totalTasks;
    private Long totalDuration;
    private Long totalInputBytes;
    private Long totalShuffleRead;
    private Long totalShuffleWrite;
    private Long maxMemory;
    private List<String> executorLogs;

    public Long getTotalInputBytes() {
        return totalInputBytes;
    }
}
