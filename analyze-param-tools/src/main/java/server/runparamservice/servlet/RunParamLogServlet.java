package server.runparamservice.servlet;

import common.Config;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class RunParamLogServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(RunParamLogServlet.class);
    private static Config conf = Config.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String filePrefix = req.getParameter("filePrefix");
        resp.setStatus(HttpStatus.OK_200);
        if (filePrefix == null) {
            resp.getWriter().println("Invalid parameters.");
            return;
        }

        File stdoutFile = new File(conf.getSSHOutputDir() + filePrefix + ".stdout");
        File stderrFile = new File(conf.getSSHOutputDir() + filePrefix + ".stderr");
        File metaFile = new File(conf.getSSHOutputDir() + filePrefix + ".meta");
        if (!stdoutFile.exists() || !stderrFile.exists() || !metaFile.exists()) {
            resp.getWriter().println("File not exists");
            return;
        }

        try {
            resp.setStatus(HttpStatus.OK_200);
            BufferedWriter bw = new BufferedWriter(resp.getWriter());
            bw.write("meta:\n");
            bw.write(FileUtils.readFileToString(metaFile));
            bw.write("\n\n\n");
            bw.write("stdout:\n");
            bw.write(FileUtils.readFileToString(stdoutFile));
            bw.write("\n\n\n");
            bw.write("stderr:\n");
            bw.write(FileUtils.readFileToString(stderrFile));
            bw.flush();
        } catch (IOException e) {
            logger.error("error when reading output file");
        }
    }
}
