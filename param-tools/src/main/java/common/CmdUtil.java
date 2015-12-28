package common;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

public class CmdUtil {
    private static Logger logger = Logger.getLogger(CmdUtil.class);

    public static CommandLine parseCmdOrDie(Options options, String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            logger.fatal("Parse argument error:", e);
            System.exit(-1);
        }
        return cmd;
    }

    public static void printHelpAndExit(String name, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(name, options);
        System.exit(0);
    }
}
