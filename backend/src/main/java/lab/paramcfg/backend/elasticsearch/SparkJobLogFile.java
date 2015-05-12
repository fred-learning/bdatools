package lab.paramcfg.backend.elasticsearch;

import lab.paramcfg.backend.common.Config;

import org.apache.hadoop.fs.Path;

public class SparkJobLogFile {
	
	private String remotePath;
	private String jobID;
	private String hostname;
	private String processID;
	private int nextLine;
	private boolean isFinished;
	
	public SparkJobLogFile(Path path) {
		remotePath = path.toString();
		jobID = path.getParent().getName();
		hostname = path.getName().split("_")[0];
		processID = path.getName().split("_")[1];
		nextLine = 0;
		isFinished = false;
	}
	
	public SparkJobLogFile(String path, int nextLine) {
		Path localPath = new Path(path);
		String localname = localPath.getName();
		jobID = localname.split("-")[0];
		hostname = localname.split("-")[1].split("_")[0];
		processID = localname.split("-")[1].split("_")[1];
		remotePath = String.format("%s/%s/%s_%s", Config.SPARKLOG_REMOTE_PATH, jobID, hostname, processID); 
		this.nextLine = nextLine;
		isFinished = nextLine == Integer.MAX_VALUE;
	}
	
	public String getRemotePath() { 
		return remotePath; 
	}
	
	public String getLocalPath() { 
		return String.format("%s/%s-%s_%s", 
		    Config.LOCAL_LOGS_PATH, jobID, hostname, processID); 
	}
	
	public boolean isFinish() {
		return isFinished;
	}
	
	public int getNextLine() {
		return nextLine;
	}
}
