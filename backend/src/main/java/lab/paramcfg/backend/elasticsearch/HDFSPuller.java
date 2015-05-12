package lab.paramcfg.backend.elasticsearch;

import java.io.File;
import java.net.URI;

import lab.paramcfg.backend.common.Config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

public class HDFSPuller {
  private static Logger logger = Logger.getLogger(HDFSPuller.class);
  private FilterDB db;
  
  public HDFSPuller(FilterDB db) {
    this.db = db;
    
    File upperDir = new File(Config.LOCAL_LOGS_PATH);
    if (!upperDir.exists()) {
      logger.info(String.format(
          "Create directory %s.", upperDir.getAbsolutePath()));
      upperDir.mkdir();
    }
  }
  
  /*
   * Pull HDFS files to local disk.
   * If file f were pulled before, es_migration.leveldb[f] isn't empty.
   * Depending on the f status, the value return by leveldb will be different.
   * If it was downloaded but not uploaded, it would be UPLOAD:0.
   * If it was uploading but not finished, it would be UPLOAD:int_val. int_val is nextline need to be upload.
   * If it was uploaded, it would be UPLOAD:Integer.MAX.
   */
  public boolean pullSparkLogToLocal() {
    try {
      FileSystem hdfs = FileSystem.get(
          new URI(Config.HDFS_REMOTE_URI), Config.getHadoopConf());
        
      for (FileStatus jobStatus : hdfs.listStatus(new Path(Config.SPARKLOG_REMOTE_PATH))) {
        String jobId = jobStatus.getPath().getName();
        if (jobId.startsWith(".")  || jobId.startsWith("_") || jobStatus.isFile()
              || db.jobVisited(jobId)) { 
          continue;
        }
            
        for (FileStatus hostLogStatus : hdfs.listStatus(jobStatus.getPath())) {
          if (!hostLogStatus.isFile()) continue;
          Path remotePath = hostLogStatus.getPath();
          SparkJobLogFile jFile = new SparkJobLogFile(remotePath);
                
          if (!db.logVisisted(jFile.getLocalPath())) {
            logger.info(String.format("Copy [%s] to local %s", 
                remotePath, jFile.getLocalPath()));
            hdfs.copyToLocalFile(remotePath, new Path(jFile.getLocalPath()));
            db.setLogInit(jFile.getLocalPath());
          } 
        }
            
        db.setJobVisited(jobId);
      }
        
      // delete .crc file
      logger.info("Delete local crc file.");
      File dir = new File(String.format("%s", Config.LOCAL_LOGS_PATH));
      for (File f : dir.listFiles()) {
        if (f.getName().endsWith(".crc"))
            f.delete();
      }
      
      return true;
    } catch (Exception e) {
      String msg = String.format("Could not download file from %s",
                                 Config.HDFS_REMOTE_URI + Config.SPARKLOG_REMOTE_PATH);
      logger.error(msg, e);
      return false;
    }
    
  }
  
}
