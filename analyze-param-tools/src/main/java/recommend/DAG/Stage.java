package recommend.DAG;

import java.util.ArrayList;
import java.util.List;

public class Stage {
	private Integer stageId;
	private Boolean skipped;
	private String dotFile;
	private List<Integer> cachedRddList;
	private List<String> incomingEdgeList;
	
	public Stage() {
		stageId = -1;
		dotFile = "DUMMY";
		skipped = true;
		cachedRddList = new ArrayList<Integer>();
		incomingEdgeList = new ArrayList<String>();
	}
	
	public Integer getStageId() {
		return stageId;
	}
	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}
	public Boolean getSkipped() {
		return skipped;
	}
	public void setSkipped(Boolean skipped) {
		this.skipped = skipped;
	}
	public String getDotFile() {
		return dotFile;
	}
	public void setDotFile(String dotFile) {
		this.dotFile = dotFile;
	}
	public List<Integer> getCachedRddList() {
		return cachedRddList;
	}
	public void addCachedRdd(Integer i) {
		for (Integer j : cachedRddList) if (i == j) return;
		cachedRddList.add(i);
	}
	public List<String> getIncomingEdgeList() {
		return incomingEdgeList;
	}
	public void addIncomingEdge(String s) {
		for (String s2 : incomingEdgeList) if (s.equals(s2)) return;
		incomingEdgeList.add(s);
	}
	
	@Override
	public String toString() {
		int maxDotLen = Math.min(dotFile.length(), 20);
		String msg = String.format("||StageId:%d, Skipped:%b, CachedRdd:%s, incomingEdge:%s, dotFile:%s...||", 
				stageId, skipped, cachedRddList, incomingEdgeList, dotFile.substring(0, maxDotLen)); 
		return msg;
	}
}
