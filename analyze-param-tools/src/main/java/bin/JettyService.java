package bin;

import common.Config;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import server.recommendservice.RecommendParamsService;
import server.servlet.RecommendServlet;
import server.servlet.RunParamHistoryServlet;
import server.servlet.RunParamLogServlet;
import server.servlet.RunParamServlet;

import java.io.File;
import java.net.InetSocketAddress;

public class JettyService implements Runnable {
    private static Logger logger = Logger.getLogger(JettyService.class);
    private static Config conf = Config.getInstance();

    public void run() {
        warmup();
        Server server = new Server(new InetSocketAddress(conf.getJettyIP(), conf.getJettyPort()));
        ServletContextHandler handler = new ServletContextHandler();
        server.setHandler(handler);
        handler.addServlet(RecommendServlet.class, conf.getRecommendServletPath());
        handler.addServlet(RunParamServlet.class, conf.getRunParamServletPath());
        handler.addServlet(RunParamHistoryServlet.class, conf.getRunParamHistoryServletPath());
        handler.addServlet(RunParamLogServlet.class, conf.getRunParamLogServletPath());
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
        // clear error running job
        RecommendParamsService.getInstance();
        // clear run param history directory
        File runParamDir = new File(conf.getSSHOutputDir());
        try {
            if (runParamDir.exists()) FileUtils.deleteDirectory(runParamDir);
            runParamDir.mkdirs();
        } catch (Exception e) {
            logger.error("Error when encounter in initing " + conf.getSSHOutputDir(), e);
            System.exit(-1);
        }
    }

}
