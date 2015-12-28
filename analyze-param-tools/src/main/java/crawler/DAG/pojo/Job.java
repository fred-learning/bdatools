package crawler.DAG.pojo;

import java.util.List;

public class Job {
	private Integer jobId;
	private String name;
	private String submissionTime;
	private String completionTime;
	private List<Integer> stageIds;
	private String status;
	private Integer numTasks;
	private Integer numActiveTasks;
	private Integer numCompletedTasks;
	private Integer numSkippedTasks;
	private Integer numFailedTasks;
	private Integer numActiveStages;
	private Integer numCompletedStages;
	private Integer numSkippedStages;
	private Integer numFailedStages;
	
	
	public Integer getJobId() {
		return jobId;
	}
	public String getName() {
		return name;
	}
	public String getSubmissionTime() {
		return submissionTime;
	}
	public String getCompletionTime() {
		return completionTime;
	}
	public List<Integer> getStageIds() {
		return stageIds;
	}
	public String getStatus() {
		return status;
	}
	public Integer getNumTasks() {
		return numTasks;
	}
	public Integer getNumActiveTasks() {
		return numActiveTasks;
	}
	public Integer getNumCompletedTasks() {
		return numCompletedTasks;
	}
	public Integer getNumSkippedTasks() {
		return numSkippedTasks;
	}
	public Integer getNumFailedTasks() {
		return numFailedTasks;
	}
	public Integer getNumActiveStages() {
		return numActiveStages;
	}
	public Integer getNumCompletedStages() {
		return numCompletedStages;
	}
	public Integer getNumSkippedStages() {
		return numSkippedStages;
	}
	public Integer getNumFailedStages() {
		return numFailedStages;
	}
	
	
}
