package lab.paramcfg.backend.mongodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import lab.paramcfg.backend.common.TupleKeyComparable;
import lab.paramcfg.backend.common.Util;

public class AnalysisApp {
	public static void main(String[] args) {
		// input the cmd
		System.out.println("please submit your app:");
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String cmd = in.nextLine();
		System.out.println("cmd is "+cmd);
		// run the job
		System.out.println("Start running the job");
		Date startForSearch = new Date();
		Process runPro = null;
		try {
			String[] command_arr = cmd.split(" ");
//			String[] command_test = {"sh","-c","\""+cmd+">/tmp/1111\""};
//			for (String testString : command_test) {
//				System.out.println(testString);
//			}
			runPro = Runtime.getRuntime().exec(command_arr);
			String line;
			BufferedReader in2 = new BufferedReader(new InputStreamReader(
					runPro.getInputStream()));
			while ((line = in2.readLine()) != null) {
				System.out.println(line);
			}
			runPro.waitFor();
			System.out.println("exitvalue:"+runPro.exitValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date endForSearch = new Date();
		System.out.println("Finish running the job");
		// 获取jobdata
		String id = YarnUtils.pullNewestAppId(startForSearch.getTime(),
				endForSearch.getTime());
//		String id = YarnUtils.pullNewestAppId(Long.valueOf("1429518915759"),Long.valueOf("1429518958334"));
		int status = YarnUtils.pullJobStatus(id);

		JobConfig config = new JobConfig(cmd);
		JobResource resource = new JobResource();
		System.out.println("now available mem:"
				+ resource.getAvailable_memory_in_megabyte() + " vcores:"
				+ resource.getAvailable_vcores());

		JournalData jData = new JournalData(id);
		Date startTime = Util.timechanger(jData.getStarttime());
		Date endTime = Util.timechanger(jData.getEndtime());
		System.out.println("starttime:"+startTime);
		System.out.println("endtime:"+endTime);
		MonitoringData mData = new MonitoringData(id, startTime.getTime()/1000,
				endTime.getTime()/1000);
		RDDSData rData = new RDDSData(id);

		// 初始化db,存储jobdata
		DBInstance db = new DBInstance("192.168.3.57", "test", "job");
		try {
			db.saveJobData(id, status, config, resource, startTime, endTime,
					jData, mData, rData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Finish save the jobdata");

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
		System.out.println(data.getId() + "always be saved");

		// 计算与其他所有的相似度
        int k = 10;
        List<TupleKeyComparable<Double, JobData> > sim_datas = data.computeAllSimi(db, k);

        System.out.println("Top " + k + "similar datas:");
        for (TupleKeyComparable<Double, JobData> sim_data : sim_datas) {
            System.out.println(String.format("id %s, sim %f", sim_data.y.getId(), sim_data.x));
        }

		db.close();
		System.out.println("Finish all tasks");
	}
}
