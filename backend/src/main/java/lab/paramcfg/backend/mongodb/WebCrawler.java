package lab.paramcfg.backend.mongodb;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class WebCrawler {

	/**
	 * 根据URL抓取网页内容
	 * 
	 * @param url
	 * @return
	 */
	public String getContentFromUrl(String url) {
		/* 实例化一个HttpClient客户端 */
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet getHttp = new HttpGet(url);

		String content = null;

		HttpResponse response;
		try {
			/* 获得信息载体 */
			response = client.execute(getHttp);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				/* 转化为文本信息 */
				content = EntityUtils.toString(entity);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}

}
