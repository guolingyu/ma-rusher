package com.self.mahunter.function;

import net.sf.json.JSONObject;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.self.mahunter.service.CardSellService;
import com.self.mahunter.service.UserStateService;
import com.self.mahunter.utils.MAApiResult;

public class LoginFunction extends BrowserFunction {

	private UserStateService userStateService = UserStateService.getInstance();
	
	private CardSellService cardSellService = CardSellService.getInstance();

	public LoginFunction(Browser browser, String name) {
		super(browser, name);
	}

	public Object function(Object[] args) {
		String loginName = (String) args[0];
		String password = (String) args[1];
		int server = 2;

		MAApiResult apiResult = userStateService.login(loginName, password,
				server);
		if (apiResult.getError() == 0) {
			JSONObject result = new JSONObject();
			result.put("success", 1);
			return result.toString();
		} else {
			JSONObject result = new JSONObject();
			result.put("success", 0);
			result.put("errorMessage", apiResult.getErrorMessage());
			return result.toString();
		}
	}
}
