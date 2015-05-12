package lab.paramcfg.backend.mongodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JournalData implements Serializable {

	private String jobid;
	private String starttime;
	private String endtime;
	private HashMap<String, Integer> num_line;
	private double[] vector;

	private String[] partname = new String[] { "storage.DiskBlockManager",
			"storage.BlockManager", "storage.BlockManagerMaster",
			"storage.MemoryStore", "storage.ShuffleBlockFetcherIterator",
			"storage.BlockManagerSlaveActor", "broadcast.TorrentBroadcast",
			"rdd.HadoopRDD", "spark.CacheManager",
			"spark.MapOutputTrackerWorker", "spark.MapOutputTracker",
			"spark.SecurityManager", "executor.Executor",
			"executor.CoarseGrainedExecutorBackend",
			"shuffle.OneForOneBlockFetcher", "shuffle.RetryingBlockFetcher",
			"collection.ExternalAppendOnlyMap", "internal.ThreadLocalRandom",
			"client.RMProxy", "client.TransportResponseHandler",
			"client.TransportClientFactory", "Remoting",
			"yarn.ApplicationMaster", "yarn.YarnRMClient",
			"yarn.YarnAllocator", "yarn.ExecutorRunnable",
			"yarn.YarnAllocationHandler", "yarn.YarnRMClientImpl",
			"util.SparkUncaughtExceptionHandler",
			"channel.DefaultChannelPipeline", "server.TransportChannelHandler",
			"server.TransportRequestHandler" };

	/**
	 * init
	 * 
	 * @param id
	 */
	public JournalData(String id) {
		starttime = "99/99/99 99:99:99";
		endtime = "00/00/00 00:00:00";
		jobid = id;
		num_line = new HashMap<String, Integer>();
		vector = new double[partname.length];
		retrieveFromYarn();
	}
	
	public JournalData() {
		jobid = "-1";
	}

	/**
	 * 解析每一行log
	 * 
	 * @param context
	 */
	private void lineparser(String context) {
		if (context.equals(""))
			return;
		String regex = "^\\d{2}/\\d{2}/\\d{2}";
		Pattern pt = Pattern.compile(regex);
		Matcher matcher = pt.matcher(context);
		if (matcher.find()) {
			try {
				String[] part = context.split(" ");
				String timestamp = context.substring(0, 17);
				// String logtype = part[2];
				String component = part[3].substring(0, part[3].length() - 1);
				if (timestamp.compareTo(starttime) < 0) {
					setStarttime(timestamp);
				}
				if (timestamp.compareTo(endtime) > 0) {
					setEndtime(timestamp);
				}
				if (num_line.containsKey(component)) {
					int count = num_line.get(component);
					num_line.put(component, count + 1);
				} else {
					num_line.put(component, 1);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}
	/**
	 * 读取log存入hashmap和数组
	 */
	private void retrieveFromYarn() {
		String strCmd = "yarn logs -applicationId " + jobid;
		System.out.println(strCmd);
		Process process;
		try {
			String[] cmdarr = strCmd.split(" ");
			process = Runtime.getRuntime().exec(cmdarr);
			// process.waitFor();
			// 读取屏幕输出
			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = strCon.readLine()) != null) {
				lineparser(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(num_line);
		// compute the statistic array
		for (int i = 0; i < partname.length; i++) {
			if (num_line.containsKey(partname[i])) {
				vector[i] = num_line.get(partname[i]);
			}
		}
	}

	/**
	 * 具体实现计算两个向量的cos距离
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private double cosSimilarity(double[] p1, double[] p2) {
		double dotProduct = 0.0;
		double lengthSquaredp1 = 0.0;
		double lengthSquaredp2 = 0.0;
		for (int i = 0; i < p1.length; i++) {
			lengthSquaredp1 += p1[i] * p1[i];
			lengthSquaredp2 += p2[i] * p2[i];
			dotProduct += p1[i] * p2[i];
		}
		double denominator = Math.sqrt(lengthSquaredp1)
				* Math.sqrt(lengthSquaredp2);
        // correct for floating-point rounding errors
		if (denominator < dotProduct) {
			denominator = dotProduct;
		}
		// correct for zero-vector corner case
		if (denominator == 0 && dotProduct == 0) {
			return 0;
		}
		return dotProduct / denominator;
	}

	/**
	 * 计算两个向量的相似度
	 * 
	 * @param other
	 * @return
	 */
	public double similarity(JournalData other) {
		return cosSimilarity(vector, other.getVector());
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Map<String, Integer> getNum_line() {
		return num_line;
	}

	public void setNum_line(HashMap<String, Integer> num_line) {
		this.num_line = num_line;
	}

	public double[] getVector() {
		return vector;
	}

	public void setVector(double[] vector) {
		this.vector = vector;
	}

}
