package lab.paramcfg.backend.storage.graph;


import lab.paramcfg.backend.storage.graph.crawler.DAG;
import lab.paramcfg.backend.storage.graph.crawler.WebCrawler;
import lab.paramcfg.backend.storage.graph.match.GraphMatching;


public class RDDSData {

    private String jobid;
	private DAG dag;

	public RDDSData(String jobid) {
		this.jobid = jobid;
		this.dag = new WebCrawler().getDAG(jobid);
	}

	public double similarity(RDDSData other) {
		return new GraphMatching().similarity(this.dag, other.dag);
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

}
