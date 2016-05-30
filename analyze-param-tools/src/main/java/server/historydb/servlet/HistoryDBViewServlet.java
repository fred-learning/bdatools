package server.historydb.servlet;

import com.google.gson.Gson;
import common.TypeUtil;
import historydb.App;
import historydb.HistoryClient;
import common.ServletUtil;
import server.historydb.response.ViewResonseStatus;
import server.historydb.response.ViewResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HistoryDBViewServlet extends HttpServlet {
    private static Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pageIdxStr = req.getParameter("pageIdx");

        ViewResponse response;
        if (pageIdxStr == null || !TypeUtil.isInteger(pageIdxStr)) {
            response = new ViewResponse(ViewResonseStatus.ARGUMENT_ERROR);
        } else {
            HistoryClient client = new HistoryClient();
            client.connect();
            int pageCount = client.pageCount();
            int activePage = Math.min(Math.max(1, Integer.parseInt(pageIdxStr)), pageCount);
            List<App> apps = client.getItems(activePage - 1);
            response = new ViewResponse(ViewResonseStatus.SUCCESS);
            response.setViewItems(apps);
            response.setActivePage(activePage);
            response.setPageCount(pageCount);
            client.close();
        }

        String msg = gson.toJson(response, ViewResponse.class);
        ServletUtil.setResponse(resp, msg);
    }
}
