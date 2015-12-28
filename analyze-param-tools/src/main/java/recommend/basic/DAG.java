package recommend.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class DAG {
	ArrayList<LinkedList<Integer>> edges;
	ArrayList<Nodem> nodems;
	
	public ArrayList<LinkedList<Integer>> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<LinkedList<Integer>> edges) {
		this.edges = edges;
	}
	public ArrayList<Nodem> getNodems() {
		return nodems;
	}
	public void setNodems(ArrayList<Nodem> nodems) {
		this.nodems = nodems;
	}
	
	public DAG(ArrayList<LinkedList<Integer>> edges, ArrayList<Nodem> nodems){
		this.edges = edges;
		this.nodems = nodems;
	}
	
	public String toString() {
	  StringBuilder sb = new StringBuilder();
	  
	  int idx_so_far = 0;
	  sb.append("nodes:\n");
	  for (Nodem n : nodems) {
	    sb.append( String.format("\t%s -- (%s, %s)\n", idx_so_far++, n.getId(), n.getLabel()) );
	  }
	  
	  idx_so_far = 0;
	  sb.append("edges\n");
	  for (LinkedList<Integer> nexts: edges) {
		  String msg = String.format("\t%d -- %s\n", idx_so_far++, Arrays.toString(nexts.toArray()));
		  sb.append( msg);
	  }
	  
	  return sb.toString();
	}
}
