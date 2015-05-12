package backend;

import java.util.Date;
import java.util.Scanner;

import junit.framework.Assert;
import lab.paramcfg.backend.common.Util;
import lab.paramcfg.backend.mongodb.DBInstance;
import lab.paramcfg.backend.mongodb.JobConfig;
import lab.paramcfg.backend.mongodb.JobData;
import lab.paramcfg.backend.mongodb.JobResource;
import lab.paramcfg.backend.mongodb.JournalData;
import lab.paramcfg.backend.mongodb.MonitoringData;
import lab.paramcfg.backend.mongodb.RDDSData;
import lab.paramcfg.backend.mongodb.YarnUtils;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class MongoTest {

	@Test
	// Test Mongodb serialize and deserialize
	public void testSave() throws Exception {
		
		//input the cmd
		System.out.println("please submit your app:");
		Scanner in = new Scanner(System.in);
		String cmd = in.next();
		
		//run the job
		System.out.println("Start running the job");
		Date startForSearch = new Date();
		Process runPro = Runtime.getRuntime().exec(cmd);
		runPro.waitFor();
		Date endForSearch = new Date();
		System.out.println("Finish running the job");
		
		//获取jobdata,(1429518915759,1429518958334)
		String id = YarnUtils.pullNewestAppId(startForSearch.getTime(),endForSearch.getTime());
		int status = YarnUtils.pullJobStatus(id);
		
		JobConfig config = new JobConfig(cmd);
		JobResource resource = new JobResource();
		System.out.println("now available mem:" + resource.getAvailable_memory_in_megabyte()
				+ " vcores:" + resource.getAvailable_vcores());

		JournalData jData = new JournalData(id);
		Date startTime = Util.timechanger(jData.getStarttime());
		Date endTime = Util.timechanger(jData.getEndtime());

		MonitoringData mData = new MonitoringData(id, startTime.getTime(),
				endTime.getTime());
		RDDSData rData = new RDDSData(id);

		//初始化db,存储jobdata
		DBInstance db = new DBInstance("192.168.3.57", "test", "job");
		db.clearCollection();
		db.saveJobData(id, status, config, resource, startTime, endTime, jData,
				mData, rData);

		System.out.println("Finish save the jobdata");
		
		//根据id从db获取jobdata
		JobData data = db.getJobData(id);
		System.out.println(data.getId()+"--"+data.getLimitedResource().getAvailable_memory_in_megabyte()+"--"+data.getJournalData().getNum_line()
				+"--"+data.getMonitoringData().getJobid()+"--"+data.getRddsData());
		
		//计算与其他所有的相似度
		
		
		Assert.assertNotNull(data);
		db.clearCollection();
		System.out.println("Finish all tasks");
	}

}
