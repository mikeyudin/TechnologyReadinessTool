package net.techreadiness.customer.action.controls;

import java.util.List;

import net.techreadiness.service.object.Role;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.action.filters.FilterSelectionHandler;
import net.techreadiness.ui.model.JSONRole;
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

@Namespace("/roleControl")
public class RoleDataControlAction extends BaseAction implements ConversationAware, ApplicationContextAware {
	private static final long serialVersionUID = 1L;

	private List<JSONRole> jsonRoles;
	private Long roleId;
	private List<Role> roles;

	private Conversation conversation;
	private ApplicationContext applicationContext;

	@Action(value = "ajaxRoleLoad", results = { @Result(type = "json", params = { "root", "jsonRoles" }) })
	public String ajaxRoleLoad() {
		FilterSelectionHandler<Role> selectionHandler = getSelectionHandler();
		List<Role> roles = selectionHandler.getList(ActionContext.getContext().getParameters());
		jsonRoles = Lists.newArrayList();

		// convert objects to JSON
		for (Role role : roles) {
			jsonRoles.add(new JSONRole(role.getRoleId(), role.getName(), role.getCode()));
		}

		return SUCCESS;
	}

	@Action(value = "add", results = { @Result(name = "success", location = "show", type = "redirect", params = { "ajax",
			"true", "dataGridId", "%{dataGridId}" }) })
	public String add() {
		FilterSelectionHandler<Role> selectionHandler = getSelectionHandler();
		selectionHandler.add(roleId);
		return SUCCESS;
	}

	@Action(value = "show", results = { @Result(name = "success", location = "/roleControl/displayRoleSelection.jsp") }, params = {
			"ajax", "true", "dataGridId", "%{dataGridId}" })
	public String show() {
		FilterSelectionHandler<Role> selectionHandler = getSelectionHandler();
		roles = selectionHandler.getSelection();
		return SUCCESS;
	}

	@Action(value = "remove", results = { @Result(name = "success", location = "show", type = "redirect", params = { "ajax",
			"true", "dataGridId", "%{dataGridId}" }) })
	public String remove() {
		FilterSelectionHandler<Role> selectionHandler = getSelectionHandler();
		selectionHandler.remove(roleId);
		return SUCCESS;
	}

	@Action(value = "showOptions", results = { @Result(name = "success", location = "/roleControl/changeRoleSelection.jsp") })
	public String showOptions() {
		return SUCCESS;
	}

	private FilterSelectionHandler<Role> getSelectionHandler() {
		String beanName = conversation.get(String.class, "roleFilterSelectionHandler");
		FilterSelectionHandler<Role> selectionHandler = applicationContext.getBean(beanName, FilterSelectionHandler.class);
		if (selectionHandler instanceof ConversationAware) {
			((ConversationAware) selectionHandler).setConversation(conversation);
		}
		return selectionHandler;
	}

	public List<JSONRole> getJsonRoles() {
		return jsonRoles;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public List<Role> getRoles() {
		return roles;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

}
