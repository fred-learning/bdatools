package lab.paramcfg.backend.mongodb;

import java.io.Serializable;

import lab.paramcfg.backend.mongodb.graph.GraphMatching;
import lab.paramcfg.backend.mongodb.webCrawler.DAG;
import lab.paramcfg.backend.mongodb.webCrawler.WebCrawler;


@SuppressWarnings("serial")
public class RDDSData implements Serializable {

	private String jobid;
	private DAG dag;

	public RDDSData(String jobid) {
		this.jobid = jobid;
		this.dag = new WebCrawler().getDAG(jobid);
	}

	public double similarity(RDDSData other) {
		return new GraphMatching().similarity(this.dag, other.dag);
//		return 0;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

}
