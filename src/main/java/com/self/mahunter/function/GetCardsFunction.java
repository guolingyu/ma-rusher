package com.self.mahunter.function;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONObject;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.self.mahunter.entity.Card;
import com.self.mahunter.service.UserStateService;
import com.self.mahunter.utils.JSONHelper;

public class GetCardsFunction extends BrowserFunction {

	private UserStateService userStateService = UserStateService.getInstance();

	public GetCardsFunction(Browser browser, String name) {
		super(browser, name);
	}

	public Object function(Object[] args) {
		List<Card> cards = userStateService.getUserCards();
		Collections.sort(cards, new CardCostComparator());
		int total = cards.size();

		JSONObject result = JSONHelper.buildSuccJSON();
		result.put("cards", cards);
		result.put("total", total);

		System.out.println(result);
		return result.toString();
	}

	private class CardCostComparator implements Comparator<Card> {

		public int compare(Card o1, Card o2) {
			if (null == o1 || null == o1.getCost()) {
				return -1;
			} else if (null == o2 || null == o2.getCost()) {
				return 1;
			}
			if (o1.getCost() > o2.getCost()) {
				return 1;
			} else if (o1.getCost() == o2.getCost()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
