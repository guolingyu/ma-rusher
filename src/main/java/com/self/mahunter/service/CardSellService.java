package com.self.mahunter.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.self.mahunter.entity.AutoSellExcludeCard;
import com.self.mahunter.entity.Card;
import com.self.mahunter.entity.UserState;
import com.self.mahunter.utils.MAApiHelper;
import com.self.mahunter.utils.MAApiResult;

public class CardSellService {

	private UserStateService userStateService = UserStateService.getInstance();

	private List<AutoSellExcludeCard> autoSellExcludes = new ArrayList<AutoSellExcludeCard>();

	private static CardSellService instance = new CardSellService();

	public static CardSellService getInstance() {
		return instance;
	}

	private Set<Integer> excludes = new HashSet<Integer>();

	private CardSellService() {
		String filePath = System.getProperty("user.dir")
				+ "/data/autosell-exclude.json";
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}
		try {
			String json = FileUtils.readFileToString(file);
			if (StringUtils.isBlank(json)) {
				return;
			}
			JSONObject ob = JSONObject.fromObject(json);
			if (null != ob.getJSONArray("exclude")) {
				autoSellExcludes = JSONArray.toList(ob.getJSONArray("exclude"),
						AutoSellExcludeCard.class);
			}
			for (int i = 0; i < autoSellExcludes.size(); i++) {
				excludes.add(autoSellExcludes.get(i).getMastercardid());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	public void autoSellCard() {
		userStateService.refreshUserStatus();
		List<Card> cards = userStateService.getUserCards();
		MAApiHelper apiHelper = userStateService.getApiHelper();
		Set<Card> toSellCards = new HashSet<Card>();

		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			if (card.getStar() < 5
					&& !excludes.contains(card.getMasterCardId())) {

				Map<String, String> params = new HashMap<String, String>();
				params.put("serial_id", "" + card.getSeriald());
				MAApiResult apiResult = apiHelper.call(
						"/connect/app/trunk/sell?cyt=1", params);
				if (1010 == apiResult.getError()) {
					toSellCards.add(card);
					System.out.println("自动售卖卡牌：" + card);
				} else {
					System.out.println("自动售卖卡牌失败："
							+ apiResult.getErrorMessage());
				}
			}
		}
		userStateService.getUserCards().removeAll(toSellCards);
	}
}
