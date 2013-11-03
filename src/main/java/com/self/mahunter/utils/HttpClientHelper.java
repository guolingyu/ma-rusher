package com.self.mahunter.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientHelper {

	private static final String MOBLIE_USER_AGENT = "Million/100 (t03gchnduos; t03gduoszc; 4.1.1) samsung/t03gduoszc/t03gchnduos:4.1.1/JRO03C/N7102ZCAMB3:user/release-keys";

	private static final String WEB_USER_AGENT = "Mozilla/5.0 (iPad; CPU OS 5_0 like Mac OS X) AppleWebKit/534.44 (KHTML, like Gecko) Version/5.0.2 Mobile/9A241 Safari/6533.18.5";

	private static CloseableHttpClient httpclient = HttpClients.createDefault();

	private static HttpClientContext context = HttpClientContext.create();

	public synchronized static String post(String url,
			Map<String, String> params) {
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("User-Agent", WEB_USER_AGENT);
			if (null != params && !params.isEmpty()) {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (String key : params.keySet()) {
					formparams
							.add(new BasicNameValuePair(key, params.get(key)));
				}
				UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(
						formparams, "UTF-8");
				post.setEntity(uefEntity);
			}

			HttpResponse response = httpclient.execute(post, context);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public synchronized static String get(String url) {
		try {
			HttpGet get = new HttpGet(url);
			get.addHeader("User-Agent", WEB_USER_AGENT);
			HttpResponse response = httpclient.execute(get, context);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
