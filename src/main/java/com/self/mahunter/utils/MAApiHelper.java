package com.self.mahunter.utils;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class MAApiHelper {

	private static final String SERVER_HOST = "http://game%d-CBT.ma.sdo.com:10001";

	private int server;

	public MAApiHelper(int server) {
		super();
		this.server = server;
	}

	public int getServer() {
		return server;
	}

	public void setServer(int server) {
		this.server = server;
	}

	public boolean connect() {
		String url = String.format(SERVER_HOST, server)
				+ "/connect/app/check_inspection?cyt=1";
		HttpClientHelper.post(url, null);
		return true;
	}

	public MAApiResult call(String urlPath, Map<String, String> params) {
		String url = String.format(SERVER_HOST, server) + urlPath;

		try {
			String xmlResult = HttpClientHelper.post(url, params);
			if (null != xmlResult) {
				SAXReader saxReader = new SAXReader();
				Document document = saxReader.read(new ByteArrayInputStream(
						xmlResult.getBytes("UTF-8")));

				MAApiResult result = new MAApiResult();
				int error = Integer.parseInt(document.selectSingleNode(
						"/response/header/error/code").getText());
				result.setError(error);
				if (0 != error) {
					Node node = document
							.selectSingleNode("/response/header/error/message");
					if (null != node) {
						result.setErrorMessage(node.getText());
					}
				}
				result.setData(document);
				return result;
			} else {
				MAApiResult result = new MAApiResult(404, "服务器没有返回任何信息", null);
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
			MAApiResult result = new MAApiResult(505, "连接服务器出现异常:" + e, null);
			return result;
		}
	}

}
