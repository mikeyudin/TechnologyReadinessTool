package net.techreadiness.customer.action.user;

import java.util.List;

import javax.inject.Inject;

import net.techreadiness.navigation.taskflow.youraccount.YourAccountTaskFlowData;
import net.techreadiness.navigation.taskflow.youraccount.YourAccountTaskFlowDefinition;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowState;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.collect.Lists;

public class YouraccountAction extends AbstractUserAction {
	private static final long serialVersionUID = 1L;

	@Inject
	protected YourAccountTaskFlowDefinition yourAccountTaskFlow;
	@Inject
	protected YourAccountTaskFlowData yourAccountTaskFlowData;

	@Override
	@Action(results = { @Result(name = "success", location = "/user/yourAccount.jsp") })
	public String execute() {
		setUser(userService.getByUsername(getServiceContext(), SecurityContextHolder.getContext().getAuthentication()
				.getName()));
		setUserId(user.getUserId());
		setViewDef(configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.USER));
		return SUCCESS;
	}

	@Action(value = "youraccountEditAccount", results = { @Result(name = "success", type = "redirect", location = "/task/youraccount/update/edit") })
	public String youraccountEditAccount() throws ServiceException {
		return routeToTask(YourAccountTaskFlowDefinition.TASK_EDIT_USERS);
	}

	@Action(value = "youraccountChangePassword", results = { @Result(name = "success", type = "redirect", location = "/task/youraccount/update/password") })
	public String youraccountChangePassword() throws ServiceException {
		return routeToTask(YourAccountTaskFlowDefinition.TASK_CHANGE_PASSWORD);
	}

	@Override
	protected String routeToTask(String taskName) {
		List<User> userList = Lists.newArrayList();

		userList.add(userService.getById(getServiceContext(), getUserId()));

		Task task = yourAccountTaskFlow.getTask(taskName);
		TaskFlowState state = new TaskFlowState();
		state.setTasks(Lists.newArrayList(task));
		yourAccountTaskFlowData.setTaskFlowState(state);
		yourAccountTaskFlowData.setStandardInvocation(false);
		yourAccountTaskFlowData.setUsers(userList);

		return SUCCESS;
	}
}
