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

import org.bson.Document;
import org.bson.types.Binary;
import org.mortbay.util.ajax.JSON;

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
		T ret = (T) in.readObject();
		in.close();
		inputStream.close();
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
