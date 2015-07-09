package lab.paramcfg.backend.storage.graph.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import lab.paramcfg.backend.storage.graph.crawler.DAG;
import lab.paramcfg.backend.storage.graph.crawler.Nodem;

import org.apache.log4j.Logger;
import org.processmining.analysis.graphmatching.algos.DistanceAlgo;
import org.processmining.analysis.graphmatching.algos.GraphEditDistanceAStarSim;
import org.processmining.analysis.graphmatching.graph.SimpleGraph;


public class GraphMatching {
    private Logger logger = Logger.getLogger(GraphMatching.class);
	
	public double similarity(DAG firstDag, DAG secondDag){
		DistanceAlgo distanceAlgo = new GraphEditDistanceAStarSim();
		Object weights[] = new Object[6];
		weights[0] = "vweight";
		weights[1] = 0.2;
		weights[2] = "sweight";
		weights[3] = 0.3;
		weights[4] = "eweight";
		weights[5] = 0.5;
		distanceAlgo.setWeight(weights);
		
		Set<Integer> vertices1 = new HashSet<Integer>();
		Set<Integer> vertices2 = new HashSet<Integer>();
		Map<Integer,Set<Integer>> outgoingEdges1 = new HashMap<Integer,Set<Integer>>();
		Map<Integer,Set<Integer>> outgoingEdges2 = new HashMap<Integer,Set<Integer>>();
		Map<Integer,Set<Integer>> incomingEdges1 = new HashMap<Integer,Set<Integer>>();
		Map<Integer,Set<Integer>> incomingEdges2 = new HashMap<Integer,Set<Integer>>();
		
		ArrayList<Nodem> nodes1 = firstDag.getNodems();
		ArrayList<Nodem> nodes2 = secondDag.getNodems();
		ArrayList<LinkedList<Integer>> edges1 = firstDag.getEdges();
		ArrayList<LinkedList<Integer>> edges2 = secondDag.getEdges();
		HashMap<Integer, Integer> idIdxMap1 = new HashMap<Integer, Integer>(); 
		HashMap<Integer, Integer> idIdxMap2 = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> idxIdMap1 = new HashMap<Integer, Integer>(); 
		HashMap<Integer, Integer> idxIdMap2 = new HashMap<Integer, Integer>();
		Map<Integer,String> labels1 = new HashMap<Integer,String>();
		Map<Integer,String> labels2 = new HashMap<Integer,String>();
		
		logger.info(nodes1.size());
		logger.info(nodes2.size());
		
		/* initialize idMap1 and idMap2 */
		for (int i = 0; i < nodes1.size(); i++) {
			idIdxMap1.put(Integer.parseInt(nodes1.get(i).getId()), i);
			idxIdMap1.put(i, Integer.parseInt(nodes1.get(i).getId()));
		}
		for (int i = 0; i < nodes2.size(); i++) {
			idIdxMap2.put(Integer.parseInt(nodes2.get(i).getId()), i);
			idxIdMap2.put(i, Integer.parseInt(nodes2.get(i).getId()));
		}
		
		/* initialize vertices1 and vertices2 */
		for(Nodem node:nodes1){
			vertices1.add(Integer.parseInt(node.getId()));
		}
		for(Nodem node:nodes2){
			vertices2.add(Integer.parseInt(node.getId()));
		}
		

		
		/* initialize outgoingEdges1 and outgoingEdges2 */
		for(int i=0; i<edges1.size(); i++){
			LinkedList<Integer> edge = edges1.get(i); 
			Set<Integer> tmpSet = new HashSet<Integer>();
			for(Integer idx: edge){
				tmpSet.add(idx);
			}
			outgoingEdges1.put(Integer.parseInt(nodes1.get(i).getId()), tmpSet);
		}
		
		for(int i=0; i<edges2.size(); i++){
			LinkedList<Integer> edge = edges2.get(i); 
			Set<Integer> tmpSet = new HashSet<Integer>();
			for(Integer idx: edge){
				tmpSet.add(idx);
			}
			outgoingEdges2.put(Integer.parseInt(nodes2.get(i).getId()), tmpSet);
		}
		
		/* initialize incomingEdges1 and incomingEdges2 */
		Vector<Set<Integer>> tmpSets = new Vector<Set<Integer>>();
		for(int i=0; i<edges1.size(); i++){
			tmpSets.add(new HashSet<Integer>());
		}
		for(int i=0; i<edges1.size(); i++){
			LinkedList<Integer> edge = edges1.get(i); 
			for(Integer id: edge){
				tmpSets.get(idIdxMap1.get(id)).add(idxIdMap1.get(i));
			}
		}
		for(int i=0; i<edges1.size(); i++){
			incomingEdges1.put(idxIdMap1.get(i), tmpSets.get(i));
		}
		
		tmpSets = new Vector<Set<Integer>>();
		for(int i=0; i<edges2.size(); i++){
			tmpSets.add(new HashSet<Integer>());
		}
		for(int i=0; i<edges2.size(); i++){
			LinkedList<Integer> edge = edges2.get(i); 
			for(Integer id: edge){
				tmpSets.get(idIdxMap2.get(id)).add(idxIdMap2.get(i));
			}
		}
		for(int i=0; i<edges2.size(); i++){
			incomingEdges2.put(idxIdMap2.get(i), tmpSets.get(i));
		}
		
		/* initialize labels1 and labels2 */
		for(Nodem n: nodes1){
			labels1.put(Integer.parseInt(n.getId()), n.getLabel());
		}
		for(Nodem n: nodes2){
			labels2.put(Integer.parseInt(n.getId()), n.getLabel());
		}
		
		/* construct two graph  */
		SimpleGraph sg1 = new SimpleGraph(vertices1, outgoingEdges1, incomingEdges1, labels1, null, null, null);
		SimpleGraph sg2 = new SimpleGraph(vertices2, outgoingEdges2, incomingEdges2, labels2, null, null, null);
		
		/* compute the similarity by distanceAlgo */
		double sim = distanceAlgo.compute(sg1, sg2);
		
		return sim;
	}

}

