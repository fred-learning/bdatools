package recommend.DAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import recommend.basic.DAG;
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

	/*
	public DAG toDAG() {
		// construct node id to reference index
		Map<String, Integer> id_to_idx = new HashMap<String, Integer>();
		int idx_so_far = 0;
		for (Nodem n : nodeSet) id_to_idx.put(n.getId(), idx_so_far++);

		// convert nodeSet to arraylist
		ArrayList<Nodem> nodeList = new ArrayList<Nodem>();
		for (int i = 0; i < idx_so_far; ++ i) nodeList.add(null); // init list size
		for (Nodem n : nodeSet) {
			int idx = id_to_idx.get(n.getId());
			nodeList.set(idx, n);
		}
		
		// convert edgeMap to arraylist<arraylist>
		ArrayList<LinkedList<Integer>> edges = new ArrayList<LinkedList<Integer>>();
		for (int i = 0; i < idx_so_far; ++ i) edges.add(null); // init list size
		for (Nodem n : nodeSet) {
			LinkedList<Integer> list = new LinkedList<Integer>();
			Set<String> destSet = edgeMap.getDests(n.getId());
			if (destSet != null) {
				for (String dest_id : destSet) {
					int dest_idx = id_to_idx.get(dest_id);
					list.add(dest_idx);
				}
			}
			int src_idx = id_to_idx.get(n.getId());
			edges.set(src_idx, list);
		}
		
		return new DAG(edges, nodeList);
	}
	*/
}
