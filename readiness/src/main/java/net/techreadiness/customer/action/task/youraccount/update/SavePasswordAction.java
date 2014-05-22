package net.techreadiness.customer.action.task.youraccount.update;

import java.util.List;

import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.ui.action.task.youraccount.YourAccountTaskFlowAction;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, type = "redirect", location = "/task/youraccount/update/password"),
		@Result(name = Action.ERROR, type = "lastAction", params = { "actionName", "password" }) })
public class SavePasswordAction extends YourAccountTaskFlowAction {

	private static final long serialVersionUID = 1L;

	private String password;
	private String confirmPassword;

	@Override
	public String execute() {
		if (StringUtils.isEmpty(password)) {
			addFieldError("password", getText("change.password.required"));
		}

		if (StringUtils.isEmpty(confirmPassword)) {
			addFieldError("confirmPassword", getText("change.confirm.password.required"));
		}
		try {
			userService.changePassword(getServiceContext(), getServiceContext().getUserName(), password, confirmPassword);
		} catch (ValidationServiceException e) {
			List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
			for (ValidationError validationError : errors) {
				addActionError(validationError.getOnlineMessage());
			}
		} catch (ServiceException se) {
			addActionError("Problem updating password");
		}
		if (hasErrors()) {
			return ERROR;
		}
		return SUCCESS;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswor() {
		return password;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

}
