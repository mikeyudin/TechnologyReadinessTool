package net.techreadiness.customer.action.task.user.update;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_UPDATE;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.DataModificationStatus.ModificationState;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.task.user.UserTaskFlowAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.dao.DataIntegrityViolationException;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", type = "redirect", location = "/task/user/update/edit"),
		@Result(name = "invalid", type = "lastAction", params = { "actionName", "edit" }) })
public class SaveAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;
	@Inject
	private DataModificationStatus dataModificationStatus;
	@Key(Long.class)
	@Element(User.class)
	private Map<Long, User> users;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_UPDATE })
	public String execute() {

		for (Entry<Long, User> entry : users.entrySet()) {
			try {
				entry.setValue(userService.update(getServiceContext(), entry.getValue()));
				getTaskFlowData().getUsers().add(entry.getValue());
			} catch (ValidationServiceException e) {
				List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
				for (ValidationError validationError : errors) {
					addFieldError("users[" + entry.getKey() + "]." + validationError.getFieldName(),
							validationError.getOnlineMessage());
				}
			} catch (ServiceException se) {
				if (se.getCause() instanceof DataIntegrityViolationException) {
					addFieldError("users[" + entry.getKey() + "]." + "username", "Duplicated username");
				} else {
					addFieldError("users[" + entry.getKey() + "]." + "username", "Problem updating");
				}
			}
		}

		Collection<User> usersById = userService.findById(getServiceContext(), getTaskFlowData().getUserIds());
		getTaskFlowData().getUsers().clear();
		getTaskFlowData().getUsers().addAll(usersById);
		if (hasErrors()) {
			dataModificationStatus.setModificationState(ModificationState.FAILURE);
			return "invalid";
		}
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		users = new HashMap<>();
	}

	public Map<Long, User> getUsers() {
		return users;
	}

	public void setUsers(Map<Long, User> users) {
		this.users = users;
	}
}