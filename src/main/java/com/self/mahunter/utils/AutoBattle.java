package com.self.mahunter.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class AutoBattle {

	private static final String username = "15811202808";

	private static final String password = "4fun4game";

	private static final long interval = 1000l;

	public static void main(String[] args) {
		// 登陆
		System.out.println(String.format(
				"Start login as user:[%s], password:[%s]", username, password));

		String userId = null;

		HttpClientHelper
				.post("http://game2-CBT.ma.sdo.com:10001/connect/app/check_inspection?cyt=1",
						null);

		Map<String, String> params = new HashMap<String, String>();
		params.put("login_id", username);
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
					userId = ((Element) document
							.selectSingleNode("/response/body/login/user_id"))
							.getText();
					System.out.println("登陆成功，登陆用户ID：" + userId);
				} else {
					System.out
							.println("登陆失败退出："
									+ document.selectSingleNode(
											"/response/header/error/message")
											.getText());
					return;
				}
			} else {
				System.out.println("Login Failed: ERROR");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// 遍历妖精
		xmlResult = HttpClientHelper
				.post("http://game2-CBT.ma.sdo.com:10001/connect/app/menu/fairyselect?cyt=1",
						null);

		List<String> bosses = new ArrayList<String>();

		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new ByteArrayInputStream(
					xmlResult.getBytes("UTF-8")));
			Node node = document
					.selectSingleNode("/response/header/error/code");
			if (null != node && "0".equals(node.getText())) {
				List nodes = document
						.selectNodes("/response/body/fairy_select/fairy_event");
				for (int i = 0; i < nodes.size(); i++) {
					Element element = (Element) nodes.get(i);
					if ("1".equals(element.element("put_down").getText())) {
						bosses.add((element.element("fairy"))
								.elementText("serial_id"));
					}
				}
			} else {
				System.out.println("操作失败退出程序:"
						+ document.selectSingleNode(
								"/response/header/error/message").getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (bosses.size() == 0) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("发现可以攻击的妖精" + bosses.size() + "只：");
			for (int i = 0; i < bosses.size(); i++) {

				// 开始打怪
				String bossid = bosses.get(i);

				params.clear();
				params.put("serial_id", bossid);
				params.put("user_id", userId);

				xmlResult = HttpClientHelper
						.post("http://game2-CBT.ma.sdo.com:10001/connect/app/menu/fairyselect?cyt=1",
								params);
				
			}
		}
	}
}
