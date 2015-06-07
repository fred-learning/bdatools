package lab.paramcfg.backend.mongodb.webCrawler;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;  

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
	
    public DAG getDAG(String ip_port, String appId)
    {  	
    	ArrayList<LinkedList<Integer>> graph ;
        ArrayList<Nodem> nodems = new ArrayList<Nodem>();
    	Hashtable<String, Integer> maps = new Hashtable<String, Integer>();
    	/* used to crawl one page */
    	String url = "http://" + ip_port + "/history/" + appId + "/jobs/";
    	String content = getContentFromUrl(url);
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
        		content = getContentFromUrl(url);//.replaceAll("\n", "");
        		if(null == content){
            		return null;
            	}
        		/* preprocess content from page to get dag graph data */
        		pattern = Pattern.compile(dagDataReg,Pattern.MULTILINE|Pattern.DOTALL);
        		matcher = pattern.matcher(content);
        		String finalStr = "";
        		if(matcher.find()){	
        			finalStr = matcher.group(0);
        			finalStr = finalStr.replaceAll("&quot;", "\"");
        			finalStr = finalStr.replaceAll("&gt;", ">");
        			finalStr = finalStr.replaceAll("</div><div class=\"stage-metadata\" stageId=\"stage_(\\d+)\" style=\"display:none\">", "");
        			finalStr = finalStr.replaceAll("<div class=\"dot-file\">", "");
        		}
        		
        		/* get the graph data string */
        		String graphsStr = finalStr.replaceAll("\\}</div>[ \n]*digraph G \\{", "").replaceAll("</div>", "");

        		/* parse the graph data string to graphViz's graph */
        		StringReader reader = new StringReader(graphsStr);
        		Parser p = null;
        		try {
                    p = new Parser();
                    p.parse(reader);
                } catch (Exception e) {
                	e.printStackTrace();
                }
                ArrayList<Graph> gl = p.getGraphs();
                System.out.println(gl.get(0).getNodes(false).get(0).getId());
                ArrayList<Node> nodes = gl.get(0).getNodes(false);
                int counter = 0;
                
                /* add the nodes id to nodems */
                for(Node n: nodes) {
                	String id = n.getId().getId().trim();
                	String label = n.getAttribute("label").trim();
                	if(isNumeric(id)){
                		nodems.add(new Nodem(id, label));
                		maps.put(id, counter++);
                	}
                }
                
                /* add the adjacent nodes' id */
                graph = new ArrayList<LinkedList<Integer>>(nodes.size());
                for(int i=0; i<nodes.size(); i++){
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

