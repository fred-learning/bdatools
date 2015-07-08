package lab.paramcfg.backend.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class Config {
	public static String REST_URL;
	public static String DAG_PATH;
	public static String RRDS_PATH;
	public static String[] NODENAMES;
	public static String[] MONGODB_CONFIG;
	
//	public static String OP_SSH_PATH;
//	public static String OP_CLUSTER_ID;
//	public static String OP_MASTER_IP;
//	public static String OP_FRONT_IP;
//	
//	public static String OP_REST_URL;
//	public static String OP_DAG_PATH;
//	public static String OP_RRDS_PATH;
//	public static String[] OP_NODENAMES;
//	public static String[] OP_MONGODB_CONFIG;
		
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
			
//			parseHosts();
//			OP_FRONT_IP = jobj.getString("op_front_ip");
//			OP_RRDS_PATH = jobj.getString("op_rrds_path");
//			OP_SSH_PATH = jobj.getString("op_ssh_path");
//			OP_MONGODB_CONFIG = jobj.getString("op_mongodb_config").split(",");
		} catch (Exception e) {
			logger.fatal(CONFIG_PATH + " parse error", e);
			System.exit(-1);
		}
	}
//	private static void parseHosts() {
//		try {
//			ArrayList<String> iplist = new ArrayList<String>();
//			FileReader fr = new FileReader("/etc/hosts");
//			BufferedReader br = new BufferedReader(fr);
//			String context = null;
//			while ((context=br.readLine())!=null) {
//				if (context.startsWith("192.168")) {
//					String ip = context.split("\\s+")[0];
//					String name = context.split("\\s+")[1];
//					OP_CLUSTER_ID = name.split("hadoop")[0];
//					if (name.split("hadoop")[1].startsWith("master")) {
//						OP_MASTER_IP = ip;
//					}
//					iplist.add(ip);
//				}
//			}
//			OP_NODENAMES = new String[iplist.size()];
//			OP_NODENAMES = iplist.toArray(OP_NODENAMES);
//			OP_REST_URL = OP_MASTER_IP+":8088";
//			OP_DAG_PATH = OP_MASTER_IP+":18080";
////			System.out.println("master ip is "+OP_MASTER_IP);
////			System.out.println("cluster id is "+OP_CLUSTER_ID);
////			for (String tmpname : OP_NODENAMES) {
////				System.out.println(tmpname);
////			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//	}
//	public static void testOutput() {
//		System.out.println("OP_SSH_PATH:"+OP_SSH_PATH);
//		System.out.println("OP_CLUSTER_ID:"+OP_CLUSTER_ID);
//		System.out.println("OP_MASTER_IP:"+OP_MASTER_IP);
//		System.out.println("OP_REST_URL:"+OP_REST_URL);
//		System.out.println("OP_FRONT_IP:"+OP_FRONT_IP);
//		System.out.println("OP_DAG_PATH:"+OP_DAG_PATH);
//		System.out.println("OP_RRDS_PATH:"+OP_RRDS_PATH);
//		System.out.println("OP_NODENAMES:"+OP_NODENAMES.length);
//		System.out.println("OP_MONGODB_CONFIG:"+OP_MONGODB_CONFIG.length);
//		
//	}
	
//	public static void configByHand() {
//		@SuppressWarnings("resource")
//		Scanner in = new Scanner(System.in);
//		System.out.println("please set rest_url(e.g. http://192.168.3.57:8088):");
//		String resturl = in.nextLine();
//		System.out.println(resturl);
//		Config.REST_URL = resturl;
//		System.out.println("please set dag_path(e.g. http://192.168.3.57:18080):");
//		String dagpath = in.nextLine();
//		System.out.println(dagpath);
//		Config.DAG_PATH = dagpath;
//		System.out.println("please set rrds_path(e.g. /home/spark/wangjue/software/ganglia-3.6.0/rrds/apache-cluster/):");
//		String rrdspath = in.nextLine();
//		System.out.println(rrdspath);
//		Config.RRDS_PATH = rrdspath;
//		System.out.println("please set nodenames(e.g. pc57,pc59,pc61,pc63):");
//		String nodenames = in.nextLine();
//		System.out.println(nodenames);
//		Config.NODENAMES = nodenames.split(",");
//	}
	
};