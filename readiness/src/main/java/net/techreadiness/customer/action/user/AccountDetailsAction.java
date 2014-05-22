package net.techreadiness.customer.action.user;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.navigation.taskflow.youraccount.YourAccountTaskFlowData;
import net.techreadiness.navigation.taskflow.youraccount.YourAccountTaskFlowDefinition;
import net.techreadiness.persistence.datagrid.OrgByUserItemProvider;
import net.techreadiness.persistence.datagrid.RoleByUserItemProvider;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Role;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.Preparable;

@Results({ @Result(name = "success", location = "/user/your-account.jsp") })
public class AccountDetailsAction extends BaseAction implements Preparable {

	private static final long serialVersionUID = 1L;
	private User user;
	private ViewDef viewDef;
	@Inject
	private ConfigService configService;
	@Inject
	private RoleByUserItemProvider roleByUserItemProvider;
	@Inject
	private OrgByUserItemProvider orgByUserItemProvider;
	@Inject
	private YourAccountTaskFlowDefinition yourAccountTaskFlow;
	@Inject
	private YourAccountTaskFlowData yourAccountTaskFlowData;
	@ConversationScoped
	private DataGridState<Role> roleDataGridState;
	@ConversationScoped
	private DataGridState<Org> orgDataGridState;

	@Override
	public String execute() throws Exception {
		user = userService.getByUsername(getServiceContext(), SecurityContextHolder.getContext().getAuthentication()
				.getName());

		roleByUserItemProvider.setScopeId(getServiceContext().getScopeId());
		roleByUserItemProvider.setUserId(user.getUserId());

		orgByUserItemProvider.setScopeId(getServiceContext().getScopeId());
		orgByUserItemProvider.setUserId(user.getUserId());

		viewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.USER);
		return SUCCESS;
	}

	@Action(value = "edit-account", results = { @Result(name = "success", type = "redirectAction", params = { "namespace",
			"/task/youraccount/update", "actionName", "edit" }) })
	public String youraccountEditAccount() throws ServiceException {
		return routeToTask(YourAccountTaskFlowDefinition.TASK_EDIT_USERS);
	}

	@Action(value = "change-password", results = { @Result(name = "success", type = "redirectAction", params = {
			"namespace", "/task/youraccount/update", "actionName", "password" }) })
	public String youraccountChangePassword() throws ServiceException {
		return routeToTask(YourAccountTaskFlowDefinition.TASK_CHANGE_PASSWORD);
	}

	private String routeToTask(String taskName) {
		List<User> userList = Lists.newArrayList();
		userList.add(user);
		Task task = yourAccountTaskFlow.getTask(taskName);
		TaskFlowState state = new TaskFlowState();
		state.setTasks(Lists.newArrayList(task));
		yourAccountTaskFlowData.setTaskFlowState(state);
		yourAccountTaskFlowData.setStandardInvocation(false);
		yourAccountTaskFlowData.setUsers(userList);

		return SUCCESS;
	}

	public User getUser() {
		return user;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public RoleByUserItemProvider getRoleByUserItemProvider() {
		return roleByUserItemProvider;
	}

	public OrgByUserItemProvider getOrgByUserItemProvider() {
		return orgByUserItemProvider;
	}

	public DataGridState<Role> getRoleDataGridState() {
		return roleDataGridState;
	}

	public DataGridState<Org> getOrgDataGridState() {
		return orgDataGridState;
	}

	@Override
	public void prepare() throws Exception {
		user = userService.getByUsername(getServiceContext(), SecurityContextHolder.getContext().getAuthentication()
				.getName());
	}
}
