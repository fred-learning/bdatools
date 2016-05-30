package server.recommendservice.servlet;

import com.google.gson.Gson;
import common.ServletUtil;
import common.TypeUtil;
import server.recommendservice.response.ProgressViewItem;
import server.recommendservice.response.ProgressViewResponse;
import server.recommendservice.response.ProgressViewResponseStatus;
import server.recommendservice.service.ProgressClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RecommendProgressViewServlet extends HttpServlet {
    private static Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pageIdxStr = req.getParameter("pageIdx");

        ProgressViewResponse response;
        if (pageIdxStr == null || !TypeUtil.isInteger(pageIdxStr)) {
            response = new ProgressViewResponse(ProgressViewResponseStatus.LACK_OF_ARGUMENT_ERROR);
        } else {
            ProgressClient client = new ProgressClient();
            client.connect();
            int pageCount = client.pageCount();
            int activePage = Math.min(Math.max(1, Integer.parseInt(pageIdxStr)), pageCount);
            List<ProgressViewItem> items = client.getItems(activePage - 1);
            response = new ProgressViewResponse(ProgressViewResponseStatus.SUCCESS);
            response.setItems(items);
            response.setActivePage(activePage);
            response.setPageCount(pageCount);
            client.close();
        }

        String msg = gson.toJson(response, ProgressViewResponse.class);
        ServletUtil.setResponse(resp, msg);
    }
}
