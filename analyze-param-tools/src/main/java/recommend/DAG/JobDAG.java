package recommend.DAG;

import java.util.HashSet;
import java.util.Set;

import recommend.basic.EdgeMap;
import recommend.basic.Nodem;

public class JobDAG {
	private Integer jobid = -1;
	private EdgeMap edgeMap;
	private Set<Nodem> nodeSet;
	
	public JobDAG() {
		edgeMap = new EdgeMap();
		nodeSet = new HashSet<Nodem>();
	}
	
	public void addStageDAG(StageDAG stageDAG) {
		if (stageDAG == null) return;
		edgeMap.addEdges(stageDAG.getEdgeMap());
		nodeSet.addAll(stageDAG.getNodeSet());
	}
	
	public EdgeMap getEdgeMap() {
		return edgeMap;
	}

	public Set<Nodem> getNodeSet() {
		return nodeSet;
	}

	public Integer getJobid() {
		return jobid;
	}

	public void setJobid(Integer jobid) {
		this.jobid = jobid;
	}

}
