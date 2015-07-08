package lab.paramcfg.backend.mongodb.webCrawler;

import java.util.ArrayList;
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
	
}