package lab.paramcfg.backend.storage.bin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import lab.paramcfg.backend.common.Config;
import lab.paramcfg.backend.common.TupleKeyComparable;
import lab.paramcfg.backend.common.Util;
import lab.paramcfg.backend.storage.JobData;
import lab.paramcfg.backend.storage.graph.RDDSData;
import lab.paramcfg.backend.storage.journal.JournalData;
import lab.paramcfg.backend.storage.mongodb.DBInstance;
import lab.paramcfg.backend.storage.monitor.MonitoringData;
import lab.paramcfg.backend.storage.others.JobConfig;
import lab.paramcfg.backend.storage.others.JobResource;
import lab.paramcfg.backend.storage.others.YarnUtils;

public class AnalysisApp {
	public static void main(String[] args) {
//		// 输出配置
////		Config.testOutput();
//
//		// 提交命令
//		System.out.println("!!!please submit your app:");
//		Scanner in = new Scanner(System.in);
//		String cmd = in.nextLine();
//		System.out.println("!!!cmd is " + cmd);
//
//		// 运行job
//		System.out.println("!!!Start running the job");
//		Date startForSearch = new Date();
//		Process runPro = null;
//		try {
//			String[] command_arr = cmd.split("\\s+");
//			// String[] command_arr =
//			// {"sh","-c","\""+cmd+">/tmp/backend.output\""};
//			runPro = Runtime.getRuntime().exec(command_arr);
//			String line;
//			BufferedReader in2 = new BufferedReader(new InputStreamReader(
//					runPro.getInputStream()));
//			while ((line = in2.readLine()) != null) {
//				System.out.println(line);
//			}
//			runPro.waitFor();
//			System.out.println("!!!exitvalue:" + runPro.exitValue());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Date endForSearch = new Date();
//		System.out.println("!!!Finish running the job");
//
//		// 获取jobid和status
//		// System.out.println("starttime:"+startForSearch.getTime());
//		String id = YarnUtils.pullNewestAppId(startForSearch.getTime(),
//				endForSearch.getTime());
//		// String id =
//		// YarnUtils.pullNewestAppId(Long.valueOf("1429518915759"),Long.valueOf("1429518958334"));
//		int status = YarnUtils.pullJobStatus(id);
//		System.out.println("!!!the job status:" + status);
//
//		// 获取配置和资源
//		JobConfig config = new JobConfig(cmd);
//		JobResource resource = new JobResource();
//		// System.out.println("now available mem:"
//		// + resource.getAvailable_memory_in_megabyte() + " vcores:"
//		// + resource.getAvailable_vcores());
//		System.out.println("!!!now available mem:"
//				+ resource.getAvailable_memory_in_megabyte());
//
//		// 获取三类数据
//		JournalData jData = new JournalData(id);
//		Date startTime = Util.timechanger(jData.getStarttime());
//		Date endTime = Util.timechanger(jData.getEndtime());
//		// System.out.println("starttime:"+startTime);
//		// System.out.println("endtime:"+endTime);
//		MonitoringData mData = new MonitoringData(id,
//				startTime.getTime() / 1000, endTime.getTime() / 1000);
//		RDDSData rData = new RDDSData(id);
//
//		// 初始化db,存储jobdata
//		DBInstance db = new DBInstance(Config.MONGODB_CONFIG[0],
//				Config.MONGODB_CONFIG[1], Config.MONGODB_CONFIG[2]);
//		try {
//			db.saveJobData(id, status, config, resource, startTime, endTime,
//					jData, mData, rData);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("!!!Finish save the jobdata");
//
//		// 根据id从db获取jobdata
//		JobData data = new JobData();
//		try {
//			data = db.getJobData(id);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(data.getId() + " saved");
//
//		// 计算与其他所有的相似度
//		long minitime = Long.MAX_VALUE;
//		String bestconfig = "";
//
//		int k = 10;
//		List<TupleKeyComparable<Double, JobData>> sim_datas = data
//				.computeAllSimi(db, k);
//
//		System.out.println("!!!Top " + k + "similar datas:");
//		for (TupleKeyComparable<Double, JobData> sim_data : sim_datas) {
//			long tmptime = sim_data.y.getEndTime().getTime()
//					- sim_data.y.getStartTime().getTime();
//			System.out.println(String.format("id %s, sim %f time %d",
//					sim_data.y.getId(), sim_data.x, tmptime));
//			if (tmptime < minitime) {
//				bestconfig = sim_data.y.getConfig().getConfigcmd();
//			}
//		}
//		
//		in.close();
//		db.close();
//
//		// 返回给前端
////		System.out.println("!!!return the config to front");
////		String param = HttpPost.configToJson(bestconfig);
////		String reString = HttpPost.sendPost(Config.OP_FRONT_IP,
////				"confirm_data_json=" + param);
////		System.out.println("!!!the best config is " + param);
////		System.out.println("!!!get return:" + reString);
//		System.out.println("!!!Finish all tasks");
	}
}
