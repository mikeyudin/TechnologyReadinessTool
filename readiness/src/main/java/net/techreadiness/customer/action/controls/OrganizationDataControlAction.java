package net.techreadiness.customer.action.controls;

import java.util.List;

import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.action.filters.FilterSelectionHandler;
import net.techreadiness.ui.model.JSONOrg;
import net.techreadiness.ui.util.Conversation;
import net.techreadiness.ui.util.ConversationAware;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/organizationControl")
public class OrganizationDataControlAction extends BaseAction implements ConversationAware, ApplicationContextAware {
	private static final long serialVersionUID = 1L;

	private List<JSONOrg> jsonOrgs;
	private Long orgId;
	private List<Org> orgs;
	private boolean multiple = false;

	private Conversation conversation;
	private ApplicationContext applicationContext;

	@Action(value = "ajaxOrgLoad", results = { @Result(type = "json", params = { "root", "jsonOrgs" }) })
	public String ajaxOrgLoad() {
		FilterSelectionHandler<Org> selectionHandler = getSelectionHandler();
		List<Org> orgs = selectionHandler.getList(ActionContext.getContext().getParameters());
		jsonOrgs = Lists.newArrayList();

		// convert objects to JSON
		for (Org org : orgs) {
			jsonOrgs.add(new JSONOrg(org.getOrgId(), org.getName(), org.getCode(), org.getCity(), org.getState(), org
					.getOrgTypeName()));
		}

		return SUCCESS;
	}

	@Action(value = "add", results = { @Result(name = "success", location = "show", type = "redirect", params = { "ajax",
			"true", "dataGridId", "%{dataGridId}", "multiple", "%{multiple}" }) })
	public String add() {
		FilterSelectionHandler<Org> selectionHandler = getSelectionHandler();
		if (!multiple) {
			selectionHandler.clear();
		}
		selectionHandler.add(orgId);
		return SUCCESS;
	}

	@Action(value = "show", results = { @Result(name = "success", location = "/organizationControl/displayOrgSelection.jsp") }, params = {
			"ajax", "true", "dataGridId", "%{dataGridId}", "multiple", "%{multiple}" })
	public String show() {
		FilterSelectionHandler<Org> selectionHandler = getSelectionHandler();
		orgs = selectionHandler.getSelection();
		return SUCCESS;
	}

	@Action(value = "remove", results = { @Result(name = "success", location = "show", type = "redirect", params = { "ajax",
			"true", "dataGridId", "%{dataGridId}", "multiple", "%{multiple}" }) })
	public String remove() {
		FilterSelectionHandler<Org> selectionHandler = getSelectionHandler();
		selectionHandler.remove(orgId);
		return SUCCESS;
	}

	@Action(value = "showOptions", results = { @Result(name = "success", location = "/organizationControl/changeOrgSelection.jsp") })
	public String showOptions() {
		return SUCCESS;
	}

	private FilterSelectionHandler<Org> getSelectionHandler() {
		String beanName = conversation.get(String.class, "orgFilterSelectionHandler");
		FilterSelectionHandler<Org> selectionHandler = applicationContext.getBean(beanName, FilterSelectionHandler.class);
		if (selectionHandler instanceof ConversationAware) {
			((ConversationAware) selectionHandler).setConversation(conversation);
		}
		return selectionHandler;
	}

	public List<JSONOrg> getJsonOrgs() {
		return jsonOrgs;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public List<Org> getOrgs() {
		return orgs;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

}
