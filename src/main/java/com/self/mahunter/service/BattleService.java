package com.self.mahunter.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;

import com.self.mahunter.entity.Fairy;
import com.self.mahunter.utils.HttpClientHelper;

public class BattleService {

	private static BattleService instance = new BattleService();

	private UserStateService userStateService = UserStateService.getInstance();

	public static BattleService getInstance() {
		return instance;
	}

	private BattleService() {

	}

	private ExecutorService exec = Executors.newSingleThreadExecutor();

	private BattleThread battleThread;

	private Browser browser;

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public void start() {
		if (null != battleThread && battleThread.state == 1) {
			battleThread.state = 2;
		}
		battleThread = new BattleThread();
		exec.execute(battleThread);
	}

	public void stop() {
		if (null != battleThread && battleThread.state == 1) {
			battleThread.state = 2;
		}
	}

	private class BattleThread implements Runnable {

		int state = 0; // 0 未启动，1 已启动， 2 准备中止, 3已终止

		private boolean checkStopSignal() {
			if (state == 2) {
				state = 3;
				return true;
			} else {
				return false;
			}
		}

		public void run() {
			state = 1;
			while (true) {
				if (checkStopSignal()) {
					print("自动攻击已暂停");
					return;
				}

				// 遍历妖精
				String xmlResult = HttpClientHelper
						.post("http://game2-CBT.ma.sdo.com:10001/connect/app/menu/fairyselect?cyt=1",
								null);

				List<Fairy> bosses = new ArrayList<Fairy>();

				try {
					SAXReader saxReader = new SAXReader();
					Document document = saxReader
							.read(new ByteArrayInputStream(xmlResult
									.getBytes("UTF-8")));
					Node node = document
							.selectSingleNode("/response/header/error/code");
					if (null != node && "0".equals(node.getText())) {
						List nodes = document
								.selectNodes("/response/body/fairy_select/fairy_event");
						for (int i = 0; i < nodes.size(); i++) {
							Element element = (Element) nodes.get(i);
							if ("1".equals(element.element("put_down")
									.getText())) {
								Fairy fairy = new Fairy();
								fairy.setSeriald(Integer.parseInt((element
										.element("fairy"))
										.elementText("serial_id")));
								fairy.setUserId(Integer.parseInt((element
										.element("user")).elementText("id")));
								bosses.add(fairy);
							}
						}
					} else {
						System.out.println("操作失败退出程序:"
								+ document.selectSingleNode(
										"/response/header/error/message")
										.getText());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (bosses.size() == 0) {
					print("扫描结束，没有发现可以攻击的妖精");
				} else {
					print("扫描结束，发现可以攻击的妖精" + bosses.size() + "只：");
					for (int i = 0; i < bosses.size(); i++) {

						// 开始打怪
						Fairy fairy = bosses.get(i);

						Map<String, String> params = new HashMap<String, String>();
						params.put("serial_id", "" + fairy.getSeriald());
						params.put("user_id", "" + fairy.getUserId());

						xmlResult = HttpClientHelper
								.post("http://game2-CBT.ma.sdo.com:10001/connect/app/exploration/fairybattle?cyt=1",
										params);
						System.out.println(xmlResult);
						rest();
					}
				}

				rest();
			}
		}
	}
	
	private void rest(){
		try {
			Thread.sleep(5000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void rest(long timemili){
		try {
			Thread.sleep(timemili);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void print(final String msg) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				browser.execute("appendBattleInfo(\"" + msg + "\")");
			}
		});
	}

}
