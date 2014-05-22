package net.techreadiness.customer.action.task.org.delete;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_DELETE;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.exception.AuthorizationException;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({
		@Result(name = "success", type = "redirectAction", params = { "namespace", "/task/org/delete", "actionName",
				"delete" }), @Result(name = "invalid", type = "lastAction", params = { "actionName", "delete" }) })
public class SaveAction extends OrgTaskFlowAction {
	private static final long serialVersionUID = 1L;

	@Inject
	private OrganizationService orgService;

	@ConversationScoped
	private DataGridState<?> orgSearchGrid;

	@Key(Long.class)
	@Element(Boolean.class)
	private Map<Long, Boolean> orgs = Maps.newHashMap();

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORG_DELETE })
	public String execute() {
		for (Entry<Long, Boolean> org : orgs.entrySet()) {
			if (org.getValue()) {
				try {
					orgService.delete(getServiceContext(), org.getKey());
					Iterator<Org> iter = getTaskFlowData().getOrgs().iterator();
					while (iter.hasNext()) {
						Org current = iter.next();
						if (current.getOrgId().equals(org.getKey())) {
							iter.remove();
						}
					}

					orgSearchGrid.deSelectItem(Long.toString(org.getKey()));
				} catch (ValidationServiceException e) {
					addActionError(e.getMessage());
				} catch (AuthorizationException e) {
					addActionError(e.getMessage());
				}
			}
		}

		if (hasErrors()) {
			return "invalid";
		}
		return SUCCESS;
	}

	public Map<Long, Boolean> getOrgs() {
		return orgs;
	}

	public void setOrgs(Map<Long, Boolean> orgs) {
		this.orgs = orgs;
	}
}
