package server.historydb.servlet;

import com.google.gson.Gson;
import common.Config;
import historydb.HistoryClient;
import common.ServletUtil;
import server.historydb.response.DeleteResonseStatus;
import server.historydb.response.DeleteResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HistoryDBDeleteServlet extends HttpServlet {
    private static Config config = Config.getInstance();
    private static Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String appid = req.getParameter("appid");

        DeleteResponse response;
        HistoryClient client = new HistoryClient();
        client.connect();
        if (appid == null || appid.trim().length() == 0) {
            response = new DeleteResponse(DeleteResonseStatus.LACK_OF_ARGUMENT_ERROR);
        } else if (client.findApp(config.getClusterID(), appid) == null) {
            response = new DeleteResponse(DeleteResonseStatus.APPLICATION_NOT_EXIST);
        } else {
            client.deleteApp(appid);
            response = new DeleteResponse(DeleteResonseStatus.SUCCESS);
        }
        client.close();

        String msg = gson.toJson(response);
        ServletUtil.setResponse(resp, msg);
    }
}
