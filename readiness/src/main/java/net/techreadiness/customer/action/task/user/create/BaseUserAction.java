package net.techreadiness.customer.action.task.user.create;

import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.service.UserService;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Role;
import net.techreadiness.ui.action.filters.FilterSelectionHandler;
import net.techreadiness.ui.action.task.user.UserTaskFlowAction;
import net.techreadiness.ui.util.ConversationAware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;

public class BaseUserAction extends UserTaskFlowAction implements Preparable, ApplicationContextAware {
	private static final long serialVersionUID = 1L;

	protected Map<String, String> userMap = null;
	@Inject
	protected UserService userService;
	private ApplicationContext applicationContext;

	@Override
	public void prepare() throws Exception {
		if (userMap == null) {
			userMap = Maps.newHashMap();
		}
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}

	protected FilterSelectionHandler<Role> getRoleSelectionHandler() {
		String beanName = conversation.get(String.class, "roleFilterSelectionHandler");
		FilterSelectionHandler<Role> selectionHandler = applicationContext.getBean(beanName, FilterSelectionHandler.class);
		if (selectionHandler instanceof ConversationAware) {
			((ConversationAware) selectionHandler).setConversation(conversation);
		}
		return selectionHandler;
	}

	protected FilterSelectionHandler<Org> getOrgSelectionHandler() {
		String beanName = conversation.get(String.class, "orgFilterSelectionHandler");
		FilterSelectionHandler<Org> selectionHandler = applicationContext.getBean(beanName, FilterSelectionHandler.class);
		if (selectionHandler instanceof ConversationAware) {
			((ConversationAware) selectionHandler).setConversation(conversation);
		}
		return selectionHandler;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}
}