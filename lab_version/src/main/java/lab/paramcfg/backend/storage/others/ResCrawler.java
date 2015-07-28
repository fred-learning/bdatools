package lab.paramcfg.backend.storage.others;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lab.paramcfg.backend.common.Config;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

enum State { RUNNING, STOPPED };

public class ResCrawler implements Runnable {
	private static Logger logger = Logger.getLogger(ResCrawler.class);
  
	// state of crawler
	private State state = State.STOPPED;
	
	// max executor resource used percent
	private double maxExePercent = 0;
	
	// max total memory used percent
    private double maxMemPercent = 0;
    
    // url of this crawler
    private String url = null;
    
    public ResCrawler (String appid) {
      this.url = String.format("http://%s/proxy/%s/executors/", Config.REST_URL, appid);
  }

    // crawl resource used with one second interval
	public boolean startResCrawler() {
		Thread thread = new Thread(this);
		thread.start();
		return true; 
	}
	
	public boolean stopResCrawler() {
		state = State.STOPPED;
		return true;
	}
	
	public void run() {
		int tryTimes = 0;
		int MAX_TRY = 10;
		
		state = State.RUNNING;
		while(state==State.RUNNING) {
			Document doc = null;
			// crawl content of resources
	    	try {
	    		doc=Jsoup.connect(url).get();
	    		processContent(doc);
	    		logger.info(String.format("Resource using: (Mem: %f, Exe: %f)", 
	    		    this.getMaxMemPercent(), this.getMaxExePercent()));
	    		tryTimes = 0;
			} catch (Exception e) {
				logger.error(e);
				
				if (++ tryTimes > MAX_TRY) break;
			}
	    	
            //wait for one second
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                logger.error(e1);
            }
		}
	}
	
	private synchronized Boolean processContent(Document doc) throws Exception {
		double memTotal = 0;
		double memUsed = 0;
		double exeTotal = 0;
		double exeUsed = 0;
		
		// Extract mem info
		String memReg = "Memory: (.+) Used \\((.+) Total\\)";
		String memUsedStr = doc.select(".unstyled li").get(0).text();
	    Matcher memMatcher = Pattern.compile(memReg).matcher(memUsedStr);
	    
	    // If mem info not exsit return false
	    if (!memMatcher.matches() || memMatcher.groupCount()<2) return false;
	    
	    memUsed = this.parse(memMatcher.group(1));
		memTotal = this.parse(memMatcher.group(2));
		
		Elements exeUsedElements = doc.select(".table tbody").get(0).children();
		Iterator<Element> exeUsedIter = exeUsedElements.iterator();
		while(exeUsedIter.hasNext()){
			Element element = exeUsedIter.next();
			String tasks = element.getAllElements().get(6).text();
			if (0 != Integer.parseInt(tasks)) {
				exeUsed += 1.0;
			}
		}
		exeTotal = exeUsedElements.size();
		exeUsed = exeUsed + 1; // consider driver

		this.maxMemPercent = Math.max(this.maxMemPercent, Math.max(memUsed, 1) / memTotal);
		this.maxExePercent = Math.max(this.maxExePercent, exeUsed / exeTotal);
		
		return true;
	}

	private double parse(String arg0) {
		String str = arg0.trim();
		long KB_FACTOR = 1024;
		long MB_FACTOR = 1024 * KB_FACTOR;
		long GB_FACTOR = 1024 * MB_FACTOR;

	    int spaceNdx = str.indexOf(" ");
	    double ret = Double.parseDouble(str.substring(0, spaceNdx));
	    
	    String s = str.substring(spaceNdx + 1);
	    if (s.equals("GB")) return ret * GB_FACTOR;
	    else if (s.equals("MB")) return ret * MB_FACTOR;
	    else if (s.equals("KB")) return ret * KB_FACTOR;
	    else if (s.equals("B")) return ret;
	    else return -1;

	}

	public double getMaxExePercent() {
		return maxExePercent;
	}

    public State getState() {
		return state;
	}

	public double getMaxMemPercent() {
		return maxMemPercent;
	}


	public static void main(String[] args) {
	  String appid = "application_1437031904347_0031";
	  ResCrawler crawler = new ResCrawler(appid);
	  crawler.startResCrawler();
	}
	
}
