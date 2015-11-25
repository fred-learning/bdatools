package lab.paramcfg.backend.storage.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lab.paramcfg.backend.common.Config;

import org.apache.log4j.Logger;

public class MonitoringData {
    private static Logger logger = Logger.getLogger(MonitoringData.class);
  
    private String jobid;
    private HashMap<String, ArrayList<String[]>> datas;

    private String[] MONITOR_METRICS = { 
        "cpu_user", "cpu_system", 
        "bytes_in", "bytes_out", 
        "disk_writes", "disk_reads", 
    };

    public MonitoringData(String cjobid, long startTime, long endTime) {
        jobid = cjobid;
        datas = new HashMap<String, ArrayList<String[]>>();
        extract(Config.RRDS_PATH, Config.NODENAMES, startTime, endTime);
    }

    private void extract(String rrdsPath, String[] nodeNames, long startTime, long endTime) {
        logger.info("Extracting nodes rdd data.");
        
        for (String nodename : nodeNames) {
            for (String monitor : MONITOR_METRICS) {
                String strCmd = "rrdtool fetch " + rrdsPath + nodename
                        + "/" + monitor + ".rrd" + " AVERAGE -s " + startTime
                        + " -e " + endTime;
                logger.info("Running command:" + strCmd);
                
                Process process;
                ArrayList<String[]> onetypelist = new ArrayList<String[]>();
                try {
                    String[] cmdarr = strCmd.split(" ");
                    process = Runtime.getRuntime().exec(cmdarr);
                    // process.waitFor();
                    // 读取屏幕输出
                    BufferedReader strCon = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));
                    String line;
                    int count=0;
                    while ((line = strCon.readLine()) != null) {
                        // 存储key,value
                        String regex = "^\\d+: \\d";
                        Pattern pt = Pattern.compile(regex);
                        Matcher matcher = pt.matcher(line);
                        if (matcher.find()) {
                            count++;
                            String key = line.split(": ")[0];
                            String value = line.split(": ")[1];
                            String[] eachdata = { key, value };
                            onetypelist.add(eachdata);
                        }
                    }
                    logger.info("get monitor data counts:"+count);
                } catch (IOException e) {
                    logger.error(e);
                }
                datas.put(nodename + "_" + monitor, onetypelist);
            }
        }
    }

    public double similarity(MonitoringData other) {
        String[] nodeNames = Config.NODENAMES;
        SAXConstructObject dataConstruct = new SAXConstructObject();
        RealTimeSeriesRepresentation representation = new SAXRepresentation();
        RealTimeSeriesSimilarity similarity = new LpSimilarity();
        List<List<Double>> timeSeriesData1 = new ArrayList<List<Double>>();
        List<List<Double>> timeSeriesData2 = new ArrayList<List<Double>>();
        for(String nodeName : nodeNames){
            for(int i = 0 ; i < MONITOR_METRICS.length ; i ++)
            {
                
                List<String[]> values = this.datas.get(nodeName + "_" + MONITOR_METRICS[i]);
                List<Double> data = new ArrayList<Double>();
                for(String[] value : values){
                    data.add(Double.parseDouble(value[1]));
                }
                timeSeriesData1.add(data);
            }
        }
        for(String nodeName : nodeNames){
            for(int i = 0 ; i < MONITOR_METRICS.length ; i ++)
            {
                
                List<String[]> values = other.datas.get(nodeName + "_" + MONITOR_METRICS[i]);
                List<Double> data = new ArrayList<Double>();
                for(String[] value : values){
                    data.add(Double.parseDouble(value[1]));
                }
                timeSeriesData2.add(data);
            }
        }

        
        ParametersObject parametersObject = new SAXParametersObject(timeSeriesData1.size(),8,4);
        List<List<Double>> result1 = representation.getRealRepresentation(timeSeriesData1, parametersObject);
        List<List<Double>> result2 = representation.getRealRepresentation(timeSeriesData2, parametersObject);
        dataConstruct.setTimeSeries1(timeSeriesData1);
        dataConstruct.setTimeSeries2(timeSeriesData2);
        dataConstruct.setTransferTimeSeries1(result1);
        dataConstruct.setTransferTimeSeries2(result2);
        dataConstruct.setSaxParametersObject(parametersObject);
        dataConstruct.setDistance(similarity.calculateSimilarity(result1, result2));
        
        return dataConstruct.getDistance();
    }

    public String getJobid() {
        return jobid;
    }

    public HashMap<String, ArrayList<String[]>> getDatas() {
        return datas;
    }

}
