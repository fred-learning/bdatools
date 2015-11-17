package crawler.DAG;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import crawler.DAG.pojo.Job;

public abstract class JsonRequest<T> {
	protected static Logger logger = Logger.getLogger(JsonRequest.class);
	
	public T handle(String url) {
		try {
			HttpResponse<String> resp = Unirest.get(url).header("accept", "application/json").asString();
			if (resp.getStatus() != 200) {
				logger.fatal(String.format("Getting data from %s failed. Response status %d.", 
						url, resp.getStatus()));
				System.exit(-1);
			} else {
				String content = resp.getBody();
				return process(content);
			}
		} catch (UnirestException e) {
			logger.fatal("Error when getting data from " + url, e);
			System.exit(-1);
		}
		return null;
	}
	
	abstract T process(String json);
}
