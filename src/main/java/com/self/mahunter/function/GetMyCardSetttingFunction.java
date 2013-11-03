package com.self.mahunter.function;

import net.sf.json.JSONObject;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.self.mahunter.entity.CardGroupSetting;
import com.self.mahunter.service.CardGroupSettingService;

public class GetMyCardSetttingFunction extends BrowserFunction {

	public GetMyCardSetttingFunction(Browser browser, String name) {
		super(browser, name);
	}

	public Object function(Object[] args) {
		String groupId = (String) args[0];
		CardGroupSetting setting = CardGroupSettingService.getInstance().get(
				groupId);
		JSONObject result = new JSONObject();
		if (null == setting) {
			result.put("result", 0);
			return result.toString();
		} else {
			result.put("result", 1);
			result.put("cards", setting.getCards());
			return result.toString();
		}

	}
}
