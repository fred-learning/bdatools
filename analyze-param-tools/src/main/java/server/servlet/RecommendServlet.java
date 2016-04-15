package server.servlet;

import common.Config;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import server.recommendservice.RecommendParamsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RecommendServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(RecommendServlet.class);
    private static RecommendParamsService service = RecommendParamsService.getInstance();
    private static Config conf = Config.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String appid = req.getParameter("appid");

        if (appid != null) {
            logger.info(String.format("Service get params (clusterid: %s, appid: %s)",
                    conf.getClusterID(), appid));
            service.addJob(appid);
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println("success");
        } else {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println("error");
        }
    }

}
