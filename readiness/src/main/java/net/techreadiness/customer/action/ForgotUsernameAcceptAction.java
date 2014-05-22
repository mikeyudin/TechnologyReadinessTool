package net.techreadiness.customer.action;

import java.util.List;

import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({
		@Result(name = Action.SUCCESS, type = "redirectAction", params = { "actionName", "forgot-username", "success",
				"true" }), @Result(name = Action.ERROR, type = "lastAction", params = { "actionName", "forgot-username" }) })
public class ForgotUsernameAcceptAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private String email;

	@Override
	public String execute() {

		if (!userService.isEmailInUse(getServiceContext(), email)) {
			addFieldError("email", "This email is not in use.");
		} else {
			try {
				userService.forgotUsername(getServiceContext(), email);
			} catch (ValidationServiceException e) {
				List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
				for (ValidationError validationError : errors) {
					addFieldError(validationError.getFieldName(), validationError.getOnlineMessage());
				}
			} catch (ServiceException e) {
				addActionError(e.getMessage());
			}
		}

		if (hasErrors()) {
			return ERROR;
		}

		return SUCCESS;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

}