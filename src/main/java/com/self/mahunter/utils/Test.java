package com.self.mahunter.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.self.mahunter.entity.CardData;
import com.self.mahunter.entity.PVPUser;
import com.self.mahunter.service.CardDatabaseService;

public class Test {

	private static final int event_id = 48;

	private static final int card_cost = 77;

	private static final int MAX_BC_COST = 30;

	public static void main(String[] args) throws UnsupportedEncodingException,
			DocumentException, InterruptedException {
		String name = "15811202808";
		String password = "4fun4game";
		System.out.println(String.format("Login as %1s:%2s", name, password));

		MAApiHelper apiHelper = new MAApiHelper(2);
		apiHelper.connect();

		Map<String, String> params = new HashMap<String, String>();
		params.put("login_id", name);
		params.put("password", password);

		MAApiResult apiResult = apiHelper.call("/connect/app/login?cyt=1",
				params);

		params.clear();
		params.put("event_id", "" + event_id);
		params.put("move", "0");

		apiResult = apiHelper.call("/connect/app/battle/battle_userlist?cyt=1",
				params);

		if (apiResult.getError() != 0) {
			return;
		}

		int bcItemCount = 0;
		while (true) {

			List nodes = apiResult.getData().selectNodes(
					"/response/body/battle_userlist/user_list/user");

			List<PVPUser> users = new ArrayList<PVPUser>();

			CardDatabaseService database = CardDatabaseService.getInstance();

			for (int i = 0; i < nodes.size(); i++) {
				Element element = (Element) nodes.get(i);

				PVPUser user = new PVPUser();
				user.setUserId(Integer
						.parseInt(element.element("id").getText()));
				user.setName(element.element("name").getText());
				user.setCost(Integer
						.parseInt(element.element("cost").getText()));
				user.setRank(Integer
						.parseInt(element.element("rank").getText()));

				Element mcElem = element.element("leader_card");
				user.setLeadCardMasterId(Integer.parseInt(mcElem.element(
						"master_card_id").getText()));
				user.setLeaderCardHp(Integer.parseInt(mcElem.element("hp")
						.getText()));
				user.setLeaderCardLv(Integer.parseInt(mcElem.element("lv")
						.getText()));
				CardData carddata = database.getCardData(Integer.toString(user
						.getLeadCardMasterId()));
				user.setLeaderCardCost(carddata.getCost());
				user.setLeaderCardName(carddata.getName());
				user.setLeaderCardStar(carddata.getStar());

				/**
				 * 如果用户的排名大于10000，且头像卡牌的cost不超过20，则进行PK
				 * 
				 */
				if (user.getRank() > 10000 && user.getLeaderCardCost() <= 20) {
					users.add(user);
				}

			}

			for (int i = 0; i < users.size(); i++) {
				PVPUser user = users.get(i);
				System.out.println("准备同玩家战斗：" + user);
				params.clear();
				params.put("event_id", "" + event_id);
				params.put("user_id", Integer.toString(user.getUserId()));
				params.put("battle_type", "0");

				apiResult = apiHelper.call("/connect/app/battle/battle?cyt=1",
						params);

				if (apiResult.getError() == 0) {
					int win = Integer.parseInt(apiResult
							.getData()
							.selectSingleNode(
									"/response/body/battle_result/winner")
							.getText());
					if (win == 1) {
						System.out.println("挑战成功");
					} else {
						System.out.println("挑战失败");
					}

					int bc = Integer.parseInt(apiResult
							.getData()
							.selectSingleNode(
									"/response/header/your_data/bc/current")
							.getText());

					if (bc < card_cost) {
						if (bcItemCount >= MAX_BC_COST) {
							return;
						}
						params.clear();
						params.put("item_id", "2");

						apiResult = apiHelper.call(
								"/connect/app/item/use?cyt=1", params);

						if (0 == apiResult.getError()) {
							bcItemCount++;
							System.out.println("吃BP药成功");
						} else {
							System.out.println("吃BP药失败："
									+ apiResult.getErrorMessage());
						}
					}
				} else {
					System.out.println("对战无法进行:" + apiResult.getErrorMessage());
				}

				Thread.sleep(30 * 1000l);
			}
		}

	}
}
