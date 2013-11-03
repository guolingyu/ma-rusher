package com.self.mahunter.function;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.self.mahunter.entity.Card;
import com.self.mahunter.entity.CardData;
import com.self.mahunter.entity.CardDatabase;
import com.self.mahunter.entity.UserState;
import com.self.mahunter.service.CardDatabaseService;
import com.self.mahunter.service.UserStateService;
import com.self.mahunter.utils.HttpClientHelper;
import com.self.mahunter.utils.XMLHelper;

public class LoginFunction extends BrowserFunction {

	private UserStateService userStateService = UserStateService.getInstance();

	private CardDatabaseService cardDatabaseService = CardDatabaseService
			.getInstance();

	public LoginFunction(Browser browser, String name) {
		super(browser, name);
	}

	public Object function(Object[] args) {
		String name = (String) args[0];
		String password = (String) args[1];
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
					// 登陆成功
					JSONObject result = new JSONObject();
					result.put("success", 1);

					// 获取用户信息
					UserState userState = new UserState();
					userState.setName(XMLHelper.getSingleNodeAsString(document,
							"/response/header/your_data/name"));
					userState.setCurrentAP(Integer.parseInt(XMLHelper
							.getSingleNodeAsString(document,
									"/response/header/your_data/ap/current")));
					userState.setMaxAP(Integer.parseInt(XMLHelper
							.getSingleNodeAsString(document,
									"/response/header/your_data/ap/max")));
					userState.setCurrentBC(Integer.parseInt(XMLHelper
							.getSingleNodeAsString(document,
									"/response/header/your_data/bc/current")));
					userState.setMaxBC(Integer.parseInt(XMLHelper
							.getSingleNodeAsString(document,
									"/response/header/your_data/bc/max")));
					userState
							.setRefreshTime(Long.parseLong(XMLHelper
									.getSingleNodeAsString(document,
											"/response/header/your_data/ap/current_time")));

					userState.setUserId(XMLHelper.getSingleNodeAsString(
							document, "/response/body/login/user_id"));

					userStateService.setUserState(userState);

					// 获取卡牌列表
					List mycardsNode = document
							.selectNodes("/response/header/your_data/owner_card_list/user_card");
					CardDatabase cardDatabase = cardDatabaseService
							.getCardDatabase();
					List<Card> mycards = new ArrayList<Card>();
					for (Iterator iterator = mycardsNode.iterator(); iterator
							.hasNext();) {
						Element cardElement = (Element) iterator.next();
						Card card = new Card();
						card.setSeriald(Integer.parseInt(cardElement.element(
								"serial_id").getText()));
						card.setMasterCardId(Integer.parseInt(cardElement
								.element("master_card_id").getText()));
						card.setHp(Integer.parseInt(cardElement.element("hp")
								.getText()));
						card.setPower(Integer.parseInt(cardElement.element(
								"power").getText()));
						card.setLv(Integer.parseInt(cardElement.element("lv")
								.getText()));
						card.setLvMax(Integer.parseInt(cardElement.element(
								"lv_max").getText()));
						card.setPlusLimitCount(Integer.parseInt(cardElement
								.element("plus_limit_count").getText()));
						card.setLimitOver(Integer.parseInt(cardElement.element(
								"limit_over").getText()));

						CardData data = cardDatabase.getCardmap()
								.get(cardElement.element("master_card_id")
										.getText());

						if (null != data) {
							card.setName(data.getName());
							card.setImgUrl(data.getImgUrl());
							card.setCost(data.getCost());
							card.setStar(data.getStar());
						}

						mycards.add(card);
					}

					userStateService.setUserCards(mycards);

					System.out.println(userState);
					return result.toString();
				} else {
					JSONObject result = new JSONObject();
					result.put("success", 0);
					result.put(
							"error",
							document.selectSingleNode(
									"/response/header/error/message").getText());
					return result.toString();
				}
			} else {
				JSONObject result = new JSONObject();
				result.put("success", 0);
				result.put("error", "服务器返回结构无法解析");
				return result.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSONObject result = new JSONObject();
			result.put("success", 0);
			result.put("error", e.getMessage());
			return result.toString();
		}
	}
}
