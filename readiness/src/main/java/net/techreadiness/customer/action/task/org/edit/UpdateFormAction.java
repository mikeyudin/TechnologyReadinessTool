package net.techreadiness.customer.action.task.org.edit;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.filters.MultipleFilterSelectionHandler;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.ui.util.ConversationAware;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class UpdateFormAction extends OrgTaskFlowAction implements ApplicationContextAware {

	private static final long serialVersionUID = 1L;

	String orgId;
	String parentOrgId;

	List<Org> parentOrgs;
	List<SimpleEntry<Long, String>> orgTypes;
	private String key;

	@Inject
	private OrganizationService organizationService;

	private ApplicationContext applicationContext;

	@Override
	@Action(value = "updateForm", results = { @Result(name = "success", location = "/task/org/updateFormAction.jsp", params = {
			"orgId", "%{orgId}", "scopeId", "%{scopeId}", "orgTypedId", "%{orgTypeId}", "key", "%{key}", "parentOrgId",
			"%{parentOrgId}", "ajax", "true" }) })
	public String execute() {
		if (key == null) {
			return SUCCESS;
		}
		List<Org> orgSelection = getOrgSelectionHandler().getSelection(getKey());
		if (orgSelection.size() > 0) {
			parentOrgId = String.valueOf(orgSelection.get(0).getOrgId());
		}

		if (!StringUtils.isEmpty(parentOrgId)) {
			Org parentOrg = organizationService.getById(getServiceContext(), Long.valueOf(parentOrgId));
			orgTypes = organizationService.findChildOrgTypesByParentOrgType(getServiceContext(), parentOrg.getOrgTypeId());
		} else {
			orgTypes = organizationService.findChildOrgTypesByParentOrgType(getServiceContext(), null);
		}

		return SUCCESS;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public List<Org> getParentOrgs() {
		return parentOrgs;
	}

	public void setParentOrgs(List<Org> parentOrgs) {
		this.parentOrgs = parentOrgs;
	}

	public List<SimpleEntry<Long, String>> getOrgTypes() {
		return orgTypes;
	}

	public void setOrgTypes(List<SimpleEntry<Long, String>> orgTypes) {
		this.orgTypes = orgTypes;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
