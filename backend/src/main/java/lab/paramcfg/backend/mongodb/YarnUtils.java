package lab.paramcfg.backend.mongodb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import lab.paramcfg.backend.common.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;

public class YarnUtils {
	/**
	 * 
	 * @return ret[0]=available_vcores,ret[1]=available_memory_in_megabyte
	 */
	public static int[] pullYarnResource() {
		int ret[] = new int[2];
		try {
			// 拼凑get请求的URL字串
			URL getUrl = new URL(Config.REST_RESOURCE_PATH);

			// 根据拼凑的URL，打开连接，URL.openConnection()函数会根据
			// URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection
			HttpURLConnection connection = (HttpURLConnection) getUrl
					.openConnection();

			// 建立与服务器的连接，并未发送数据
			connection.connect();

			// 发送数据到服务器并使用Reader读取返回的数据
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String lines;
			String jsonString = null;
			while ((lines = reader.readLine()) != null) {
				jsonString = lines;
			}
			try {
				JSONObject jsonObject1 = new JSONObject(jsonString);
				String appvalue = jsonObject1.getString("clusterMetrics");
				JSONObject jsonObject2 = new JSONObject(appvalue);
				ret[0] = Integer.valueOf(jsonObject2
						.getString("availableVirtualCores"));
				ret[1] = Integer.valueOf(jsonObject2.getString("availableMB"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ret;
	}

	/**
	 * 
	 * @return
	 */
	public static String pullNewestAppId(long starttime, long endtime) {
		String ret = "application_00";
		try {
			// 拼凑get请求的URL字串
			// String url =
			// Config.REST_NEWESTJOB_PATH+"?startedTimeBegin="+starttime+"&finishedTimeBegin="+endtime
			// 以上命令在运行时会出现时间范围不正确的现象，endtime有可能比任务结束时间晚
			// TODO： 制定搜索范围，确保任务返回没有问题
			String url = Config.REST_NEWESTJOB_PATH + "?startedTimeBegin="
					+ starttime;
			URL getUrl = new URL(url);
			System.out.println(url);
			// 根据拼凑的URL，打开连接，URL.openConnection()函数会根据
			// URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection
			HttpURLConnection connection = (HttpURLConnection) getUrl
					.openConnection();

			// 建立与服务器的连接，并未发送数据
			connection.connect();

			// 发送数据到服务器并使用Reader读取返回的数据
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String lines;
			String jsonString = null;
			while ((lines = reader.readLine()) != null) {
				jsonString = lines;
			}
			try {
				JSONObject jsonObject1 = new JSONObject(jsonString);
				String apps = jsonObject1.getString("apps");
				JSONObject jsonObject2 = new JSONObject(apps);
				JSONArray jsonArray = jsonObject2.getJSONArray("app");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsontmp = new JSONObject(jsonArray.getString(i));
					String tmpid = jsontmp.getString("id");
					if (tmpid.compareTo(ret) > 0) {
						ret = tmpid;
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("I get the new id:" + ret);
		return ret;
	}

	public static int pullJobStatus(String jobid) {
		int ret = 0;
		try {
			// 拼凑get请求的URL字串
			URL getUrl = new URL(Config.REST_NEWESTJOB_PATH + jobid);

			// 根据拼凑的URL，打开连接，URL.openConnection()函数会根据
			// URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection
			HttpURLConnection connection = (HttpURLConnection) getUrl
					.openConnection();

			// 建立与服务器的连接，并未发送数据
			connection.connect();

			// 发送数据到服务器并使用Reader读取返回的数据
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String lines;
			String jsonString = null;
			while ((lines = reader.readLine()) != null) {
				jsonString = lines;
			}
			try {
				JSONObject jsonObject1 = new JSONObject(jsonString);
				String appvalue = jsonObject1.getString("app");
				JSONObject jsonObject2 = new JSONObject(appvalue);
				String status = jsonObject2.getString("finalStatus");
				if (status.equals("SUCCEEDED")) {
					ret = 1;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ret;
	}
}
