package net.techreadiness.customer.action.task.user.enable;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ENABLE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.DataModificationStatus.ModificationState;
import net.techreadiness.service.UserService;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.action.task.user.UserTaskFlowAction;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({
		@Result(name = "success", type = "redirectAction", params = { "namespace", "/task/user/enable", "actionName", "edit" }),
		@Result(name = "invalid", type = "lastAction", params = { "actionName", "edit" }) })
public class SaveAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;
	@Inject
	private UserService userService;
	@Key(Long.class)
	@Element(User.class)
	private Map<Long, User> users;

	@Inject
	DataModificationStatus dataModificationStatus;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_ENABLE })
	public String execute() {
		for (Entry<Long, User> entry : users.entrySet()) {
			try {
				if (entry.getValue().getDisableDate() == null) {
					if (StringUtils.isNotEmpty(entry.getValue().getDisableReason())) {
						addFieldError("users[" + entry.getKey() + "]." + "disableReason",
								"Status of Disabled is required with Disable Reason.");
					}

					if (!hasErrors()) {
						userService.enableUser(getServiceContext(), entry.getKey());
					}
				} else {

					if (StringUtils.isEmpty(entry.getValue().getDisableReason())) {
						addFieldError("users[" + entry.getKey() + "]." + "disableReason",
								"Disable Reason is required when Status is Disabled.");
					}

					if (!hasErrors()) {
						userService.disableUser(getServiceContext(), entry.getKey(), entry.getValue().getDisableDate(),
								entry.getValue().getDisableReason());
					}
				}
			} catch (ValidationServiceException e) {
				List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
				for (ValidationError validationError : errors) {
					addFieldError("users[" + entry.getKey() + "]." + validationError.getFieldName(),
							validationError.getOnlineMessage());
				}
			}
		}
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
}
