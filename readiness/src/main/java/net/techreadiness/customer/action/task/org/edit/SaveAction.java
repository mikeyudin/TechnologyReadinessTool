package net.techreadiness.customer.action.task.org.edit;

import static net.techreadiness.security.CorePermissionCodes.CORE_CUSTOMER_ORG_UPDATE;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.filters.MultipleFilterSelectionHandler;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.ui.util.ConversationAware;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({
		@Result(name = Action.SUCCESS, type = "redirectAction", params = { "namespace", "/task/org/edit", "actionName",
				"edit" }), @Result(name = "invalid", type = "lastAction", params = { "actionName", "edit" }) })
public class SaveAction extends OrgTaskFlowAction implements Preparable, ApplicationContextAware {
	private static final long serialVersionUID = 1L;

	private Long scopeId;
	private Long orgTypeId;
	@Inject
	private OrganizationService organizationService;

	@Element(Org.class)
	@Key(Long.class)
	private Map<Long, Org> orgs;

	private ApplicationContext applicationContext;

	@Override
	@CoreSecured({ CORE_CUSTOMER_ORG_UPDATE })
	public String execute() {

		for (Entry<Long, Org> entry : orgs.entrySet()) {
			try {
				updateParentId(entry.getValue());
				Org updatedOrg = organizationService.addOrUpdate(getServiceContext(), entry.getValue());
				if (updatedOrg.getInactive()) {
					Collection<Org> descendantOrgs = organizationService.findDescendantOrgs(getServiceContext(),
							updatedOrg.getOrgId());
					for (Org childOrg : descendantOrgs) {
						childOrg.setInactive(Boolean.TRUE);
						organizationService.addOrUpdate(getServiceContext(), childOrg);
					}
				}
			} catch (ValidationServiceException e) {
				List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
				for (ValidationError validationError : errors) {
					addFieldError("orgs[" + entry.getKey() + "]." + validationError.getFieldName(),
							validationError.getOnlineMessage());
				}
			}
		}

		Collection<Long> orgIds = new HashSet<>();
		for (Org org : getTaskFlowData().getOrgs()) {
			orgIds.add(org.getOrgId());
		}
		Collection<Org> orgsById = organizationService.findByIds(getServiceContext(), orgIds);
		getTaskFlowData().getOrgs().clear();
		getTaskFlowData().getOrgs().addAll(orgsById);

		if (hasErrors()) {
			return "invalid";
		}

		return SUCCESS;
	}

	private void updateParentId(Org org) {
		List<Org> orgList = getOrgSelectionHandler().getSelection(String.valueOf(org.getOrgId()));
		if (orgList != null && !orgList.isEmpty()) {
			if (null == orgList.get(0)) {
				org.setParentOrgId(null);
			} else {
				org.setParentOrgId(orgList.get(0).getOrgId());
			}
		}
	}

	@Override
	public void prepare() throws Exception {
		orgs = Maps.newHashMap();
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public Long getScopeId() {
		return scopeId;
	}

	public void setOrgTypeId(Long orgTypeId) {
		this.orgTypeId = orgTypeId;
	}

	public Long getOrgTypeId() {
		return orgTypeId;
	}

	public Map<Long, Org> getOrgs() {
		return orgs;
	}

	public void setOrgs(Map<Long, Org> orgs) {
		this.orgs = orgs;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

	protected MultipleFilterSelectionHandler<Org> getOrgSelectionHandler() {
		String beanName = conversation.get(String.class, "orgFilterSelectionHandler");
		MultipleFilterSelectionHandler<Org> selectionHandler = applicationContext.getBean(beanName,
				MultipleFilterSelectionHandler.class);
		if (selectionHandler instanceof ConversationAware) {
			((ConversationAware) selectionHandler).setConversation(conversation);
		}
		return selectionHandler;
	}
}
