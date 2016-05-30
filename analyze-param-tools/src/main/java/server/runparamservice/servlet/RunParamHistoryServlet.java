package server.runparamservice.servlet;

import com.google.gson.Gson;
import common.Config;
import common.DateUtil;
import common.ServletUtil;
import common.TypeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import server.runparamservice.pojo.ItemStatus;
import server.runparamservice.pojo.ParamHistoryItem;
import server.runparamservice.response.RunParamHistoryResponse;
import server.runparamservice.response.RunParamHistoryResponseStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunParamHistoryServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(RunParamHistoryServlet.class);
    private static Config conf = Config.getInstance();
    private Pattern metaPattern = Pattern.compile("((\\d+)_(\\w+)).meta");
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RunParamHistoryResponse response;
        String pageIdxStr = req.getParameter("pageIdx");
        if (pageIdxStr == null || !TypeUtil.isInteger(pageIdxStr)) {
            response = new RunParamHistoryResponse(RunParamHistoryResponseStatus.INVALID_ARGUMENT);
        } else {
            Collection<File> files = FileUtils.listFiles(
                    new File(conf.getSSHOutputDir()), new String[]{"meta"}, false);
            List<ParamHistoryItem> items = new ArrayList<ParamHistoryItem>();
            for (File f : files) {
                try {
                    ParamHistoryItem item = parseMetaData(f);
                    if (item != null) items.add(item);
                } catch (Exception e) {
                    logger.warn("Exception in parsing " + f.getName(), e);
                }
            }
            Collections.sort(items);

            int pageCount = Math.max((int) Math.ceil(1.0 * items.size() / conf.getMongoNumItemsPerPage()), 1);
            int pageIdx = Math.max(1, Math.min(Integer.parseInt(pageIdxStr), pageCount));
            int startIdx = (pageIdx - 1) * conf.getMongoNumItemsPerPage();
            int endIdx = Math.min(startIdx + conf.getMongoNumItemsPerPage(), items.size());
            List<ParamHistoryItem> itemsSubList = items.subList(startIdx, endIdx);

            response = new RunParamHistoryResponse(RunParamHistoryResponseStatus.SUCCESS);
            response.setItems(itemsSubList);
            response.setActivePage(pageIdx);
            response.setPageCount(pageCount);
        }

        String msg = gson.toJson(response, RunParamHistoryResponse.class);
        ServletUtil.setResponse(resp, msg);
    }

    private ParamHistoryItem parseMetaData(File file) throws IOException {
        Matcher matcher = metaPattern.matcher(file.getName());
        if (!matcher.find()) return null;

        String startTime = DateUtil.getTimeStr(Long.parseLong(matcher.group(2)));
        String appid = matcher.group(3);
        String filePrefix = matcher.group(1);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String cmd = br.readLine();
        Long runTimeInSec = null;
        ItemStatus succeed = ItemStatus.UNDEFINED;
        if (cmd != null) {
            runTimeInSec = Long.parseLong(br.readLine()) / 1000;
            succeed = Boolean.parseBoolean(br.readLine()) ? ItemStatus.SUCCEED : ItemStatus.FAILED;
        }
        br.close();

        return new ParamHistoryItem(startTime, appid, filePrefix, cmd, runTimeInSec, succeed);
    }

}
