package net.techreadiness.customer.action.user;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ACCESS;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_DELETE;
import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_UPDATE;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.navigation.taskflow.user.UserTaskFlowDefinition;
import net.techreadiness.persistence.datagrid.UserByScopeItemProvider;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Role;
import net.techreadiness.service.object.Scope;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.filters.FilterSelectionHandler;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.BaseTaskControlAction;
import net.techreadiness.ui.util.Conversation;
import net.techreadiness.ui.util.ConversationAware;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Results({ @Result(name = "success", location = "/user/list.jsp") })
public class ListAction extends BaseTaskControlAction<UserTaskFlowDefinition> implements ConversationAware,
		ApplicationContextAware {
	private static final long serialVersionUID = 1L;

	@Inject
	private UserByScopeItemProvider itemProvider;
	@Inject
	private ConfigService configService;
	@Inject
	private ScopeService scopeService;

	private ViewDef viewDef;
	private Conversation conversation;
	private ApplicationContext applicationContext;

	@ConversationScoped
	private DataGridState<User> userGrid;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_ACCESS })
	public String execute() {

		Scope scope = scopeService.getScopeWithUsers(getServiceContext());
		itemProvider.setScope(scope);
		itemProvider.setServiceContext(getServiceContext());

		conversation.put("orgFilterSelectionHandler", "orgFilterSelectionHandlerForUser");
		conversation.put("roleFilterSelectionHandler", "roleFilterSelectionHandlerForUser");

		viewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.USER_DATAGRID);
		return SUCCESS;
	}

	public UserByScopeItemProvider getItemProvider() {
		return itemProvider;
	}

	public void setUserGrid(DataGridState<User> userGrid) {
		this.userGrid = userGrid;
	}

	public DataGridState<User> getUserGrid() {
		return userGrid;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public Boolean getInlineEditable() {
		return hasPermission(CORE_CUSTOMER_USER_UPDATE);
	}

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
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

	public boolean isShowDeletedUsersCheckboxDisplayed() {
		return hasPermission(CORE_CUSTOMER_USER_DELETE);
	}
}
