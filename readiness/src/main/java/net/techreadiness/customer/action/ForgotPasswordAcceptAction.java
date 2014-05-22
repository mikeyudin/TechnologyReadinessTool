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
	@Result(name = Action.SUCCESS, type = "redirectAction", params = { "actionName", "forgot-password", "success",
	"true" }), @Result(name = "invalid", type = "lastAction", params = { "actionName", "forgot-password" }) })
public class ForgotPasswordAcceptAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private String email;
	private String username;

	@Override
	public String execute() {

		if (!userService.isUsernameEmailPairValid(getServiceContext(), username, email)) {
			addFieldError("email", "This username/email combination is not valid.");
		} else {
			try {
				userService.resetPassword(getServiceContext(), userService.getByUsername(getServiceContext(), username)
						.getUserId());
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
			return "invalid";
		}

		return SUCCESS;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
