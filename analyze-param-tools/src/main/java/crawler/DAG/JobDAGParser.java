package crawler.DAG;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import common.Config;
import crawler.request.TextRequest;
import recommend.DAG.JobDAG;
import recommend.DAG.Stage;
import recommend.DAG.StageDAG;

public class JobDAGParser {
	private String html;
	
	public JobDAGParser(String html) {
		this.html = html;
	}
	
	public JobDAG getJobDAG() {
		JobDAG jobDAG = new JobDAG();
		for (Stage stage : getStageList()) {
			StageDAG stageDAG = StageDAG.constructInstance(stage);
			jobDAG.addStageDAG(stageDAG);
		}

		if (jobDAG.getNodeSet().size() == 0) {
			return null;
		} else {
			return jobDAG;
		}
	}
	
	public List<Stage> getStageList() {
		String newlineKeeped = html.replaceAll("\r\n|\r|\n|\n\r", "br2n");
		Document doc = Jsoup.parse(newlineKeeped);
		Elements stageElems = doc.getElementsByClass("stage-metadata");
		List<Stage> stageList = new ArrayList<Stage>();
		for (Element stageElem : stageElems) stageList.add(toStage(stageElem));
		return stageList;
	}
	
	private Stage toStage(Element elem) {
		Integer stageId = Integer.parseInt(elem.attr("stage-id"));
		Boolean skipped = Boolean.parseBoolean(elem.attr("skipped"));
		String dotFile = elem.getElementsByClass("dot-file").first().text().replaceAll("br2n", "\n");
		Stage stage = new Stage();
		stage.setStageId(stageId);
		stage.setSkipped(skipped);
		stage.setDotFile(dotFile);
		for (Element e : elem.getElementsByClass("incoming-edge")) stage.addIncomingEdge(e.text());
		for (Element e : elem.getElementsByClass("cached-rdd")) stage.addCachedRdd(Integer.parseInt(e.text()));
		return stage;
	}
	
}
