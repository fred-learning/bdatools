import org.apache.log4j.Logger;

/**
 * Created by kaze on 1/15/16.
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("info message 1");
        logger.info("info message 2");
        Thread.sleep(2000);
        logger.info("info message after 2 sec");
        logger.warn("warn message 1");
        logger.debug("debug message 1");
        logger.fatal("fatal message 1");
    }
}
