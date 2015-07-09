package lab.paramcfg.backend.common;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * 解析resources/common_config.json文件中的配置参数。 
 */
public class Config {
	public static String REST_URL; // YARN REST API
	public static String DAG_PATH; // SPARK DAG API
	public static String RRDS_PATH; // GANGLIA RRD data storage path
	public static String[] NODENAMES; // cluster nodes hostname
	public static String[] MONGODB_CONFIG; // mongodb configuration
		
	private static String CONFIG_PATH = "/common_config.json";
	private static Logger logger = Logger.getLogger(Config.class);
	
	static {
		try {
			logger.info("Parse " + CONFIG_PATH);
			byte[] bytes = IOUtils.toByteArray(Config.class.getResourceAsStream(CONFIG_PATH));
			String text = new String(bytes, StandardCharsets.UTF_8);
			JSONObject jobj = new JSONObject(text);
			
			REST_URL = jobj.getString("rest_url");
			RRDS_PATH = jobj.getString("rrds_path");
			NODENAMES = jobj.getString("nodenames").split(",");
			DAG_PATH = jobj.getString("dag_path");
			MONGODB_CONFIG = jobj.getString("mongodb_config").split(",");
			
		} catch (Exception e) {
			logger.fatal(CONFIG_PATH + " parse error", e);
			System.exit(-1);
		}
	}	
};
