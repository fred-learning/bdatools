package bin;

import common.Config;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import server.service.RecommendParamsService;

public class RecommendService {
    private static Logger logger = Logger.getLogger(RecommendService.class);
    private static Config conf = Config.getInstance();

    public static void main(String[] args) {
        Server server = new Server(conf.getJettyPort());
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        handler.addServlet(server.servlet.RecommendParams.class, "/sparkrecommend");

        warmup();
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

    public static void warmup() {
        RecommendParamsService.getInstance(); // clear error running job
    }

}
