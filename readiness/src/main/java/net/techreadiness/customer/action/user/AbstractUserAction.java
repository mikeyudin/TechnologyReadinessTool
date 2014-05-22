package net.techreadiness.customer.action.user;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import net.techreadiness.navigation.taskflow.user.UserTaskFlowData;
import net.techreadiness.navigation.taskflow.user.UserTaskFlowDefinition;
import net.techreadiness.persistence.datagrid.OrgByUserItemProvider;
import net.techreadiness.persistence.datagrid.RoleByUserItemProvider;
import net.techreadiness.service.ConfigService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.RoleService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Role;
import net.techreadiness.service.object.User;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowState;
import net.techreadiness.ui.util.ConversationScoped;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.opensymphony.xwork2.Preparable;

public class AbstractUserAction extends BaseAction implements Preparable {

	private static final long serialVersionUID = 1L;

	protected Long userId;
	protected Long orgId;
	protected Long roleId;

	protected User user;
	protected Org org;
	protected Role role;

	protected boolean showTaskLinks;

	protected ViewDef viewDef;
	protected ViewDef roleViewDef;
	protected ViewDef orgViewDef;

	@Inject
	protected ConfigService configService;
	@Inject
	protected ScopeService scopeService;
	@Inject
	protected OrganizationService orgService;
	@Inject
	protected RoleService roleService;

	@ConversationScoped
	protected DataGridState<User> userGrid;
	@ConversationScoped
	protected DataGridState<Role> roleDataGridState;
	@ConversationScoped
	protected DataGridState<Org> orgDataGridState;

	@Inject
	protected UserTaskFlowDefinition userTaskFlow;
	@Inject
	protected UserTaskFlowData userTaskFlowData;

	@Inject
	protected RoleByUserItemProvider roleByUserItemProvider;
	@Inject
	protected OrgByUserItemProvider orgByUserItemProvider;

	@Override
	public void prepare() throws Exception {
		if (user == null) {
			user = userService.getNew(getServiceContext());
			userId = user.getUserId();
		}

		roleByUserItemProvider.setScopeId(getServiceContext().getScopeId());
		roleByUserItemProvider.setUserId(user.getUserId());

		orgByUserItemProvider.setScopeId(getServiceContext().getScopeId());
		orgByUserItemProvider.setUserId(user.getUserId());
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public void setShowTaskLinks(boolean showTaskLinks) {
		this.showTaskLinks = showTaskLinks;
	}

	public boolean isShowTaskLinks() {
		return showTaskLinks;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setUser(User user) {
		this.user = user;
		roleByUserItemProvider.setUserId(user.getUserId());
		orgByUserItemProvider.setUserId(user.getUserId());

	}

	public User getUser() {
		return user;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setUserGrid(DataGridState<User> userGrid) {
		this.userGrid = userGrid;
	}

	public DataGridState<User> getUserGrid() {
		return userGrid;
	}

	public DataGridItemProvider<Role> getRoleGridItemProvider() {
		return roleByUserItemProvider;
	}

	public DataGridItemProvider<Org> getOrgGridItemProvider() {
		return orgByUserItemProvider;
	}

	public void setRoleViewDef(ViewDef roleViewDef) {
		this.roleViewDef = roleViewDef;
	}

	public ViewDef getRoleViewDef() {
		return roleViewDef;
	}

	public void setRoleDataGridState(DataGridState<Role> roleDataGridState) {
		this.roleDataGridState = roleDataGridState;
	}

	public DataGridState<Role> getRoleDataGridState() {
		return roleDataGridState;
	}

	public void setOrgViewDef(ViewDef orgViewDef) {
		this.orgViewDef = orgViewDef;
	}

	public ViewDef getOrgViewDef() {
		return orgViewDef;
	}

	public void setOrgDataGridState(DataGridState<Org> orgDataGridState) {
		this.orgDataGridState = orgDataGridState;
	}

	public DataGridState<Org> getOrgDataGridState() {
		return orgDataGridState;
	}

	protected String routeToTask(String taskName) {
		Set<User> userList = new HashSet<>();

		userList.add(userService.getById(getServiceContext(), getUserId()));

		Task task = userTaskFlow.getTask(taskName);
		TaskFlowState state = new TaskFlowState();
		state.setTasks(Lists.newArrayList(task));
		userTaskFlowData.setTaskFlowState(state);

		userTaskFlowData.setUsers(userList);

		if (org != null) {
			userTaskFlowData.setOrgs(Sets.newHashSet(org));
		}
		if (role != null) {
			userTaskFlowData.setRoles(Sets.newHashSet(role));
		}

		return SUCCESS;
	}
}
