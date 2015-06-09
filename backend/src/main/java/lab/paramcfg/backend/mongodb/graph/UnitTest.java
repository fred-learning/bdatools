package lab.paramcfg.backend.mongodb.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.processmining.analysis.graphmatching.algos.DistanceAlgo;
import org.processmining.analysis.graphmatching.algos.GraphEditDistanceAStarSim;
import org.processmining.analysis.graphmatching.graph.SimpleGraph;

public class UnitTest {
	
	public static void main(String args[]) {
		try {
			Document doc = Jsoup.connect("http://pc57:18080/history/application_1433623322646_0019/jobs/job/?id=2").get();
			Elements newsHeadlines = doc.select(".dot-file");
			String tt = newsHeadlines.html();
			System.out.println(tt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
//	@SuppressWarnings("serial")
//	public static void main(String args[]){
//		DistanceAlgo distanceAlgo = new GraphEditDistanceAStarSim();
//		Object weights[] = new Object[6];
//		weights[0] = "vweight";
//		weights[1] = 0.2;
//		weights[2] = "sweight";
//		weights[3] = 0.3;
//		weights[4] = "eweight";
//		weights[5] = 0.5;
//		distanceAlgo.setWeight(weights);
//		 //SimpleGraph(Set<Integer> vertices, Map<Integer,Set<Integer>> outgoingEdges, Map<Integer,Set<Integer>> incomingEdges, Map<Integer,String> labels, Set<Integer> functionVertices, Set<Integer> eventVertices, Set<Integer> connectorVertices)
//		
//		Set<Integer> vertices1 = new HashSet<Integer>() {{ add(1); add(2); add(3); add(4); add(5);}};
//		Set<Integer> vertices2 = new HashSet<Integer>() {{ add(5); add(6); add(7); add(8);}};
//		
// 		Map<Integer,Set<Integer>> outgoingEdges1 = new HashMap<Integer,Set<Integer>>() {{ 
//	        put(1,  new HashSet<Integer>() {{ add(2); add(3);}}); 
//	        put(2,  new HashSet<Integer>() {{ add(5); }}); 
//	        put(3,  new HashSet<Integer>() {{ add(4); }}); 
//	        put(4,  new HashSet<Integer>() {{ add(5); }}); 
//	        put(5,  new HashSet<Integer>() {{}}); 
//		}};
//		
//		Map<Integer,Set<Integer>> outgoingEdges2 = new HashMap<Integer,Set<Integer>>() {{ 
//	        put(5,  new HashSet<Integer>() {{ add(6); add(7);}}); 
//	        put(6,  new HashSet<Integer>() {{ add(8); }}); 
//	        put(7,  new HashSet<Integer>() {{ add(8); }}); 
//	        put(8,  new HashSet<Integer>() {{  }}); 
//		}};
//		
//		Map<Integer,Set<Integer>> incomingEdges1 = new HashMap<Integer,Set<Integer>>() {{ 
//	        put(1,  new HashSet<Integer>() {{ }}); 
//	        put(2,  new HashSet<Integer>() {{ add(1); }}); 
//	        put(3,  new HashSet<Integer>() {{ add(1); }}); 
//	        put(4,  new HashSet<Integer>() {{ add(3); }}); 
//	        put(5,  new HashSet<Integer>() {{ add(2); add(4); }});
//		}};
//		
//		Map<Integer,Set<Integer>> incomingEdges2 = new HashMap<Integer,Set<Integer>>() {{ 
//			put(5,  new HashSet<Integer>() {{ }}); 
//	        put(6,  new HashSet<Integer>() {{ add(5); }}); 
//	        put(7,  new HashSet<Integer>() {{ add(5); }}); 
//	        put(8,  new HashSet<Integer>() {{ add(6); add(7); }}); 
//		}};
//		
//		Map<Integer,String> labels1 = new HashMap<Integer,String>() {{ 
//	        put(1, "map"); 
//	        put(2, "reduce"); 
//	        put(3, "reduce"); 
//	        put(4, "join"); 
//	        put(5, "group");
//		}};
//		
//		Map<Integer,String> labels2 = new HashMap<Integer,String>() {{ 
//			put(5, "map"); 
//	        put(6, "reduce"); 
//	        put(7, "join"); 
//	        put(8, "group"); 
//		}};
//		
//		SimpleGraph sg1 = new SimpleGraph(vertices1, outgoingEdges1, incomingEdges1, labels1, null, null, null);
//		SimpleGraph sg2 = new SimpleGraph(vertices2, outgoingEdges2, incomingEdges2, labels2, null, null, null);
//		
//		double sim = distanceAlgo.compute(sg1, sg2);
//		
//		System.out.println(sim + "\n");
//	}
}
