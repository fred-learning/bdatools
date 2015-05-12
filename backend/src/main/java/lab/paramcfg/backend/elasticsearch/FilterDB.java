package lab.paramcfg.backend.elasticsearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

public class FilterDB {
  private static Logger logger = Logger.getLogger(FilterDB.class);
  private static String STORAGE_PATH = "sparklog.leveldb";
  private DB db;
  
  public FilterDB() {
    Options options = new Options();
    options.createIfMissing(true);
    File file = new File("sparklog.leveldb");
      
    try {
      logger.info(String.format("Open ES migration DB[%s].", STORAGE_PATH));
      db = Iq80DBFactory.factory.open(file, options);
    } catch (IOException e) {
      logger.fatal("Could not open ES migration DB.", e);
      System.exit(-1);
    }
  }
  
  @Override
  protected void finalize() throws Throwable {
    db.close();
  };
  
  private String get(String name) {
    byte[] dbVal = db.get(name.getBytes());
    return dbVal == null ? null : new String(dbVal);
  }
  
  private void set(String name, String value) {
    db.put(name.getBytes(), value.getBytes());
  }
  
  public void setLogInit(String name) {
    set(name, "UPLOAD:0");
  }
  
  public void updateLog(String localpath, int v) {
    set(localpath, "UPLOAD:" + v);
  }
  
  public void setLogFinish(String localpath) {
    set(localpath, "UPLOAD:" + Integer.MAX_VALUE);
  }
  
  public boolean logVisisted(String path) {
    String v = get(path);
    return v != null && v.startsWith("UPLOAD");
  }
  
  public void setJobVisited(String dir) {
    set(dir, "DIRECTORY");
  }
  
  public boolean jobVisited(String dir) {
    String v = get(dir);
    return v != null && v.equals("DIRECTORY");
  }

  public List<SparkJobLogFile> getUnfinishedSparkLogFile() {
    DBIterator it = db.iterator();
    List<SparkJobLogFile> jFiles = new ArrayList<SparkJobLogFile>();
      
    try {
      logger.info(String.format("Get Log file from DB[%s]", STORAGE_PATH));
      for (it.seekToFirst(); it.hasNext(); it.next()) {
        String key = new String(it.peekNext().getKey());
        String value = new String(it.peekNext().getValue());
        
        if (value.equals("DIRECTORY")) continue;
        int v = Integer.parseInt(value.split(":")[1]);
        if (v != Integer.MAX_VALUE) {
          jFiles.add(new SparkJobLogFile(key, v));
        }
      }
    } catch (Exception e) {
      logger.error(String.format(
          "Get Log file from DB[%s] error:", STORAGE_PATH), e);
    }

    logger.info(String.format("Get %d log files.", jFiles.size()));
    return jFiles;
  }
  
  
}
