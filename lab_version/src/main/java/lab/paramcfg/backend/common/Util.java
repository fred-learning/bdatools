package lab.paramcfg.backend.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lab.paramcfg.backend.storage.*;
import lab.paramcfg.backend.storage.graph.RDDSData;
import lab.paramcfg.backend.storage.journal.JournalData;
import lab.paramcfg.backend.storage.monitor.MonitoringData;
import lab.paramcfg.backend.storage.others.JobConfig;
import lab.paramcfg.backend.storage.others.JobResource;

import org.bson.Document;
import org.bson.types.Binary;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Util {

	private static String OBJ_FIELD_NAME = "JavaObject";

	public static <T> DBObject serializeToMongo(Object o, Class<T> classOfT)
			throws IOException {
		DBObject ret = new BasicDBObject();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		objStream.writeObject(o);
		ret.put(OBJ_FIELD_NAME, byteStream.toByteArray());
		objStream.close();
		byteStream.close();
		return ret;
	}

	public static <T> T deserializeFromMongo(Object o, Class<T> classOfT)
			throws IOException, ClassNotFoundException {
		Binary dbo = (Binary) ((Document) o).get(OBJ_FIELD_NAME);
		byte[] bytes = (byte[]) dbo.getData();
		InputStream inputStream = new ByteArrayInputStream(bytes);
		ObjectInputStream in = new ObjectInputStream(inputStream);
		@SuppressWarnings("unchecked")
		T ret = (T) in.readObject();
		in.close();
		inputStream.close();
		return ret;
	}

    public static JobData mongodocToJobData(Document doc)
            throws ClassNotFoundException, IOException {
        String id = doc.getString("id");
        int status = doc.getInteger("status");

        Gson gson = new Gson();

        Date startTime = doc.getDate("startTime");
        Date endTime = doc.getDate("endTime");
        
//        JobConfig config = Util.deserializeFromMongo(doc.get("config"), JobConfig.class);
//        JobResource resource = Util.deserializeFromMongo(
//                doc.get("limited_resource"), JobResource.class);
//        JournalData jData = Util.deserializeFromMongo(
//                doc.get("journal_data"), JournalData.class);
//        MonitoringData mData = Util.deserializeFromMongo(
//                doc.get("monitoring_data"), MonitoringData.class);
//        RDDSData rData = Util.deserializeFromMongo(	doc.get("rdds_data"), RDDSData.class);
        
        JobConfig config = gson.fromJson(doc.getString("config"), JobConfig.class);
        JobResource resource = gson.fromJson(doc.getString("limited_resource"), JobResource.class);
        JournalData jData = gson.fromJson(doc.getString("journal_data"), JournalData.class);
        MonitoringData mData = gson.fromJson(doc.getString("monitoring_data"), MonitoringData.class);
        RDDSData rData = gson.fromJson(doc.getString("rdds_data"), RDDSData.class);
        
        JobData ret = new JobData(id, status, config, resource,
                startTime, endTime, jData, mData, rData);

        return ret;
    }


	public static Date timechanger(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd hh:mm:ss");
		Date rst = new Date();
		try {
			rst = df.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rst;
	}
	
}
