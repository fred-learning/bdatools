package crawler.DAG;

import java.util.ArrayList;
import java.util.List;

import com.mashape.unirest.http.exceptions.UnirestException;
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

	public static List<JobDAG> getJobDAGsByAppSummary(SparkSummary summary) throws UnirestException {
        List<JobDAG> ret = new ArrayList<JobDAG>();
        for (Job job : getJobsByAppSummary(summary)) {
            Integer jobid = job.getJobId();
            JobDAG jobDAG = getJobDAG(summary, jobid);
            if (jobDAG != null) ret.add(jobDAG);
        }
        return ret;
    }

    private static List<Job> getJobsByAppSummary(SparkSummary summary) throws UnirestException {
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
	
	private static JobDAG getJobDAG(SparkSummary summary, Integer jobid) throws UnirestException {
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
        if (jobDAG == null) {
            return null;
        } else {
            jobDAG.setJobid(jobid);
            return jobDAG;
        }
	}
}
