package lab.paramcfg.backend.storage.others;

import java.io.Serializable;
import java.util.HashMap;

public class JobConfig {
    private String configcmd;
    private HashMap<String, String> configMap;

	public JobConfig() {
		configMap = new HashMap<String, String>();
	}

	public JobConfig(String cmd) {
		configcmd = cmd;
	}

	@SuppressWarnings("unused")
	private void update(String key, String value) {
		getConfigMap().put(key, value);
	}

	public String getConfigcmd() {
		return configcmd;
	}

	public HashMap<String, String> getConfigMap() {
		return configMap;
	}

}
