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
import com.self.mahunter.utils.MAApiHelper;
import com.self.mahunter.utils.MAApiResult;

public class BattleService {

	private static BattleService instance = new BattleService();

	private UserStateService userStateService = UserStateService.getInstance();

	private BattleIntelligentService battleIntelligentService = BattleIntelligentService
			.getInstance();

	private CardSellService cardSellService = CardSellService.getInstance();

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

		String currentCards = null;

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

				MAApiHelper apiHelper = userStateService.getApiHelper();
				MAApiResult apiResult = null;

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

				if (checkStopSignal()) {
					print("自动攻击已暂停");
					return;
				}

				if (bosses.size() == 0) {
					print("扫描结束，没有发现可以攻击的妖精");
				} else {
					print("扫描结束，发现可以攻击的妖精" + bosses.size() + "只：");
					for (int i = 0; i < bosses.size(); i++) {

						if (checkStopSignal()) {
							print("自动攻击已暂停");
							return;
						}

						Fairy fairy = bosses.get(i);

						// 检查一下血量和等级
						Map<String, String> params = new HashMap<String, String>();
						params.put("serial_id", "" + fairy.getSeriald());
						params.put("user_id", "" + fairy.getUserId());
						xmlResult = HttpClientHelper
								.post("http://game2-CBT.ma.sdo.com:10001/connect/app/exploration/fairyhistory?cyt=1",
										params);

						try {
							SAXReader saxReader = new SAXReader();
							Document document = saxReader
									.read(new ByteArrayInputStream(xmlResult
											.getBytes("UTF-8")));
							Node node = document
									.selectSingleNode("/response/header/error/code");
							if (null != node && "0".equals(node.getText())) {
								Element fairyHistory = (Element) document
										.selectSingleNode("/response/body/fairy_history/fairy");
								int level = Integer.parseInt(fairyHistory
										.element("lv").getText());
								int hp = Integer.parseInt(fairyHistory.element(
										"hp").getText());
								fairy.setHp(hp);
								fairy.setLevel(level);
							} else {
								System.out
										.println("操作失败退出程序:"
												+ document
														.selectSingleNode(
																"/response/header/error/message")
														.getText());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						// 设置卡组
						String cards = battleIntelligentService.getCards(fairy);
						if (null != cards) {
							String[] cardSplited = cards.split(",");
							String leader = cardSplited[0];
							StringBuilder sb = new StringBuilder(cards);
							for (int j = 0; j < 12 - cardSplited.length; j++) {
								sb.append(",empty");
							}

							params.clear();
							params.put("C", sb.toString());
							params.put("lr", leader);

							xmlResult = HttpClientHelper
									.post("http://game2-CBT.ma.sdo.com:10001/connect/app/cardselect/savedeckcard?cyt=1",
											params);
						}

						// 打怪
						params.clear();
						params.put("serial_id", "" + fairy.getSeriald());
						params.put("user_id", "" + fairy.getUserId());

						apiResult = apiHelper.call(
								"/connect/app/exploration/fairybattle?cyt=1",
								params);
						if (0 == apiResult.getError()) {
							boolean ifSuccess = "1"
									.equals(apiResult
											.query("/response/body/battle_result/winner"));
							String discover = apiResult
									.query("/response/body/explore/fairy/attacker_history/attacker[discoverer=1]/user_name");
							String fairyName = apiResult
									.query("/response/body/explore/fairy/name");
							int hpLeft = Integer.parseInt(apiResult
									.query("/response/body/explore/fairy/hp"));
							print("攻击妖精:[" + fairyName + "],lv:["
									+ fairy.getLevel() + "], hp:["
									+ fairy.getHp() + "],发现人:[" + discover
									+ "],是否消灭:[" + ifSuccess + "], 剩余HP：["
									+ hpLeft + "]");
						} else if (1050 == apiResult.getError()) {
							// 自动吃BP药
							params.clear();
							params.put("item_id", "2");

							apiResult = apiHelper.call(
									"/connect/app/item/use?cyt=1", params);

							if (0 == apiResult.getError()) {
								print("吃BP药成功");
							} else {
								print("吃BP药失败：" + apiResult.getErrorMessage());
							}

						} else if (8000 == apiResult.getError()) {
							if ("现在无法进行战斗".equals(apiResult.getErrorMessage())) {
								// 如果只是无法战斗
								// do nothing
							} else {
								// 卡满自动卖
								cardSellService.autoSellCard();
							}

						} else if (9000 == apiResult.getError()) {
							// 被掉线了，可能是手机登陆游戏了
							rest(10 * 60 * 1000l);
						} else {
							print("没能成功战斗:" + apiResult.getErrorMessage());
						}
						rest();
					}
				}

				rest();
			}
		}
	}

	public List<Fairy> getFairyList() {
		// 遍历妖精
		String xmlResult = HttpClientHelper
				.post("http://game2-CBT.ma.sdo.com:10001/connect/app/menu/fairyselect?cyt=1",
						null);

		List<Fairy> bosses = new ArrayList<Fairy>();

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
						Fairy fairy = new Fairy();
						fairy.setSeriald(Integer.parseInt((element
								.element("fairy")).elementText("serial_id")));
						fairy.setUserId(Integer.parseInt((element
								.element("user")).elementText("id")));
						bosses.add(fairy);
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
		return bosses;
	}

	private void rest() {
		try {
			Thread.sleep(10000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void rest(long timemili) {
		try {
			Thread.sleep(timemili);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void print(final String msg) {
		System.out.println(msg);
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				browser.execute("appendBattleInfo(\"" + msg + "\")");
			}
		});
	}

}
