package lab.paramcfg.backend.storage.journal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class LogMap {
    private static Logger logger = Logger.getLogger(LogMap.class);
    
	public static HashMap<String, HashSet<String>> logmap;
	static{
		logmap = new HashMap<String, HashSet<String>>();
		try {
			InputStream is = LogMap.class.getResourceAsStream("/loglist");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String context;
			while ((context = reader.readLine()) != null&&!context.equals("")) {
				context = context.replaceAll("\\*", ".*");
				context = context.replaceAll("\\(", "\\\\(");
				context = context.replaceAll("\\)", "\\\\)");
				String key = context.split(":")[0];
				String value = context.split(":")[1];
				if (logmap.containsKey(key)) {
					HashSet<String> tmpset = logmap.get(key);
					tmpset.add(value);
				}else {
					HashSet<String> tmpset = new HashSet<String>();
					tmpset.add(value);
					logmap.put(key, tmpset);
				}
			}
		} catch (Exception e) {
		    logger.error("Error when parsing LogMap", e);
		}
	}
	
}
