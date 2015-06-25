package lab.paramcfg.backend.mongodb.webCrawler;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lab.paramcfg.backend.common.Config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

public class WebCrawler {
	
	 /**  
     * 根据URL抓取网页内容  
     *   
     * @param url  
     * @return  
     */
    public String getContentFromUrl(String url)  
    {  
        /* 实例化一个HttpClient客户端 */
		HttpClient client = HttpClientBuilder.create().build();
        HttpGet getHttp = new HttpGet(url);  
    
        String content = null;  
    
        HttpResponse response;  
        try
        {  
            /*获得信息载体*/
            response = client.execute(getHttp);  
            HttpEntity entity = response.getEntity();  
            
            if (entity != null)  
            {  
                /* 转化为文本信息 */
                content = EntityUtils.toString(entity);
                
            }  
    
        } 
        catch (Exception e)  
        {  
            //e.printStackTrace();
            System.out.println("read page from url fail.");
            return null;
        }
             
        return content;  
    }  
	
    public DAG getDAG(String appId)
    {  	
    	ArrayList<LinkedList<Integer>> graph ;
        ArrayList<Nodem> nodems = new ArrayList<Nodem>();
    	Hashtable<String, Integer> maps = new Hashtable<String, Integer>();
    	/* used to crawl one page */
    	String url = Config.OP_DAG_PATH + "/history/" + appId + "/jobs/";
    	Document doc;
    	String content = null;
		try {			
			doc = Jsoup.connect(url).get();
			content = doc.html();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	if(null == content){
    		return null;
    	}    	
    	
    	/* extract the information of the job */
    	String jobsNumReg = "Completed Jobs \\((\\d+)\\)";
    	String dagDataReg = "(<div class=\"dot-file\">(digraph G \\{.+\\})</div>)";
    	Pattern pattern = Pattern.compile(jobsNumReg);     //, Pattern.MULTILINE | Pattern.DOTALL);  
        Matcher matcher=pattern.matcher(content); 
        
        int jobNum = 0;
        if(matcher.find()){
        	try{
        		jobNum = Integer.parseInt(matcher.group(1));
        	}catch(Exception e){
        		//e.printStackTrace();
        		System.out.println("no jobs completed.");
        		return null;
        	}
        	if(jobNum > 0){
        		
        		/* get the last one job */
        		url = url + "job/?id=" + (jobNum-1);
        		String tmpstr = getContentFromUrl(url).replaceAll("\r\n|\r|\n|\n\r", "br2n");
        		content = Jsoup.parse(tmpstr).select(".dot-file").html().replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("br2n", "\n");//.replaceAll("\r\n|\r|\n|\n\r", "\n");
        		
        		if(null == content){
            		return null;
            	}

        		String graphsStr = content;
        		StringReader reader = new StringReader(graphsStr);
        		
        		Parser p = null;
        		try {
                    p = new Parser();
                    p.parse(reader);
                } catch (Exception e) {
                	e.printStackTrace();
                }
                ArrayList<Graph> gl = p.getGraphs();
                ArrayList<Graph> gl2 = p.getGraphs();
                Queue<Graph> queue = new LinkedList<Graph>();
                if(gl2.size() == 0) return null;
                for(Graph tmpG: gl2){
                	queue.add(tmpG);
                }
                int counter = 0;
                while (!queue.isEmpty()) {
					Graph g = queue.poll();
					String label =  g.getAttribute("label");
					for(Node nn: g.getNodes(true)){
						String id = nn.getId().getId().trim();
						if(isNumeric(id)){
							if (null==label) {
								label = " ";
							}
	                		nodems.add(new Nodem(id, label));
	                		maps.put(id, counter++);
	                	}
					}
					
					gl2 = g.getSubgraphs();
					for(Graph tmpG: gl2){
	                	queue.add(tmpG);
	                }
				}
                
                /* add the adjacent nodes' id */
                graph = new ArrayList<LinkedList<Integer>>(nodems.size());
                for(int i=0; i<nodems.size(); i++){
                	graph.add(new LinkedList<Integer>());
                }
                
                ArrayList<Edge> edges = gl.get(0).getEdges();
                for(Edge e: edges) {
                	String start = e.getSource().toString().replaceAll("\\[.*\\];\n:", "").trim();
                    String end = e.getTarget().toString().replaceAll("\\[.*\\];\n:", "").trim();
                    int endint = Integer.parseInt(end);
                	if(null == graph.get(maps.get(start))){
                		graph.set(maps.get(start), new LinkedList<Integer>());
                	}
                	if(!graph.get(maps.get(start)).contains(endint)){
                		graph.get(maps.get(start)).add(endint);
                	}
                }
                
                // get different stages edges
                Elements incomingEdges  = Jsoup.parse(tmpstr).select(".incoming-edge");
                for(int i=0; i<incomingEdges.size(); i++){
                	String edge = incomingEdges.get(i).html();
                	String start = edge.split(",")[0].trim();
                    String end = edge.split(",")[0].trim();
                    int endint = Integer.parseInt(end);
                    if(null == graph.get(maps.get(start))){
                		graph.set(maps.get(start), new LinkedList<Integer>());
                	}
                	if(!graph.get(maps.get(start)).contains(endint)){
                		graph.get(maps.get(start)).add(endint);
                	}
                }
                
                DAG retDag = new DAG(graph, nodems);
                
                return retDag;
        	}
        	
        }
        return null;
    }
    
    protected static boolean isNumeric(String s) {  
        return s.trim().matches("[-+]?\\d*\\.?\\d+");  
    }  
    
    
}

