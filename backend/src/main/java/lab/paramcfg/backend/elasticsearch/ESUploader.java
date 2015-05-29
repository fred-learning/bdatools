package lab.paramcfg.backend.elasticsearch;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import lab.paramcfg.backend.common.Config;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.logaggregation.AggregatedLogFormat.LogKey;
import org.apache.hadoop.yarn.logaggregation.AggregatedLogFormat.LogReader;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

public class ESUploader {
  private static Logger logger = Logger.getLogger(ESUploader.class);
  private static String LOG_PATTERN = "\\d{2}/\\d{2}/\\d{2} [012]\\d:[0-6]\\d:[0-6]\\d \\w+ .*";
  private FilterDB db;
  
  public ESUploader(FilterDB db) {
    this.db = db;
  }
  
  public void upload() {
    File localTempDir = new File(Config.LOCAL_TEMP_PATH);
    if (!localTempDir.exists()) localTempDir.mkdir();
    
    List<SparkJobLogFile> files = db.getUnfinishedSparkLogFile();
    for (SparkJobLogFile f : files) {
      logger.info("Upload " + f.getLocalPath());
      try {
        uploadLogFile(f);
        logger.info("Upload finished.");
      } catch (Exception e) {
        logger.error("Error in uploading file:", e);
      }
    }
  }
  
  private void uploadLogFile(SparkJobLogFile jFile) 
      throws Exception {
    if (jFile.getNextLine() == Integer.MAX_VALUE || jFile.isFinish()) { 
      return;
    } else {
      String localPath = jFile.getLocalPath();
      String remotePath = jFile.getRemotePath();
      // if file not exist in local dist, copy to local
      File localFile = new File(localPath);
      if (!localFile.exists()) {
        logger.warn(String.format(
            "File not exist. Copy [%s] to [%s].", remotePath, localPath));
        FileSystem hdfs = FileSystem.get(
            new URI(Config.HDFS_REMOTE_URI), Config.getHadoopConf());
        hdfs.copyToLocalFile(new Path(remotePath), new Path(localPath));
        localFile = new File(localPath);
      }
      
      // parse input file and convert it into temp file
      String in_path_str = "file:///" 
          + System.getProperty("user.dir") + "/" + localPath;
      String out_path_str = Config.LOCAL_TEMP_PATH + "/" + UUID.randomUUID();
      File tempFile = new File(out_path_str);
      PrintStream out_ps = new PrintStream(tempFile);
      Path in_path = new Path(in_path_str);
      LogReader reader = new LogReader(new Configuration(), in_path);

      boolean isSuccess = true;
      try {
        DataInputStream valueStream = reader.next(new LogKey());
        LogReader.readAContainerLogsForALogType(valueStream, out_ps);
      } catch (Exception e) {
        logger.warn(String.format(
            "Invalid log file Remote:[%s]", jFile.getRemotePath()),
            e);
        db.setLogFinish(jFile.getLocalPath());
        isSuccess = false;
      } finally {
        out_ps.close();
        reader.close();
        if (isSuccess) {
          // reading temp file
          upLoadESQueryFromFile(out_path_str, jFile);
        }
        
        // clear temp and origin file
        if (tempFile.exists()) tempFile.delete();
        if (localFile.exists()) {
          logger.info(String.format(
              "Delete Local[%s].", jFile.getLocalPath()));
          localFile.delete();
        }
      }
    }
  }
  
  
  private void upLoadESQueryFromFile(
      String tempFilePath, SparkJobLogFile jFile) 
          throws IOException, InterruptedException {
    FileReader fr = new FileReader(tempFilePath);
    BufferedReader br = new BufferedReader(fr);
    for (int i = 0; i < jFile.getNextLine(); ++ i) {
      br.readLine();
    }

    final int BATCH_SIZE = 100;
    int nowCount = jFile.getNextLine();
    String date = null, logType = null, logContent = null, next = null;
    Pattern pattern = Pattern.compile(LOG_PATTERN);
    CloseableHttpClient client = HttpClients.createDefault();
    List<String> batchQueryBuffer = new ArrayList<String>();
    boolean suc = true;
    while ((next = br.readLine()) != null) {
      if (pattern.matcher(next).matches()) {
        final int DATE_SPACE_POS = 17;
        date = next.substring(0, DATE_SPACE_POS);
        final int TYPE_SPACE_POS = next.substring(DATE_SPACE_POS + 1).indexOf(" ");
        logType = next.substring(DATE_SPACE_POS + 1, DATE_SPACE_POS + 1 + TYPE_SPACE_POS);
        logContent = next.substring(DATE_SPACE_POS + 1 +TYPE_SPACE_POS + 1);
      } else {
        //lastDate = null;
        //logType = "Console";
        //logContent = next;
        continue;
      }
      
      String query = formatQuery(date, logType, jFile.getRemotePath(), logContent);
      batchQueryBuffer.add(query);
      if (batchQueryBuffer.size() >= BATCH_SIZE) {
        suc = batchInsertWithRetry(client, batchQueryBuffer, 3);
        if (suc) {
          nowCount += BATCH_SIZE;
          db.updateLog(jFile.getLocalPath(), nowCount);
          batchQueryBuffer.clear();
        } else {
          break;
        }
      }
    }
    
    if (suc && batchQueryBuffer.size() != 0) {
      suc = batchInsertWithRetry(client, batchQueryBuffer, 3);
    }
    
    if (suc) db.setLogFinish(jFile.getLocalPath());
    
    br.close();
  }
 
  private String formatQuery(String date, String type, String path, String content) {
    return String.format(
        "{\"date\" : \"%s\", \"logType\" : \"%s\", "
        + "\"storagePath\" : \"%s\", \"content\" : \"%s\"}",
        date, type, path, content);
  }
  
  private boolean batchInsertWithRetry(
      CloseableHttpClient client, List<String> formatQuries, int retry_times) 
          throws InterruptedException {
    final int SLEEP_TIME = 5000;
    while (retry_times-- > 0 && !batchInsert(client, formatQuries)) {
      logger.error("Batch insert failed, retry later.");
      Thread.sleep(SLEEP_TIME);
    }
    if (retry_times < 0) return false;
    else return true;
  }
  
  private boolean batchInsert(
      CloseableHttpClient client, List<String> formatQuries) {
    StringBuilder sb = new StringBuilder();
    for (String s: formatQuries) {
      String msg = String.format("{ \"create\" : {} }\n%s\n", s);
      sb.append(msg);
    }
    
    try {
      HttpPost httpPost = new HttpPost(Config.ES_SPARKLOG_PATH + "/_bulk");
      httpPost.setEntity(new StringEntity(sb.toString()));
      CloseableHttpResponse response = client.execute(httpPost);
      response.close();
      return true;
    } catch (Exception e) {
      logger.error("Upload batch query  error:", e);
      return false;
    }
  }
  
  @SuppressWarnings("unused")
@Deprecated
  private boolean uploadSingleQuery(
      CloseableHttpClient client, String query) {
    HttpPost httpPost = new HttpPost(Config.ES_SPARKLOG_PATH);
    try {
      httpPost.setEntity(new StringEntity(query));
      CloseableHttpResponse response = client.execute(httpPost);
      HttpEntity entity = response.getEntity();
      StringWriter writer = new StringWriter();
      IOUtils.copy(entity.getContent(), writer);
      System.out.println(writer.toString());
      return true;
    } catch (Exception e) {
      logger.error(String.format(
          "Upload single query [%s] error:", query), e);
      return false;
    }
  }
}
