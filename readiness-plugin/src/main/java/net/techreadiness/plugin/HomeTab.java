package net.techreadiness.plugin;

import net.techreadiness.navigation.DefaultTab;

public class HomeTab extends DefaultTab {

	public HomeTab() {
		super("home", "tab.home.title", "/", "readiness-index", "tab.home.description", Integer.valueOf(0), true);
	}

}
