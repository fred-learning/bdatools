package recommend.graphmatch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import recommend.DAG.JobDAG;
import recommend.basic.Nodem;

/* 
 * Helper class for converting DAG into structure that GraphMatch graphmatch requires.
 */
public class GraphMatchingUtil {
	
	public static Set<Integer> getVerticeIndex(JobDAG dag) {
		Set<Integer> S = new HashSet<Integer>();
		for (Nodem n : dag.getNodeSet()) {
			S.add(Integer.parseInt(n.getId()));
		}
		return S;
	}
	
	public static Map<Integer, Set<Integer>> getOutgoingEdge(JobDAG dag) {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		for (Nodem n : dag.getNodeSet()) {
			Integer src = Integer.parseInt(n.getId());
			Set<Integer> dests = new HashSet<Integer>();
			for (String destStr : dag.getEdgeMap().getDests(n.getId())) {
				Integer dest = Integer.parseInt(destStr);
				dests.add(dest);
			}
			ret.put(src, dests);
		}
		return ret;
	}
	
	public static Map<Integer, Set<Integer>> getIncomingEdge(JobDAG dag) {
		Map<Integer, Set<Integer>> ret = new HashMap<Integer, Set<Integer>>();
		for (Nodem n : dag.getNodeSet()) ret.put(Integer.parseInt(n.getId()), new HashSet<Integer>());
		
		for (Nodem n : dag.getNodeSet()) {
			Integer src = Integer.parseInt(n.getId());
			for (String destStr : dag.getEdgeMap().getDests(n.getId())) {
				Integer dest = Integer.parseInt(destStr);
				ret.get(dest).add(src);
			}
		}
		return ret;
	}
	
	public static Map<Integer, String> getLabels(JobDAG dag) {
		Map<Integer, String> m = new HashMap<Integer, String>();
		for (Nodem n : dag.getNodeSet()) {
			Integer id = Integer.parseInt(n.getId());
			m.put(id, n.getLabel());
		}
		return m;
	}
}
