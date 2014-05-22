package net.techreadiness.customer.action.task.user.orgassign;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_USER_ORG_UPDATE;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.UserOrgService;
import net.techreadiness.service.exception.ValidationServiceException;
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

@Results({ @Result(name = "success", type = "redirect", location = "/task/user/orgassign/edit"),
		@Result(name = "invalid", type = "lastAction", params = { "actionName", "edit" }) })
public class SaveAction extends UserTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Key(Long.class)
	@Element(Long.class)
	private Multimap<Long, Long> assignments;

	@Inject
	private UserOrgService userOrgService;

	@Override
	public void prepare() throws Exception {
		assignments = HashMultimap.create();
	}

	@Override
	@CoreSecured({ CORE_CUSTOMER_USER_ORG_UPDATE })
	public String execute() {
		for (User user : getTaskFlowData().getUsers()) {
			for (Org org : getTaskFlowData().getOrgs()) {
				try {
					if (assignments.get(user.getUserId()).contains(org.getOrgId())) {
						UserOrg userOrg = userOrgService.persist(getServiceContext(), user.getUserId(), org.getOrgId());
						user.getUserOrgs().add(userOrg);
					}
				} catch (ValidationServiceException e) {
					addFieldError("assignments[" + user.getUserId() + "]", e.getLocalizedMessage());
				}
			}
		}

		for (User user : getTaskFlowData().getUsers()) {
			for (Org org : getTaskFlowData().getOrgs()) {
				try {
					if (!assignments.get(user.getUserId()).contains(org.getOrgId())) {
						userOrgService.delete(getServiceContext(), user.getUserId(), org.getOrgId());
						UserOrg userOrg = new UserOrg();
						userOrg.setOrgId(org.getOrgId());
						userOrg.setUserId(user.getUserId());
						user.getUserOrgs().remove(userOrg);
					}
				} catch (ValidationServiceException e) {
					addFieldError("assignments[" + user.getUserId() + "]", e.getLocalizedMessage());
				}
			}
		}

		if (hasErrors()) {
			return "invalid";
		}
		return SUCCESS;
	}

	public Multimap<Long, Long> getAssignments() {
		return assignments;
	}
}
