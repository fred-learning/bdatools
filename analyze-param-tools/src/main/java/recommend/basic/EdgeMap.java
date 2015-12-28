package recommend.basic;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// warpper for graph edges
public class EdgeMap {
	private Map<String, Set<String>> M;
	
	public EdgeMap() {
		M = new HashMap<String, Set<String>>();
	}
	
	public void addEdge(String src, String dst) {
		Set<String> S = M.get(src);
		if (S == null) {
			S = new HashSet<String>();
			M.put(src, S);
		}
		S.add(dst);
	}
	
	public void addEdges(String src, Collection<String> C) {
		for (String i : C) addEdge(src, i);
	}
	
	public void addEdges(EdgeMap another) {
		for (Map.Entry<String, Set<String>> e : another.M.entrySet()) {
			String src = e.getKey();
			Set<String> dstSet = e.getValue();
			addEdges(src, dstSet);
		}
	}
	
	public Set<String> getDests(String src) {
		Set<String> dests = M.get(src);
		return dests == null ? new HashSet<String>() : dests;
	}

	public Long getEdgeNum() {
		long ret = 0;
		for (Map.Entry<String, Set<String>> e : M.entrySet()) {
			ret += e.getValue().size();
		}
		return ret;
	}
}
