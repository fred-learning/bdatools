package server.recommendservice.servlet;

import com.google.gson.Gson;
import common.Config;
import common.ServletUtil;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import server.recommendservice.response.JobSubmitResponse;
import server.recommendservice.response.JobSubmitStatus;
import server.recommendservice.service.RecommendParamsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RecommendJobSubmitServlet extends HttpServlet {
    private static RecommendParamsService service = RecommendParamsService.getInstance();
    private static Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String appid = req.getParameter("appid");

        JobSubmitResponse response;
        if (appid == null || appid.trim().length() == 0) {
            response = new JobSubmitResponse(JobSubmitStatus.LACK_OF_ARGUMENT_ERROR);
        } else {
            service.addJob(appid);
            response = new JobSubmitResponse(JobSubmitStatus.SUCCESS);
        }

        String msg = gson.toJson(response, JobSubmitResponse.class);
        ServletUtil.setResponse(resp, msg);
    }
}
