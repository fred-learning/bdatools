package crawler.request;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import crawler.DAG.pojo.Job;

public abstract class Request<T> {
	protected static Logger logger = Logger.getLogger(Request.class);
	private String acceptType;
	
	public Request() {
		acceptType = "application/json";
	}
	
	public Request(String type) {
		acceptType = type;
	}
	
	public T handle(String url) throws UnirestException {
		logger.debug("Handling html request: " + url);

		HttpResponse<String> resp = Unirest.get(url).header("accept", acceptType).asString();
		if (resp.getStatus() != 200) {
			logger.error(String.format("Getting data from %s failed. Response status %d.",
					url, resp.getStatus()));
			return null;
		} else {
			String content = resp.getBody();
			return process(content);
		}
	}
	
	abstract T process(String content);
}
