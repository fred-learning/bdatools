package recommend.graphmatch;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.processmining.analysis.graphmatching.algos.DistanceAlgo;
import org.processmining.analysis.graphmatching.algos.GraphEditDistanceAStarSim;
import org.processmining.analysis.graphmatching.algos.GraphEditDistanceGreedy;
import org.processmining.analysis.graphmatching.graph.SimpleGraph;

import recommend.DAG.JobDAG;

public class GraphMatching {
	private static Logger logger = Logger.getLogger(GraphMatching.class);
	
	public static double similarity(JobDAG firstDag, JobDAG secondDag){
		DistanceAlgo distanceAlgo = new GraphEditDistanceGreedy();
		Object weights[] = new Object[6];
		weights[0] = "vweight";
		weights[1] = 0.5;
		weights[2] = "sweight";
		weights[3] = 0.0;
		weights[4] = "eweight";
		weights[5] = 0.5;
		distanceAlgo.setWeight(weights);

		Set<Integer> vertices1 = GraphMatchingUtil.getVerticeIndex(firstDag);
		Set<Integer> vertices2 = GraphMatchingUtil.getVerticeIndex(secondDag);
		Map<Integer,Set<Integer>> outgoingEdges1 = GraphMatchingUtil.getOutgoingEdge(firstDag);
		Map<Integer,Set<Integer>> outgoingEdges2 = GraphMatchingUtil.getOutgoingEdge(secondDag);
		Map<Integer,Set<Integer>> incomingEdges1 = GraphMatchingUtil.getIncomingEdge(firstDag);
		Map<Integer,Set<Integer>> incomingEdges2 = GraphMatchingUtil.getIncomingEdge(secondDag);
		Map<Integer,String> labels1 = GraphMatchingUtil.getLabels(firstDag);
		Map<Integer,String> labels2 = GraphMatchingUtil.getLabels(secondDag);
		
		logger.debug("vertices1: " + vertices1);
		logger.debug("outgoingEdges1: " + outgoingEdges1);
		logger.debug("incomingEdges1: " + incomingEdges1);
		logger.debug("labels1: " + labels1);
		logger.debug("vertices2: " + vertices2);
		logger.debug("outgoingEdges2: " + outgoingEdges2);
		logger.debug("incomingEdges2: " + incomingEdges2);
		logger.debug("labels2: " + labels2);
		
		SimpleGraph sg1 = new SimpleGraph(vertices1, outgoingEdges1, incomingEdges1, labels1, null, null, null);
		SimpleGraph sg2 = new SimpleGraph(vertices2, outgoingEdges2, incomingEdges2, labels2, null, null, null);
		
		/* compute the similarity by distanceAlgo */
		double sim = 1 - distanceAlgo.compute(sg1, sg2);
		
		return sim;
	}
	
}
