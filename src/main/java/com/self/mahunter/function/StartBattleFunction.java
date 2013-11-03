package com.self.mahunter.function;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.self.mahunter.service.BattleService;

public class StartBattleFunction extends BrowserFunction {

	private BattleService battleService = BattleService.getInstance();

	public StartBattleFunction(Browser browser, String name) {
		super(browser, name);
	}

	public Object function(Object[] args) {
		battleService.start();
		return null;
	}

}
