package net.techreadiness.customer.action;

import java.util.List;

import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, type = "redirectAction", params = { "actionName", "select-scope" }),
		@Result(name = Action.ERROR, type = "lastAction", params = { "actionName", "change-password" }) })
public class ChangePasswordAcceptAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private String passwordField;
	private String confirmPasswordField;
	private String token;
	private String username;

	@Override
	public String execute() {
		if (!userService.isTokenValid(getServiceContext(), username, token)) {
			addFieldError("token", "This username/token combination is not valid.");
		} else {
			try {
				userService.changePassword(getServiceContext(), username, passwordField, confirmPasswordField);
				userService.clearTokens(getServiceContext(), username);
			} catch (ValidationServiceException e) {
				List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
				for (ValidationError validationError : errors) {
					addFieldError(validationError.getFieldName(), validationError.getOnlineMessage());
				}
			}

		}

		if (hasErrors()) {
			return Action.ERROR;
		}

		return SUCCESS;
	}

	public void setPasswordField(String passwordField) {
		this.passwordField = passwordField;
	}

	public String getPasswordField() {
		return passwordField;
	}

	public void setConfirmPasswordField(String confirmPasswordField) {
		this.confirmPasswordField = confirmPasswordField;
	}

	public String getConfirmPasswordField() {
		return confirmPasswordField;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
