package cn.edu.thu.thss.clusteranalyzetools.common;

import org.apache.log4j.Logger;

public class MiscUtil {
    private static Logger logger = Logger.getLogger(MiscUtil.class);

    public static void sleepBySec(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (Exception e) {
            logger.fatal("Unexpected exception occured:\n", e);
            logger.fatal("System exited.");
            System.exit(-1);
        }
    }
}
