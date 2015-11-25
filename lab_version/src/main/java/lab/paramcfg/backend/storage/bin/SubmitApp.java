package lab.paramcfg.backend.storage.bin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;

import org.apache.log4j.Logger;

import lab.paramcfg.backend.common.Config;
import lab.paramcfg.backend.common.Util;
import lab.paramcfg.backend.storage.graph.RDDSData;
import lab.paramcfg.backend.storage.journal.JournalData;
import lab.paramcfg.backend.storage.mongodb.DBInstance;
import lab.paramcfg.backend.storage.monitor.MonitoringData;
import lab.paramcfg.backend.storage.others.JobConfig;
import lab.paramcfg.backend.storage.others.JobResource;
import lab.paramcfg.backend.storage.others.ResCrawler;
import lab.paramcfg.backend.storage.others.YarnUtils;

public class SubmitApp {
  private static Logger logger = Logger.getLogger(SubmitApp.class);
  
  public static void main(String[] args) {
    // 提交命令
    logger.info("!!!please submit your app:");
    Scanner in = new Scanner(System.in);
    String cmd = in.nextLine();
    logger.info("!!!cmd is " + cmd);
    
    // 从页面上获取最新id
    String id = null;
    try {
      logger.info("Pull newest app id from " + Config.REST_URL);
      id = YarnUtils.pullNewestAppId();
    } catch (Exception e) {
      logger.fatal("Pull error:", e);
      return;
    }
    
    // 运行job
    ResCrawler crawler = new ResCrawler(id);
    logger.info("!!!Start running the job");
    try {
        crawler.startResCrawler();
        String[] command_arr = cmd.split("\\s+");
        Process runPro = Runtime.getRuntime().exec(command_arr);
        String line;
        BufferedReader in2 = new BufferedReader(new InputStreamReader(
                runPro.getInputStream()));
        while ((line = in2.readLine()) != null) {
            System.out.println(line);
        }
        runPro.waitFor();
        crawler.stopResCrawler();
        
        logger.info("!!!exitvalue:" + runPro.exitValue());
        if (runPro.exitValue() == 1) {
          logger.warn("!!!It seems some error occured in App. SubmitApp ended.");
          return;
        }
    } catch (Exception e) {
        logger.fatal(e);
        return;
    }
    logger.info("!!!Finish running the job");

    int status = YarnUtils.pullJobStatus(id);
    logger.info("!!!the job status:" + status);

    // 获取配置和资源
    JobConfig config = new JobConfig(cmd);
    JobResource resource = new JobResource(crawler.getMaxExePercent(), crawler.getMaxMemPercent());

    // 获取三类数据
    JournalData jData = new JournalData(id);
    Date startTime = Util.timechanger(jData.getStarttime());
    Date endTime = Util.timechanger(jData.getEndtime());
    MonitoringData mData = new MonitoringData(id, startTime.getTime() / 1000, endTime.getTime() / 1000);
    RDDSData rData = new RDDSData(id);

    // 初始化db,存储jobdata
    DBInstance db = new DBInstance(Config.MONGODB_CONFIG[0],
            Config.MONGODB_CONFIG[1], Config.MONGODB_CONFIG[2]);
    try {
        db.saveJobData(id, status, config, resource, startTime, endTime,
                jData, mData, rData);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        logger.fatal("Save data into mongodb error:", e);
        return;
    }
    
    logger.info("!!!Finish save the jobdata");
    db.close();
  }
}
