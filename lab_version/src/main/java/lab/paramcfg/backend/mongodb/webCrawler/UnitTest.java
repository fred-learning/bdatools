package lab.paramcfg.backend.mongodb.webCrawler;

import lab.paramcfg.backend.mongodb.graph.GraphMatching;

public class UnitTest 
{
	public static void main( String[] args ){
		//String url = "http://pc57:18080/history/application_1429098422682_1367/jobs/";
		
		String ip_port = "pc57:18080";
		//String appId1 = "application_1433623322646_0019";
		//String appId1 = "application_1433623322646_0025";
		String appId1 = "application_1435046041249_0037";
		String appId2 = "application_1435046041249_0037";
		System.out.println("my test on dag");
		GraphMatching gMatching = new GraphMatching();
		WebCrawler crawler = new WebCrawler();
		DAG dag1 = crawler.getDAG(appId1);
		DAG dag2 = crawler.getDAG(appId2);
		
		if(null == dag1 || null == dag2){
			System.out.println("crawl dag failed.");
			return;
		}else{
			System.out.println("crawl dag success.");
		}
		
		double sim = 1- gMatching.similarity(dag1, dag2);
		System.out.println("similarity of job " + appId1 + " and " + appId2 +" is "+ sim);
		
	} 
}
