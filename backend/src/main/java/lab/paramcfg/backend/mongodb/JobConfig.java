package lab.paramcfg.backend.mongodb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JobConfig implements Serializable {
	private String configcmd;
	private HashMap<String, String> configMap;

	public JobConfig() {
		setConfigMap(new HashMap<String, String>());
	}

	public JobConfig(String cmd) {
		setConfigcmd(cmd);
	}

	private void update(String key, String value) {
		getConfigMap().put(key, value);
	}

	public String getConfigcmd() {
		return configcmd;
	}

	public void setConfigcmd(String configcmd) {
		this.configcmd = configcmd;
	}

	public HashMap<String, String> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(HashMap<String, String> configMap) {
		this.configMap = configMap;
	}

}
