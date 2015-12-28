package bin;

import common.CmdUtil;
import common.Config;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import server.service.RecommendParamsService;

public class RecommendService implements Runnable {
    private static Logger logger = Logger.getLogger(RecommendService.class);
    private static Config conf = Config.getInstance();

    public void run() {
        warmup();
        Server server = new Server(conf.getJettyPort());
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        handler.addServlet(server.servlet.RecommendParams.class, conf.getServletPath());
        try {
            logger.info("Start jetty server on port " + conf.getJettyPort());
            server.start();
            server.join();
        } catch (Exception e) {
            logger.fatal("Jetty server failed.", e);
            logger.fatal("System exit.");
            System.exit(-1);
        }
    }

    public void warmup() {
        RecommendParamsService.getInstance(); // clear error running job
    }

}
