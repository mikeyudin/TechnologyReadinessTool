package net.techreadiness.ui.tags.navigation;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.techreadiness.navigation.Group;
import net.techreadiness.navigation.SubTab;
import net.techreadiness.navigation.Tab;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.UserService;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.tags.BaseTag;
import net.techreadiness.ui.util.ContextUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.Container;

public class NavigationBarTag extends BaseTag {
	private Tab selectedMainTab;
	private SubTab selectedSubTab;
	private Set<Tab> tabs;

	@Override
	public String execute() throws Exception {
		ActionContext actionContext = ActionContext.getContext();
		Container strutsContainer = actionContext.getContainer();
		tabs = Sets.newTreeSet();
		UserService userService = ContextUtils.getRequiredBeanOfType(UserService.class);
		Set<String> names = strutsContainer.getInstanceNames(Tab.class);

		for (String name : names) {
			Tab tab = strutsContainer.getInstance(Tab.class, name).clone();
			// only display a tab if it has children
			if (tab.isDisplayIfEmpty() || !tab.isEmpty()) {
				tabs.add(tab);
			}
		}

		Iterator<Tab> tabIterator = tabs.iterator();

		for (Tab tab : tabs) {
			for (Group group : tab.getGroups()) {
				tabIterator = Iterators.concat(tabIterator, group.getChildren().iterator());
			}
		}
		HttpServletRequest request = (HttpServletRequest) getPageContext().getRequest();
		String servletPath = request.getServletPath();
		int bestMatch = -1;
		boolean exact = false;
		while (tabIterator.hasNext()) {
			Tab tab = tabIterator.next();
			if (ArrayUtils.isEmpty(tab.getPermissionCodes())
					|| userService.hasPermission(
							(ServiceContext) actionContext.getSession().get(BaseAction.SERVICE_CONTEXT),
							tab.getPermissionCodes())) {
				String tabPath = tab.getNamespace() + tab.getDefaultAction();
				int diff = StringUtils.indexOfDifference(tabPath, servletPath);
				if (diff > bestMatch && !exact) {
					bestMatch = diff;
					selectTab(tab);
				} else if (diff == -1) {
					selectTab(tab);
					exact = true;
				}
			} else {
				tabIterator.remove();
			}

		}

		return "/navigation/bar.jsp";
	}

	private void selectTab(Tab tab) {
		if (tab instanceof SubTab) {
			SubTab subTab = (SubTab) tab;
			selectedSubTab = subTab;
			selectedMainTab = subTab.getGroup().getParent();
		} else {
			selectedMainTab = tab;
		}
	}

	public Tab getSelectedMainTab() {
		return selectedMainTab;
	}

	public void setSelectedMainTab(Tab selectedMainTab) {
		this.selectedMainTab = selectedMainTab;
	}

	public SubTab getSelectedSubTab() {
		return selectedSubTab;
	}

	public void setSelectedSubTab(SubTab selectedSubTab) {
		this.selectedSubTab = selectedSubTab;
	}

	public Set<Tab> getMainTabs() {
		return tabs;
	}
}
