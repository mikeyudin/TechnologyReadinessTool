package net.techreadiness.customer.action.task.user.orgassign;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ORG_UPDATE;

import java.util.Collection;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.UserService;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.User;
import net.techreadiness.service.object.UserOrg;
import net.techreadiness.ui.action.task.user.UserTaskFlowAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", location = "/task/user/orgassign.jsp"),
		@Result(name = "nouser", location = "/task/user/nouser.jsp") })
public class EditAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Key(Long.class)
	@Element(Long.class)
	private Multimap<Long, Long> assignments;

	@Inject
	private UserService userService;

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_ORG_UPDATE })
	public String execute() {
		if (getTaskFlowData().getUsers() == null || getTaskFlowData().getUsers().isEmpty()) {
			return "nouser";
		}

		if (!getTaskFlowData().getTaskFlowState().isInitialized()) {
			getTaskFlowData().getOrgs().addAll(
					userService.findOrgsForUsers(getServiceContext(), getServiceContext().getOrgId(), getServiceContext()
							.getScopeId(), getTaskFlowData().getUserIds()));
			getTaskFlowData().getTaskFlowState().initialized();
		}

		for (User user : getTaskFlowData().getUsers()) {
			for (UserOrg userOrg : user.getUserOrgs()) {
				assignments.put(user.getUserId(), userOrg.getOrgId());
			}
		}

		conversation.put("orgFilterSelectionHandler", "orgFilterSelectionHandlerForUserOrgAssign");

		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		assignments = HashMultimap.create();
	}

	public Multimap<Long, Long> getAssignments() {
		return assignments;
	}

	public Collection<User> getUsers() {
		return getTaskFlowData().getUsers();
	}

	public Collection<Org> getOrgs() {
		return getTaskFlowData().getOrgs();
	}
}
