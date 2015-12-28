package crawler.env;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Env {
	private Map<String, String> M;

	public Env() {
        M = new HashMap<String, String>();
    }

    public void put(String k, String v) {
        M.put(k, v);
    }

    public String get(String k) {
        return M.get(k);
    }

    @Override
    public String toString() {
        return M.toString();
    }
}
