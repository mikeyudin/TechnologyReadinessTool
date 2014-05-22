package net.techreadiness.customer.action.task.user.create;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_CREATE;

import java.util.List;
import java.util.Map;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.User;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.mail.EmailException;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, location = "/task/user/create/add", type = "redirect"),
	@Result(name = "invalid", type = "lastAction", params = { "fieldName", "userMap", "actionName", "add" }) })
public class SaveAction extends BaseUserAction {

	private static final String EMAIL = "email";
	private static final String USERNAME = "username";
	private static final long serialVersionUID = 1L;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_CREATE })
	public String execute() {

		try {
			checkEmailToUsername(userMap);
			User newUser = userService.createFromMapWithRoles(getServiceContext(), userMap, getRoleSelectionHandler()
					.getSelection(), getOrgSelectionHandler().getSelection());
			getTaskFlowData().getUsers().add(newUser);
			userService.newAccountPassword(getServiceContext(), newUser);
		} catch (ValidationServiceException e) {
			List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
			for (ValidationError validationError : errors) {
				if (validationError.getFieldName().equals("roleValidation")
						|| validationError.getFieldName().equals("orgValidation")) {
					addFieldError(validationError.getFieldName(), validationError.getOnlineMessage());
				} else {
					addFieldError("userMap." + validationError.getFieldName(), validationError.getOnlineMessage());
				}
			}
		} catch (ServiceException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			if (rootCause instanceof EmailException) {
				addActionError(e.getMessage());
			} else {
				throw e;
			}
		}
		if (hasErrors()) {
			return "invalid";
		}
		return SUCCESS;
	}

	private static void checkEmailToUsername(Map<String, String> userMap) {
		if (userMap.get(USERNAME) == null) {
			userMap.put(USERNAME, userMap.get(EMAIL));
		}
	}

}
