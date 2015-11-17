package crawler.DAG;

import java.util.List;

import common.Config;
import crawler.DAG.pojo.Job;
import crawler.DAG.pojo.Stage;

public class DAGUtils {
	private static Config conf = Config.getInstance();
	
	public static List<Job> getJobsByAppId(String appid) {
		String apiPath = String.format("%s/api/v1/applications/%s/jobs", conf.getHistServerPath(), appid);
		JsonArrayRequest<Job> request = new JsonArrayRequest<Job>(Job.class);
		return request.handle(apiPath);
	}
	
	
	public static List<Stage> getStagesByAppId(String appid) {
		String apiPath = String.format("%s/api/v1/applications/%s/stages", conf.getHistServerPath(), appid);
		JsonArrayRequest<Stage> request = new JsonArrayRequest<Stage>(Stage.class);
		return request.handle(apiPath);
	}
	
	public static void main(String[] args) {
		List<Stage> jobs = DAGUtils.getStagesByAppId("application_1447660331941_0006");
		for (Stage job : jobs) System.out.println(job.getStageId());
	}
	
}
