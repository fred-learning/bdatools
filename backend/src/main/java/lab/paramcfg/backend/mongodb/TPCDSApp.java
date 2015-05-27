package lab.paramcfg.backend.mongodb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lab.paramcfg.backend.common.TupleKeyComparable;
import lab.paramcfg.backend.common.Util;

public class TPCDSApp {
	public static void main(String[] args) throws Exception {
		BufferedReader strCon = new BufferedReader(new FileReader(
				"src/main/resources/availQuery.txt"));
		String context;
		ArrayList<String> querylist = new ArrayList<String>();
		while ((context = strCon.readLine()) != null) {
			querylist.add(context);
		}
		int[] numexecutorslist = { 32, 16, 8 };
		int[] executorcoreslist = { 2, 1 };
		String[] drivermemorylist = { "8g", "4g", "2g" };
		String[] execuormemorylist = { "4g", "2g", "1g" };
		String[] serializerlist = {
				"org.apache.spark.serializer.JavaSerializer",
				"org.apache.spark.serializer.KryoSerializer" };
		String[] datasetlist = { "default", "10", "100" };
		String cmd1 = "spark-submit --class bda.test.TPCDSCluster  --master yarn-client ";
		String cmd2 = " /home/spark/yateng/tpcds.jar hdfs:///yateng/dsdgen_";

		for (int numexecutor : numexecutorslist) {
			for (int executorcores : executorcoreslist) {
				for (String drivermemory : drivermemorylist) {
					for (String executormemory : execuormemorylist) {
						for (String serializer : serializerlist) {
							for (String dataset : datasetlist) {
								for (String query : querylist) {
									String cmd = cmd1
											+ "--conf \"spark.serializer="
											+ serializer
											+ "\" --num-executors "
											+ numexecutor
											+ " --executor-cores "
											+ executorcores
											+ " --driver-memory "
											+ drivermemory
											+ " --executor-memory "
											+ executormemory + cmd2 + dataset
											+ "/ \"" + query + "\"";
									System.out.println(cmd);

									// run the job
									System.out.println("Start running the job");
									Date startForSearch = new Date();
									Process runPro = null;
									try {
										String[] command_arr = cmd.split(" ");
										runPro = Runtime.getRuntime().exec(
												command_arr);
										String line;
										BufferedReader in2 = new BufferedReader(
												new InputStreamReader(runPro
														.getInputStream()));
										while ((line = in2.readLine()) != null) {
											System.out.println(line);
										}
										runPro.waitFor();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Date endForSearch = new Date();
									System.out
											.println("Finish running the job");
									// 获取jobdata
									String id = YarnUtils.pullNewestAppId(
											startForSearch.getTime(),
											endForSearch.getTime());
									// String id =
									// YarnUtils.pullNewestAppId(Long.valueOf("1429518915759"),Long.valueOf("1429518958334"));
									int status = YarnUtils.pullJobStatus(id);

									JobConfig config = new JobConfig(cmd);
									JobResource resource = new JobResource();
									System.out
											.println("now available mem:"
													+ resource
															.getAvailable_memory_in_megabyte()
													+ " vcores:"
													+ resource
															.getAvailable_vcores());

									JournalData jData = new JournalData(id);
									Date startTime = Util.timechanger(jData
											.getStarttime());
									Date endTime = Util.timechanger(jData
											.getEndtime());
									System.out
											.println("starttime:" + startTime);
									System.out.println("endtime:" + endTime);
									MonitoringData mData = new MonitoringData(
											id, startTime.getTime() / 1000,
											endTime.getTime() / 1000);
									RDDSData rData = new RDDSData(id);

									// 初始化db,存储jobdata
									DBInstance db = new DBInstance(
											"192.168.3.57", "test", "job");
									try {
										db.saveJobData(id, status, config,
												resource, startTime, endTime,
												jData, mData, rData);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									System.out
											.println("Finish save the jobdata");

									// 根据id从db获取jobdata
									JobData data = new JobData();
									try {
										data = db.getJobData(id);
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									System.out.println(data.getId()
											+ "always be saved");

									// 计算与其他所有的相似度
									int k = 10;
									List<TupleKeyComparable<Double, JobData>> sim_datas = data
											.computeAllSimi(db, k);

									System.out.println("Top " + k
											+ "similar datas:");
									for (TupleKeyComparable<Double, JobData> sim_data : sim_datas) {
										System.out
												.println(String.format(
														"id %s, sim %f",
														sim_data.y.getId(),
														sim_data.x));
									}

									db.close();
									System.out.println("Finish all tasks");
								}
							}
						}
					}
				}
			}
		}

	}
}