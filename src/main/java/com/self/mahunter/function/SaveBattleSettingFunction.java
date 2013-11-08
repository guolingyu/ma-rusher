package com.self.mahunter.function;

import net.sf.json.JSONObject;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.self.mahunter.entity.BattleRule;
import com.self.mahunter.service.BattleIntelligentService;

public class SaveBattleSettingFunction extends BrowserFunction {

	private BattleIntelligentService battleIntelligentService = BattleIntelligentService
			.getInstance();

	public SaveBattleSettingFunction(Browser browser, String name) {
		super(browser, name);
	}

	public Object function(Object[] args) {
		BattleRule rule = new BattleRule();
		if (null != args[0]) {
			Object[] cards = (Object[]) args[0];
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < cards.length; i++) {
				sb.append(cards[i]);
				if (i < cards.length - 1) {
					sb.append(",");
				}
			}
			rule.setCards(sb.toString());
		}
		rule.setMinHp(null == args[1] ? null : Integer
				.parseInt((String) args[1]));
		rule.setMaxHp(null == args[2] ? null : Integer
				.parseInt((String) args[2]));
		rule.setMinLv(null == args[3] ? null : Integer
				.parseInt((String) args[3]));
		rule.setMaxLv(null == args[4] ? null : Integer
				.parseInt((String) args[4]));

		battleIntelligentService.addRule(rule);
		JSONObject result = new JSONObject();
		result.put("result", 0);
		return result.toString();
	}

}
