package lab.paramcfg.backend.storage.mongodb;

import com.mongodb.client.MongoCursor;

import lab.paramcfg.backend.common.Util;
import lab.paramcfg.backend.storage.JobData;
import lab.paramcfg.backend.storage.monitor.MonitoringData;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Export data in Mongodb in JSON format.
 */
public class ExportUtils {

    public static void exportMonitoringData(DBInstance db, FileWriter out) throws Exception {
        MongoCursor<Document> cursor = db.getMongoCollection().find().iterator();

        while (cursor.hasNext()) {
            JobData data = Util.mongodocToJobData(cursor.next());
            MonitoringData mdata = data.getMonitoringData();
            String json = monitoingDataToJSON(mdata);
            out.write(json);
            out.write('\n');
            out.flush();
        }
    }

    public static String monitoingDataToJSON(MonitoringData data) throws JSONException {
        Map<String, JSONObject> nodenameMap = new HashMap<String, JSONObject>();

        HashMap<String, ArrayList<String[]>> h = data.getDatas();
        for (Map.Entry<String, ArrayList<String[]>> entry : h.entrySet()) {
            String nodename = entry.getKey().split("_")[0];
            String metric = entry.getKey().substring(nodename.length() + 1);

            // construct inner json object
            ArrayList<String> times = new ArrayList<String>();
            ArrayList<String> values = new ArrayList<String>();
            for (String[] timeNvalue : entry.getValue()) {
                String time = timeNvalue[0];
                String value = timeNvalue[1];
                times.add(time);
                values.add(value);
            }
            JSONObject innerJSON = new JSONObject();
            innerJSON.put("times", times);
            innerJSON.put("values", values);

            // check json exist, and update json
            if (nodenameMap.containsKey(nodename) == false) {
                JSONObject nodeJSON = new JSONObject();
                nodenameMap.put(nodename, nodeJSON);
            }

            JSONObject nodeJSON = nodenameMap.get(nodename);
            nodeJSON.put(metric, innerJSON);
        }

        // combine all nodes' json into a single json
        JSONObject json = new JSONObject();
        for (Map.Entry<String, JSONObject> entry : nodenameMap.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }

        return json.toString();
    }

    public static void main(String[] args) {
        System.out.println("Usage: <command> <command args>\n" +
            "where command can be:\n" +
            "monitor <location>, export monitoring data from mongodb into local file <location>"
        );

        try {
            if (args[0].equals("monitor")) {
                String filepath = args.length > 1 ? args[1] : "./monitor_export";
                FileWriter fw = new FileWriter(filepath);
                DBInstance db = new DBInstance("192.168.3.57", "test", "job");

                System.out.println("Exporting data from mongodb.");
                exportMonitoringData(db, fw);
                fw.close();
                System.out.println("Export finished.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
