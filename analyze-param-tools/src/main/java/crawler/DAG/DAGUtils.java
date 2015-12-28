package crawler.DAG;

import java.util.ArrayList;
import java.util.List;

import crawler.env.pojo.SparkSummary;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import recommend.DAG.JobDAG;
import common.Config;
import crawler.DAG.pojo.Job;
import crawler.DAG.pojo.Stage;
import crawler.request.JsonArrayRequest;
import crawler.request.TextRequest;

public class DAGUtils {
	private static Logger logger = Logger.getLogger(DAGUtils.class);
	private static Config conf = Config.getInstance();
	
	public static List<Job> getJobsByAppSummary(SparkSummary summary) {
        String attemptId = summary.getAttempts().get(0).getAttemptId();
        String apiPath;
        if (attemptId == null) {
            apiPath = String.format("%s/api/v1/applications/%s/jobs",
                    conf.getHistServerPath(), summary.getId());
        } else {
            apiPath = String.format("%s/api/v1/applications/%s/%s/jobs/",
                    conf.getHistServerPath(), summary.getId(), attemptId);
        }
		JsonArrayRequest<Job> request = new JsonArrayRequest<Job>(Job.class);
		return request.handle(apiPath);
	}
//
//	public static List<Stage> getStagesByAppId(String appid) {
//		String apiPath = String.format("%s/api/v1/applications/%s/stages", conf.getHistServerPath(), appid);
//		JsonArrayRequest<Stage> request = new JsonArrayRequest<Stage>(Stage.class);
//		return request.handle(apiPath);
//	}
//
//	public static String getDotByStage(String appid, Integer stageid) {
//		String apiPath = String.format("%s/history/%s/stages/stage/?id=%d&attempt=0", conf.getHistServerPath(), appid, stageid);
//		TextRequest request = new TextRequest();
//		String html = request.handle(apiPath);
//		Document doc = Jsoup.parse(html);
//		Elements elems = doc.getElementsByClass("dot-file");
//		return elems.first().text();
//	}
//
//	public static List<String> getStageDotByAppId(String appid) {
//		List<Stage> stages = getStagesByAppId(appid);
//		List<String> dotFiles = new ArrayList<String>();
//		for (Stage stage : stages) {
//			String dotFile = getDotByStage(appid, stage.getStageId());
//			dotFiles.add(dotFile);
//		}
//		return dotFiles;
//	}

	public static List<JobDAG> getJobDAGsByAppSummary(SparkSummary summary) {
        List<JobDAG> ret = new ArrayList<JobDAG>();
        for (Job job : getJobsByAppSummary(summary)) {
            Integer jobid = job.getJobId();
            ret.add(getJobDAG(summary, jobid));
        }
        return ret;
    }
	
	public static JobDAG getJobDAG(SparkSummary summary, Integer jobid) {
        String attemptId = summary.getAttempts().get(0).getAttemptId();
		String apiPath;
        if (attemptId == null) {
            apiPath = String.format("%s/history/%s/jobs/job/?id=%d",
                    conf.getHistServerPath(), summary.getId(), jobid);
        } else {
            apiPath = String.format("%s/history/%s/%s/jobs/job/?id=%d",
                    conf.getHistServerPath(), summary.getId(), attemptId, jobid);
        }

		TextRequest request = new TextRequest();
		String html = request.handle(apiPath);
		JobDAGParser parser = new JobDAGParser(html);
		JobDAG jobDAG = parser.getJobDAG();
        jobDAG.setJobid(jobid);

        return jobDAG;
	}
}
