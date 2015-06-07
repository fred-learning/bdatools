package lab.paramcfg.backend.common;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class Config {
	public static String HDFS_REMOTE_URI;
	public static String SPARKLOG_REMOTE_PATH;
	public static String LOCAL_LOGS_PATH;
	public static String LOCAL_TEMP_PATH;
	public static String ES_SPARKLOG_PATH;
	public static String REST_PATH;
	public static String RRDSPATH;
	public static String[] NODENAMES;
	public static String DAG_PATH;
		
	private static String CONFIG_PATH = "/common_config.json";
	private static Logger logger = Logger.getLogger(Config.class);
	
	static {
		try {
			logger.info("Parse " + CONFIG_PATH);
			byte[] bytes = IOUtils.toByteArray(Config.class.getResourceAsStream(CONFIG_PATH));
			String text = new String(bytes, StandardCharsets.UTF_8);
			JSONObject jobj = new JSONObject(text);
			
//			HDFS_REMOTE_URI = jobj.getString("hdfs_remote_uri");
//			SPARKLOG_REMOTE_PATH = jobj.getString("sparklog_remote_path");
//			LOCAL_LOGS_PATH = jobj.getString("local_logs_path");
//			LOCAL_TEMP_PATH = jobj.getString("local_temp_path");
//			ES_SPARKLOG_PATH = jobj.getString("es_sparklog_path");
			REST_PATH = jobj.getString("rest_url");
			RRDSPATH = jobj.getString("rrds_path");
			NODENAMES = jobj.getString("nodenames").split(",");
			DAG_PATH = jobj.getString("dag_path");
		} catch (Exception e) {
			logger.fatal(CONFIG_PATH + " parse error", e);
			System.exit(-1);
		}
	}
	
//	public static Configuration getHadoopConf() {
//	  Configuration conf = new Configuration();
//	  conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
//	  conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
//	  return conf;
//	}
};
