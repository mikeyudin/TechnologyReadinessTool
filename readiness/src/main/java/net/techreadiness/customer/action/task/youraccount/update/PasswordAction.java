package net.techreadiness.customer.action.task.youraccount.update;

import java.util.Arrays;
import java.util.Collection;

import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.task.youraccount.YourAccountTaskFlowAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, location = "/task/youraccount/change-password.jsp") })
public class PasswordAction extends YourAccountTaskFlowAction {
	private static final long serialVersionUID = 1L;

	private Collection<User> users;

	@Override
	public String execute() {
		users = Arrays.asList(getServiceContext().getUser());
		return SUCCESS;
	}

	public Collection<User> getUsers() {
		return users;
	}
}