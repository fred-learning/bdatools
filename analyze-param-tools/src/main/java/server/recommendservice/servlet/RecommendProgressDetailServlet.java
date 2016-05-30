package server.recommendservice.servlet;

import com.google.gson.Gson;
import common.ServletUtil;
import server.recommendservice.response.ProgressDetailResponse;
import server.recommendservice.response.ProgressDetailResponseStatus;
import server.recommendservice.response.ProgressViewItem;
import server.recommendservice.service.ProgressClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RecommendProgressDetailServlet extends HttpServlet {
    private static Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String progressid = req.getParameter("progressid");

        ProgressDetailResponse response;
        if (progressid == null) {
            response = new ProgressDetailResponse(ProgressDetailResponseStatus.LACK_OF_ARGUMENT_ERROR);
        } else {
            ProgressClient client = new ProgressClient();
            client.connect();
            ProgressViewItem item = client.getItem(progressid);
            if (item == null) {
                response = new ProgressDetailResponse(ProgressDetailResponseStatus.PROGRESS_NOT_FOUND);
            } else {
                response = new ProgressDetailResponse(ProgressDetailResponseStatus.SUCCESS);
                response.setItem(item);
            }
            client.close();
        }

        String msg = gson.toJson(response, ProgressDetailResponse.class);
        ServletUtil.setResponse(resp, msg);
    }
}
