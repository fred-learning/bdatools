package bin;

import common.Config;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import server.historydb.servlet.HistoryDBAddServlet;
import server.historydb.servlet.HistoryDBDeleteServlet;
import server.historydb.servlet.HistoryDBViewServlet;
import server.recommendservice.service.RecommendParamsService;
import server.recommendservice.servlet.RecommendJobSubmitServlet;
import server.recommendservice.servlet.RecommendProgressDetailServlet;
import server.recommendservice.servlet.RecommendProgressViewServlet;
import server.runparamservice.servlet.RunParamHistoryServlet;
import server.runparamservice.servlet.RunParamLogServlet;
import server.runparamservice.servlet.RunParamSubmitServlet;

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
        // recommend params
        handler.addServlet(RecommendProgressViewServlet.class, conf.getRecommendProgressViewServletPath());
        handler.addServlet(RecommendProgressDetailServlet.class, conf.getRecommendProgressDetailServletPath());
        handler.addServlet(RecommendJobSubmitServlet.class, conf.getRecommendJobSubmitServletPath());

        // run params
        handler.addServlet(RunParamSubmitServlet.class, conf.getRunParamSubmitServletPath());
        handler.addServlet(RunParamHistoryServlet.class, conf.getRunParamHistoryServletPath());
        handler.addServlet(RunParamLogServlet.class, conf.getRunParamLogServletPath());

        // history db management
        handler.addServlet(HistoryDBAddServlet.class, conf.getHistoryDBAddServletPath());
        handler.addServlet(HistoryDBDeleteServlet.class, conf.getHistoryDBDeleteServletPath());
        handler.addServlet(HistoryDBViewServlet.class, conf.getHistoryDBViewServletPath());

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
