package com.self.mahunter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.Node;

import com.self.mahunter.entity.Card;
import com.self.mahunter.entity.CardData;
import com.self.mahunter.entity.CardDatabase;
import com.self.mahunter.entity.UserState;
import com.self.mahunter.utils.MAApiHelper;
import com.self.mahunter.utils.MAApiResult;

public class UserStateService {

	private static UserStateService instance = new UserStateService();

	private MAApiHelper apiHelper;

	private UserStateService() {
	};

	public static UserStateService getInstance() {
		return instance;
	}

	private UserState userState;

	private List<Card> userCards;

	private CardDatabaseService cardDatabaseService = CardDatabaseService
			.getInstance();

	public UserState getUserState() {
		return userState;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
	}

	public void setUserCards(List<Card> userCards) {
		this.userCards = userCards;
	}

	public Card getUserCard(int cardid) {
		for (int i = 0; i < userCards.size(); i++) {
			if (userCards.get(i).getSeriald() == cardid) {
				return userCards.get(i);
			}
		}
		return null;
	}

	public MAApiResult login(String loginName, String password, int server) {
		System.out.println(String.format("Login as %1s:%2s", loginName,
				password));

		setApiHelper(new MAApiHelper(server));

		getApiHelper().connect();

		Map<String, String> params = new HashMap<String, String>();
		params.put("login_id", loginName);
		params.put("password", password);

		MAApiResult apiResult = getApiHelper().call("/connect/app/login?cyt=1",
				params);

		if (0 == apiResult.getError()) {

			// 获取用户信息
			userState = new UserState();
			userState.setLoginName(loginName);
			userState.setPassword(password);
			userState.setServer(server);
			userState.setName(apiResult
					.query("/response/header/your_data/name"));
			userState.setCurrentAP(Integer.parseInt(apiResult
					.query("/response/header/your_data/ap/current")));
			userState.setMaxAP(Integer.parseInt(apiResult
					.query("/response/header/your_data/ap/max")));
			userState.setCurrentBC(Integer.parseInt(apiResult
					.query("/response/header/your_data/bc/current")));
			userState.setMaxBC(Integer.parseInt(apiResult
					.query("/response/header/your_data/bc/max")));
			userState.setRefreshTime(Long.parseLong(apiResult
					.query("/response/header/your_data/ap/current_time")));
			userState
					.setUserId(apiResult.query("/response/body/login/user_id"));

			// 获取卡牌列表
			List<Node> mycardsNode = apiResult
					.queryList("/response/header/your_data/owner_card_list/user_card");
			CardDatabase cardDatabase = cardDatabaseService.getCardDatabase();
			userCards = new ArrayList<Card>();
			for (Iterator<Node> iterator = mycardsNode.iterator(); iterator
					.hasNext();) {
				Element cardElement = (Element) iterator.next();
				Card card = new Card();
				card.setSeriald(Integer.parseInt(cardElement.element(
						"serial_id").getText()));
				card.setMasterCardId(Integer.parseInt(cardElement.element(
						"master_card_id").getText()));
				card.setHp(Integer
						.parseInt(cardElement.element("hp").getText()));
				card.setPower(Integer.parseInt(cardElement.element("power")
						.getText()));
				card.setLv(Integer
						.parseInt(cardElement.element("lv").getText()));
				card.setLvMax(Integer.parseInt(cardElement.element("lv_max")
						.getText()));
				card.setPlusLimitCount(Integer.parseInt(cardElement.element(
						"plus_limit_count").getText()));
				card.setLimitOver(Integer.parseInt(cardElement.element(
						"limit_over").getText()));

				CardData data = cardDatabase.getCardmap().get(
						cardElement.element("master_card_id").getText());

				if (null != data) {
					card.setName(data.getName());
					card.setImgUrl(data.getImgUrl());
					card.setCost(data.getCost());
					card.setStar(data.getStar());
				}

				userCards.add(card);
			}
		}
		return apiResult;
	}

	public List<Card> getUserCards() {
		return userCards;
	}
	
	public MAApiHelper getApiHelper() {
		return apiHelper;
	}

	public void setApiHelper(MAApiHelper apiHelper) {
		this.apiHelper = apiHelper;
	}

	public boolean reconnect() {
		int server = userState.getServer();
		setApiHelper(new MAApiHelper(server));

		getApiHelper().connect();
		return true;
	}

	public boolean refreshUserStatus() {
		int server = userState.getServer();
		setApiHelper(new MAApiHelper(server));

		Map<String, String> params = new HashMap<String, String>();
		params.put("login_id", userState.getLoginName());
		params.put("password", userState.getPassword());

		MAApiResult apiResult = getApiHelper().call("/connect/app/login?cyt=1",
				params);

		if (0 == apiResult.getError()) {

			// 获取用户信息
			userState = new UserState();
			userState.setLoginName(userState.getLoginName());
			userState.setPassword(userState.getPassword());
			userState.setServer(server);
			userState.setName(apiResult
					.query("/response/header/your_data/name"));
			userState.setCurrentAP(Integer.parseInt(apiResult
					.query("/response/header/your_data/ap/current")));
			userState.setMaxAP(Integer.parseInt(apiResult
					.query("/response/header/your_data/ap/max")));
			userState.setCurrentBC(Integer.parseInt(apiResult
					.query("/response/header/your_data/bc/current")));
			userState.setMaxBC(Integer.parseInt(apiResult
					.query("/response/header/your_data/bc/max")));
			userState.setRefreshTime(Long.parseLong(apiResult
					.query("/response/header/your_data/ap/current_time")));
			userState
					.setUserId(apiResult.query("/response/body/login/user_id"));

			// 获取卡牌列表
			List<Node> mycardsNode = apiResult
					.queryList("/response/header/your_data/owner_card_list/user_card");
			CardDatabase cardDatabase = cardDatabaseService.getCardDatabase();
			userCards = new ArrayList<Card>();
			for (Iterator<Node> iterator = mycardsNode.iterator(); iterator
					.hasNext();) {
				Element cardElement = (Element) iterator.next();
				Card card = new Card();
				card.setSeriald(Integer.parseInt(cardElement.element(
						"serial_id").getText()));
				card.setMasterCardId(Integer.parseInt(cardElement.element(
						"master_card_id").getText()));
				card.setHp(Integer
						.parseInt(cardElement.element("hp").getText()));
				card.setPower(Integer.parseInt(cardElement.element("power")
						.getText()));
				card.setLv(Integer
						.parseInt(cardElement.element("lv").getText()));
				card.setLvMax(Integer.parseInt(cardElement.element("lv_max")
						.getText()));
				card.setPlusLimitCount(Integer.parseInt(cardElement.element(
						"plus_limit_count").getText()));
				card.setLimitOver(Integer.parseInt(cardElement.element(
						"limit_over").getText()));

				CardData data = cardDatabase.getCardmap().get(
						cardElement.element("master_card_id").getText());

				if (null != data) {
					card.setName(data.getName());
					card.setImgUrl(data.getImgUrl());
					card.setCost(data.getCost());
					card.setStar(data.getStar());
				}

				userCards.add(card);
			}
			return true;
		} else {
			return false;
		}
	}

}
