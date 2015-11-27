package common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {
	private static Logger log = Logger.getLogger(Config.class);
	
	private static Config conf;
	private Properties P;
	
	public static synchronized Config getInstance() {
		if (conf == null) {
			conf = new Config();
		}
		return conf;
	}
	
	private Config() {
		try {
			InputStream stream = this.getClass().getResourceAsStream("/settings.properties");
			P = new Properties();
			P.load(stream);
		} catch (IOException e) {
			log.fatal("Error when reading settings.properties:\n", e);
			System.exit(-1);
		}
	}
	
	public String getValue(String key) {
		return P.getProperty(key);
	}

	public String getClusterID() { return P.getProperty("clusterID"); }
	
	public String getHistServerPath() {
		return P.getProperty("historyServerPath");
	}

	public String getYarnPath() {
		return P.getProperty("yarnPath");
	}

	public String getMongoIP() { return P.getProperty("mongoIP"); }

    public String getMongoPort() { return P.getProperty("mongoPort"); }

    public String getMongoDBName() { return P.getProperty("mongoDBName"); }

    public String getMongoHistoryCollection() {
		return P.getProperty("mongoHistoryCollection");
	}

	public String getMongoRecommendProgressCollection() {
		return P.getProperty("mongoProgressCollection");
	}

	public List<String> getSparkRecommendParams() {
		String recommendStr = P.getProperty("recommendParams");
		return Arrays.asList(recommendStr.split(","));
	}

	public Integer getJettyPort() {
		return Integer.parseInt(P.getProperty("jettyPort"));
	}

	public Integer getRecommendServiceNum() {
		return Integer.parseInt(P.getProperty("recommendServiceThread"));
	}

	public Integer getHistorySyncIntevalSec() {
		return Integer.parseInt(P.getProperty("historySyncInterval"));
	}
}
