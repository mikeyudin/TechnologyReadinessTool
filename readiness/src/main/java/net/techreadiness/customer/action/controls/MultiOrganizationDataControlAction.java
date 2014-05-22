package net.techreadiness.customer.action.controls;

import java.util.List;

import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.action.filters.MultipleFilterSelectionHandler;
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

@Namespace("/multiOrganizationControl")
public class MultiOrganizationDataControlAction extends BaseAction implements ConversationAware, ApplicationContextAware {
	private static final long serialVersionUID = 1L;

	private List<JSONOrg> jsonOrgs;
	private Long orgId;
	private List<Org> orgs;
	private boolean multiple = false;
	private String key;

	private Conversation conversation;
	private ApplicationContext applicationContext;

	@Action(value = "ajaxOrgLoad", results = { @Result(type = "json", params = { "root", "jsonOrgs" }) })
	public String ajaxOrgLoad() {
		MultipleFilterSelectionHandler<Org> selectionHandler = getSelectionHandler();
		List<Org> orgs = selectionHandler.getList(ActionContext.getContext().getParameters(), key);
		jsonOrgs = Lists.newArrayList();

		// convert objects to JSON
		for (Org org : orgs) {
			jsonOrgs.add(new JSONOrg(org.getOrgId(), org.getName(), org.getCode(), org.getCity(), org.getState(), org
					.getOrgTypeName()));
		}

		return SUCCESS;
	}

	@Action(value = "add", results = { @Result(name = "success", location = "show", type = "redirect", params = { "ajax",
			"true", "dataGridId", "%{dataGridId}", "multiple", "%{multiple}", "key", "%{key}" }) })
	public String add() {
		MultipleFilterSelectionHandler<Org> selectionHandler = getSelectionHandler();
		if (!multiple) {
			selectionHandler.clear(key);
		}
		selectionHandler.add(orgId, key);
		return SUCCESS;
	}

	@Action(value = "show", results = { @Result(name = "success", location = "/multiOrganizationControl/displayOrgSelection.jsp") }, params = {
			"ajax", "true", "dataGridId", "%{dataGridId}", "multiple", "%{multiple}", "key", "%{key}" })
	public String show() {
		MultipleFilterSelectionHandler<Org> selectionHandler = getSelectionHandler();
		orgs = selectionHandler.getSelection(key);
		return SUCCESS;
	}

	@Action(value = "remove", results = { @Result(name = "success", location = "show", type = "redirect", params = { "ajax",
			"true", "dataGridId", "%{dataGridId}", "multiple", "%{multiple}", "key", "%{key}" }) })
	public String remove() {
		MultipleFilterSelectionHandler<Org> selectionHandler = getSelectionHandler();
		selectionHandler.remove(orgId, key);
		return SUCCESS;
	}

	@Action(value = "showOptions", results = { @Result(name = "success", location = "/multiOrganizationControl/changeOrgSelection.jsp") })
	public String showOptions() {
		return SUCCESS;
	}

	private MultipleFilterSelectionHandler<Org> getSelectionHandler() {
		String beanName = conversation.get(String.class, "orgFilterSelectionHandler");
		MultipleFilterSelectionHandler<Org> selectionHandler = applicationContext.getBean(beanName,
				MultipleFilterSelectionHandler.class);
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
