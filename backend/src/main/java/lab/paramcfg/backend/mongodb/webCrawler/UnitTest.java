package lab.paramcfg.backend.mongodb.webCrawler;

import lab.paramcfg.backend.mongodb.graph.GraphMatching;

public class UnitTest 
{
	public static void main( String[] args ){
		//String url = "http://pc57:18080/history/application_1429098422682_1367/jobs/";
		
		String ip_port = "pc57:18080";
		String appId1 = "application_1429098422682_1367";
		String appId2 = "application_1429098422682_1368";
		
		GraphMatching gMatching = new GraphMatching();
		WebCrawler crawler = new WebCrawler();
		DAG dag1 = crawler.getDAG(ip_port, appId1);
		DAG dag2 = crawler.getDAG(ip_port, appId2);
		
		if(null == dag1 || null == dag2){
			System.out.println("crawl dag failed.");
			return;
		}else{
			System.out.println("crawl dag success.");
		}
		
		double sim = gMatching.similarity(dag1, dag2);
		System.out.println("similarity of job " + appId1 + " and " + appId2 +" is "+ sim);
		
	} 
}
