package common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
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

	public void loadOrDie(String filePath) {
		assert P != null;
		try {
			P.load(new FileInputStream(filePath));
		} catch (IOException e) {
			log.fatal(String.format("load config from %s failed.", filePath));
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

	public List<String> getSparkRecommendParams() {
		String recommendStr = P.getProperty("recommendParams");
		return Arrays.asList(recommendStr.split(","));
	}

	// mongo db
	public String getMongoIP() { return P.getProperty("mongoIP"); }

    public String getMongoPort() { return P.getProperty("mongoPort"); }

    public String getMongoDBName() { return P.getProperty("mongoDBName"); }

    public String getMongoHistoryCollection() {
		return P.getProperty("mongoHistoryCollection");
	}

	public String getMongoRecommendProgressCollection() {
		return P.getProperty("mongoProgressCollection");
	}

	public Integer getMongoNumItemsPerPage() {
		return Integer.parseInt(P.getProperty("mongoNumItemsPerPage"));
	}

	// jetty
	public Integer getJettyPort() {
		return Integer.parseInt(P.getProperty("jettyPort"));
	}

	public String getJettyIP() {
		return P.getProperty("jettyIP");
	}

	// recommend service
	public String getRecommendProgressViewServletPath() {
		return P.getProperty("recommendProgressViewServletPath");
	}

	public String getRecommendProgressDetailServletPath() {
		return P.getProperty("recommendProgressDetailServletPath");
	}

	public String getRecommendJobSubmitServletPath() {
		return P.getProperty("recommendJobSubmitServletPath");
	}

	public Integer getRecommendServiceNum() {
		return Integer.parseInt(P.getProperty("recommendServiceThread"));
	}

	// run param service
	public String getRunParamSubmitServletPath() {
		return P.getProperty("runParamSubmitServletPath");
	}

	public String getRunParamHistoryServletPath() {
		return P.getProperty("runParamHistoryServletPath");
	}

	public String getRunParamLogServletPath() {
		return P.getProperty("runParamLogServletPath");
	}

	public String getSSHIP() {
		return P.getProperty("sshIP");
	}

	public String getSSHUser() {
		return P.getProperty("sshUser");
	}

	public String getSSHOutputDir() {
		return P.getProperty("sshOutputDir");
	}

	public Integer getSSHConcurrency() {
		return Integer.parseInt(P.getProperty("sshConcurrency"));
	}

	// history DB
	public String getHistoryDBAddServletPath() {
		return P.getProperty("historyDBAddServletPath");
	}

	public String getHistoryDBDeleteServletPath() {
		return P.getProperty("historyDBDeleteServletPath");
	}

	public String getHistoryDBViewServletPath() {
		return P.getProperty("historyDBViewServletPath");
	}
}
