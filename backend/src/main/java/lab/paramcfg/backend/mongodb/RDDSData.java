package lab.paramcfg.backend.mongodb;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

class DAG {
	ArrayList<LinkedList<Integer>> edges;
	ArrayList<Nodem> nodems;

	public DAG(ArrayList<LinkedList<Integer>> edges, ArrayList<Nodem> nodems) {
		this.edges = edges;
		this.nodems = nodems;
	}
}

class Nodem {
	private String id = null;
	private String label = null;

	public Nodem(String id, String label) {
		this.id = id;
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

}

@SuppressWarnings("serial")
public class RDDSData implements Serializable {

	private String jobid;
	private static ArrayList<LinkedList<Integer>> graph;
	private static ArrayList<Nodem> nodems = new ArrayList<Nodem>();
	private static Hashtable<String, Integer> maps = new Hashtable<String, Integer>();

	public RDDSData(String jobid) {
		this.jobid = jobid;
		getGraph(jobid);
	}

	public double similarity(RDDSData other) {
		return 0;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	public void getGraph(String appid) {
		WebCrawler crawler = new WebCrawler();
		String url = "http://pc57:18080/history/" + appid + "/jobs/";
		String content = crawler.getContentFromUrl(url);

		String jobsNumReg = "Completed Jobs \\((\\d+)\\)";
		String dagDataReg = "(<div class=\"dot-file\">(digraph G \\{.+\\})</div>)";
		Pattern pattern = Pattern.compile(jobsNumReg); // , Pattern.MULTILINE |
														// Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);

		int jobNum = 0;
		if (matcher.find()) {
			try {
				jobNum = Integer.parseInt(matcher.group(1));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("no jobs completed.");
				return;
			}
			if (jobNum > 0) {
				url = url + "job/?id=" + (jobNum - 1);
				content = crawler.getContentFromUrl(url);// .replaceAll("\n",
															// "");
				pattern = Pattern.compile(dagDataReg, Pattern.MULTILINE
						| Pattern.DOTALL);
				matcher = pattern.matcher(content);
				String finalStr = "";
				if (matcher.find()) {
					finalStr = matcher.group(0);
					finalStr = finalStr.replaceAll("&quot;", "\"");
					finalStr = finalStr.replaceAll("&gt;", ">");
					finalStr = finalStr
							.replaceAll(
									"</div><div class=\"stage-metadata\" stageId=\"stage_(\\d+)\" style=\"display:none\">",
									"");
					finalStr = finalStr.replaceAll("<div class=\"dot-file\">",
							"");
					// System.out.print(finalStr);
				}
				String graphsStr = finalStr.replaceAll(
						"\\}</div>[ \n]*digraph G \\{", "").replaceAll(
						"</div>", "");
				// String[] graphsStr = finalStr.split("</div>");
				// System.out.print(graphsStr);

				StringReader reader = new StringReader(graphsStr);
				Parser p = null;
				try {
					p = new Parser();
					p.parse(reader);
				} catch (Exception e) {
					e.printStackTrace();
				}

				ArrayList<Graph> gl = p.getGraphs();
				// System.out.println(gl.get(0).getNodes(false).get(0).getId());

				ArrayList<Node> nodes = gl.get(0).getNodes(false);
				int counter = 0;
				for (Node n : nodes) {
					String id = n.getId().getId().trim();
					if (isNumeric(id)) {
						nodems.add(new Nodem(id, ""));
						maps.put(id, counter++);
					}
					// System.out.println(id);
				}
				graph = new ArrayList<LinkedList<Integer>>(nodes.size());
				for (int i = 0; i < nodes.size(); i++) {
					graph.add(new LinkedList<Integer>());
				}

				ArrayList<Edge> edges = gl.get(0).getEdges();
				for (Edge e : edges) {
					String start = e.getSource().toString()
							.replaceAll("\\[.*\\];\n:", "").trim();
					String end = e.getTarget().toString()
							.replaceAll("\\[.*\\];\n:", "").trim();
					int endint = Integer.parseInt(end);
					if (null == graph.get(maps.get(start))) {
						graph.set(maps.get(start), new LinkedList<Integer>());
					}
					if (!graph.get(maps.get(start)).contains(endint)) {
						graph.get(maps.get(start)).add(endint);
					}
				}
			}

		}
	}

	protected static boolean isNumeric(String s) {
		return s.trim().matches("[-+]?\\d*\\.?\\d+");
	}

}
