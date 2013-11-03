package com.self.mahunter.function;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.self.mahunter.service.BattleService;

public class StopBattleFunction extends BrowserFunction {

	private BattleService battleService = BattleService.getInstance();

	public StopBattleFunction(Browser browser, String name) {
		super(browser, name);
	}

	public Object function(Object[] args) {
		battleService.stop();
		return null;
	}

}
