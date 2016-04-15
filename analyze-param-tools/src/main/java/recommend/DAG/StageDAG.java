package recommend.DAG;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alexmerz.graphviz.TokenMgrError;
import org.apache.log4j.Logger;

import recommend.basic.EdgeMap;
import recommend.basic.Nodem;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import common.TypeUtil;

public class StageDAG {
	private static Logger logger = Logger.getLogger(StageDAG.class);
	private Stage stage;
	private Set<Nodem> nodeSet;
	private EdgeMap edgeMap;

	public static StageDAG constructInstance(Stage stage) {
		StageDAG dag = new StageDAG(stage);
		try {
			dag.construct();
			return dag;
		} catch (TokenMgrError e) {
			logger.error("Parse dag error.", e);
			return null;
		} catch (Exception e) {
			logger.error("Unknown error.", e);
			return null;
		}
	}

	private StageDAG(Stage stage) {
		this.stage = stage;
	}
	
	private void construct() throws Exception, TokenMgrError {
		Parser p = new Parser();
		p.parse(new StringReader(stage.getDotFile()));
		Graph graph = p.getGraphs().get(0);
		nodeSet = getNodeSet(graph);
		edgeMap = getEdgeMap(graph);
	}
	
	private Set<Nodem> getNodeSet(Graph graph) {
		Set<Nodem> nodeList = new HashSet<Nodem>();
		for (Node nn: graph.getNodes(false)) {
			String id = nn.getId().getId().trim();
			String label = nn.getAttribute("label");
			if (TypeUtil.isInteger(id) && label != null) {
				// only extract xxxRDD part. Replace hdfs input path by HDFSInputRDD.
				label = label.startsWith("hdfs") ? "HDFSInputRDD" : label.split(" ")[0];
				nodeList.add(new Nodem(id, label));
			}
		}
		return nodeList;
	}
	
	private EdgeMap getEdgeMap(Graph graph) {
		EdgeMap edgeMap = new EdgeMap();
		for (Edge edge : graph.getEdges()) {
			String start = edge.getSource().toString().replaceAll("\\[.*\\];\n:", "").trim();
            String end = edge.getTarget().toString().replaceAll("\\[.*\\];\n:", "").trim();
            edgeMap.addEdge(start, end);
		}
		
		for (String edge : stage.getIncomingEdgeList()) {
			String[] args = edge.trim().split(",");
			if (args.length != 2) continue;
			edgeMap.addEdge(args[0], args[1]);
		}
		
		return edgeMap;
	}

	public Set<Nodem> getNodeSet() {
		return nodeSet;
	}

	public EdgeMap getEdgeMap() {
		return edgeMap;
	}
}
