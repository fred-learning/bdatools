package lab.paramcfg.backend.storage.graph.crawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class DAG{
	ArrayList<LinkedList<Integer>> edges ;
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
	  
	  sb.append("nodes:\n");
	  for (Nodem n : nodems) {
	    sb.append( String.format("\t(%s, %s)\n", n.getId(), n.getLabel()) );
	  }
	  
	  sb.append("edges\n");
	  for (LinkedList<Integer> nexts: edges) {
	    sb.append(Arrays.toString(nexts.toArray()) + "\n");
	  }
	  
	  return sb.toString();
	}
	
}