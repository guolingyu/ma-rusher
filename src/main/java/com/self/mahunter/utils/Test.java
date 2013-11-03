package com.self.mahunter.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class Test {
	public static void main(String[] args) throws UnsupportedEncodingException,
			DocumentException {
		String name = "15811202808";
		String password = "4fun4game";
		System.out.println(String.format("Login as %1s:%2s", name, password));

		// 先去拿一下cookie
		HttpClientHelper
				.post("http://game2-CBT.ma.sdo.com:10001/connect/app/check_inspection?cyt=1",
						null);

		Map<String, String> params = new HashMap<String, String>();
		params.put("login_id", name);
		params.put("password", password);

		String xmlResult = HttpClientHelper.post(
				"http://game2-CBT.ma.sdo.com:10001/connect/app/login?cyt=1",
				params);

		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new ByteArrayInputStream(
					xmlResult.getBytes("UTF-8")));
			Node node = document
					.selectSingleNode("/response/header/error/code");
			if (null != node) {
				String code = node.getText();
				if ("0".equals(code)) {
					System.out.println("登陆成功");
				} else {
					System.out.println(document.selectSingleNode(
							"/response/header/error/message").getText());
				}
			} else {
				System.out.println("失败");
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		xmlResult = HttpClientHelper
				.get("http://game2-CBT.ma.sdo.com:10001/connect/app/battle/competition_parts?redirect_flg=1");

		System.out.println(xmlResult);

		params.clear();
		params.put("event_id", "46");
		params.put("move", "0");

		xmlResult = HttpClientHelper
				.post("http://game2-CBT.ma.sdo.com:10001/connect/app/battle/battle_userlist?cyt=1",
						params);

		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new ByteArrayInputStream(xmlResult
				.getBytes("UTF-8")));
		List nodes = document
				.selectNodes("/response/body/battle_userlist/user_list/user");

		Set<String> userIds = new HashSet<String>();

		for (int i = 0; i < nodes.size(); i++) {
			Element element = (Element) nodes.get(i);
			System.out.println(element.element("id").getText());

			String userid = element.element("id").getText();
		}

		String userid = "449693";

		params.clear();
		params.put("event_id", "46");
		params.put("user_id", userid);
		params.put("battle_type", "0");

		xmlResult = HttpClientHelper
				.post("http://game2-CBT.ma.sdo.com:10001/connect/app/battle/battle?cyt=1",
						params);

		System.out.println(xmlResult);
		saxReader = new SAXReader();
		document = saxReader.read(new ByteArrayInputStream(xmlResult
				.getBytes("UTF-8")));

		Node node = document.selectSingleNode("/response/header/error/code");
		if (null != node) {
			String code = node.getText();
			if ("0".equals(code)) {
				System.out.println("挑战");
			} else if ("8000".equals(code)) {
				userIds.add(userid);
			}
		} else {
			System.out.println("失败");
		}
	}
}
