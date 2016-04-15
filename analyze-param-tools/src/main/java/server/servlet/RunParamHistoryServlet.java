package server.servlet;

import com.google.gson.Gson;
import common.Config;
import common.DateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import server.runparamservice.pojo.ItemStatus;
import server.runparamservice.pojo.ParamHistoryItem;

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

        String json = gson.toJson(items);
        resp.getWriter().write(json);
        resp.setStatus(HttpStatus.OK_200);
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
