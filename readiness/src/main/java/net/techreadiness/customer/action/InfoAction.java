package net.techreadiness.customer.action;

import java.util.Set;

import net.techreadiness.navigation.Tab;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Sets;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.Container;

@Results({
		@Result(name = "success", type = "redirectAction", params = { "actionName", "${home.defaultAction}", "namespace",
				"${home.namespce}" }), @Result(name = "default", location = "/info.jsp") })
public class InfoAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private Tab home;

	@Override
	public String execute() {
		Container container = ActionContext.getContext().getContainer();
		Set<String> names = container.getInstanceNames(Tab.class);
		Set<Tab> tabs = Sets.newTreeSet();
		for (String name : names) {
			Tab tab = container.getInstance(Tab.class, name).clone();
			// only display a tab if it has children
			if (tab.isDisplayIfEmpty() || !tab.isEmpty()) {
				tabs.add(tab);
			}
		}

		for (Tab tab : tabs) {
			if (tab.getCode().equals("home")) {
				home = tab;
				break;
			}
		}

		if (home.getDefaultAction().equals("info") && home.getNamespace().equals("/")) {
			return "default";
		}

		return SUCCESS;
	}

	public Tab getHome() {
		return home;
	}
}
