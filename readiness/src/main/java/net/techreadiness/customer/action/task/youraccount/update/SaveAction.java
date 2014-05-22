package net.techreadiness.customer.action.task.youraccount.update;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_UPDATE;

import java.util.List;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.task.youraccount.YourAccountTaskFlowAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.dao.DataIntegrityViolationException;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

@Results({ @Result(name = Action.SUCCESS, type = "redirect", location = "/task/youraccount/update/edit"),
	@Result(name = Action.ERROR, type = "lastAction", params = { "actionName", "edit" }) })
public class SaveAction extends YourAccountTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;
	private User user;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_UPDATE })
	public String execute() {
		User db = userService.getByUsername(getServiceContext(), getServiceContext().getUserName());
		db.setFirstName(user.getFirstName());
		db.setLastName(user.getLastName());
		db.setEmail(user.getEmail());

		try {
			db = userService.update(getServiceContext(), db);
			getServiceContext().getUser().setFirstName(db.getFirstName());
			getServiceContext().getUser().setLastName(db.getLastName());
		} catch (ValidationServiceException e) {
			List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
			for (ValidationError validationError : errors) {
				addFieldError("user." + validationError.getFieldName(), validationError.getOnlineMessage());
			}
		} catch (ServiceException se) {
			if (se.getCause() instanceof DataIntegrityViolationException) {
				addFieldError("user.username", "Duplicated username");
			} else {
				addFieldError("user.username", "Problem updating");
			}
		}
		if (hasErrors()) {
			return ERROR;
		}
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		user = new User();
	}

	public User getUser() {
		return user;
	}
}