package server.runparamservice;

import common.Config;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class RunParamJob implements Runnable {
    private static Logger logger = Logger.getLogger(RunParamJob.class);
    private static Config config = Config.getInstance();
    private String appid;
    private String cmd;
    private File stdoutFile, stderrFile, metaFile;
    private Boolean initSuccess;

    public RunParamJob(String appid, String cmd) {
        this.appid = appid;
        this.cmd = cmd;

        Long jobStartTime = new Date().getTime();
        String fileName = String.format("%s_%s", jobStartTime, appid);
        stdoutFile = new File(config.getSSHOutputDir() + fileName + ".stdout");
        stderrFile = new File(config.getSSHOutputDir() + fileName + ".stderr");
        metaFile = new File(config.getSSHOutputDir() + fileName + ".meta");
        try {
            if (!stdoutFile.exists()) stdoutFile.createNewFile();
            if (!stderrFile.exists()) stderrFile.createNewFile();
            if (!metaFile.exists()) metaFile.createNewFile();
            initSuccess = true;
        } catch (IOException e) {
            logger.error("error when creating new file:", e);
            try {
                if (!stdoutFile.exists()) stdoutFile.delete();
                if (!stderrFile.exists()) stderrFile.delete();
                if (!metaFile.exists()) metaFile.delete();
            } catch (Exception e2) {
                logger.error("error when deleting file:", e2);
            }
            initSuccess = false;
        }
    }

    public void run() {
        if (!initSuccess) return;

        String runCmd = String.format("ssh %s@%s \"%s\"",
                config.getSSHUser(), config.getSSHIP(), cmd);
        Date startTime = new Date();

        boolean isSucceed = true;
        try {
            logger.info("run command: " + runCmd);
            ProcessBuilder pb = new ProcessBuilder()
                    .command(new String[]{"/bin/bash", "-c", runCmd})
                    .redirectOutput(stdoutFile)
                    .redirectError(stderrFile);
            Process process = pb.start();
            process.waitFor();
            logger.info("run command finished");
        } catch (Exception e) {
            String msg = String.format("Error in executing command [%s]:", runCmd);
            logger.error(msg, e);
            isSucceed = false;
        }

        Date endTime = new Date();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(metaFile));
            String msg = String.format("%s\n%d\n%b\n",
                    cmd, endTime.getTime() - startTime.getTime(), isSucceed);
            bw.write(msg);
            bw.close();
        } catch (Exception e) {
            logger.error("Error in writing metadata:", e);
        }

    }
}
