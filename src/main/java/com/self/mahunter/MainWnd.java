package com.self.mahunter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.self.mahunter.function.GetCardsFunction;
import com.self.mahunter.function.LoginFunction;
import com.self.mahunter.function.StartBattleFunction;
import com.self.mahunter.function.StopBattleFunction;
import com.self.mahunter.service.BattleService;

public class MainWnd {

	public static final String appName = "MA猎人 1.0";

	private static final String PATH_TO_WEBAPP = "/webapp";

	private Shell shell;
	private Display display;
	private Browser browser;

	public void start() {
		display = new Display();
		shell = new Shell(display);
		shell.setSize(1100, 700);

		shell.setText(appName);
		shell.setLayout(new FillLayout());

		String host = MainWnd.class.getResource(PATH_TO_WEBAPP).toString();
		System.out.println(host);

		try {
			browser = new Browser(shell, SWT.BORDER);
			browser.addMouseListener(new MouseListener() {

				public void mouseDoubleClick(MouseEvent arg0) {
				}

				public void mouseDown(MouseEvent event) {
					if (event.button == 3)
						browser.execute("document.oncontextmenu = function() {return false;}");
				}

				public void mouseUp(MouseEvent arg0) {
				}
			});
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: "
					+ e.getMessage());
			display.dispose();
			return;
		}

		String url = host + "/sign_in.html";
		browser.setUrl(url);

		final BrowserFunction function = new LoginFunction(browser, "login");
		final BrowserFunction function2 = new GetCardsFunction(browser,
				"getMyCards");
		final BrowserFunction function3 = new StartBattleFunction(browser,
				"startBattle");
		final BrowserFunction function4 = new StopBattleFunction(browser,
				"stopBattle");

		BattleService.getInstance().setBrowser(browser);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public static void main(String args[]) {
		MainWnd mainWnd = new MainWnd();
		mainWnd.start();
	}
}
