package lab.paramcfg.backend.elasticsearch;

import org.apache.log4j.Logger;

public class DataMigrator {
	private static Logger logger = Logger.getLogger(DataMigrator.class);
	private static long INTERVAL = 60 * 60 * 1000;
	private HDFSPuller puller;
	private ESUploader uploader;

	public DataMigrator() {
	  FilterDB db = new FilterDB();
	  puller = new HDFSPuller(db);
	  uploader = new ESUploader(db);
	}

	public void run() throws Exception {
	  while (puller.pullSparkLogToLocal()) {
	    uploader.upload();
	    logger.info(String.format("Upload finished, sleep %s seconds.", INTERVAL / 1000));
	    Thread.sleep(INTERVAL);
	  }
	}
	
	public static void main(String[] args) throws Exception {
	  DataMigrator migrator = new DataMigrator();
	  migrator.run();
	  logger.info("Data Migrator finished.");
	}
	
}
