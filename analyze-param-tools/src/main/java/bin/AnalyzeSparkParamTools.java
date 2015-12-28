package bin;

import common.CmdUtil;
import common.Config;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

/**
 * Main Entrance of param recommendation program
 */
public class AnalyzeSparkParamTools {
    private static Logger logger = Logger.getLogger(AnalyzeSparkParamTools.class);
    private static Config config = Config.getInstance();

    public static void main(String[] args) {
        // handle cmd arguments
        Options options = new Options();
        options.addOption("h", false, "usage help");
        options.addOption("f", true, "specify configuration file location");
        CommandLine cmdLine = CmdUtil.parseCmdOrDie(options, args);
        if (cmdLine.hasOption("h")) {
            CmdUtil.printHelpAndExit("AnalyzeSparkParamTools", options);
        }
        if (cmdLine.hasOption("f")) {
            String confFile = cmdLine.getOptionValue("f");
            logger.info("Load config file from " + confFile);
            config.loadOrDie(confFile);
        }

        // start sync service
        logger.info("Start SyncAppInfo thread.");
        Thread syncThread = new Thread(new SyncAppInfo());
        syncThread.start();

        // start recommend service
        logger.info("Start RecommendService thread.");
        Thread recommendThread = new Thread(new RecommendService());
        recommendThread.start();

        try {
            syncThread.join();
            recommendThread.join();
        } catch (Exception e) {
            logger.fatal("Join thread error:", e);
        }
    }
}
