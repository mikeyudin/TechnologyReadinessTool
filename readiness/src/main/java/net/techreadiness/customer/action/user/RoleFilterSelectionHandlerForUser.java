package net.techreadiness.customer.action.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.service.RoleService;
import net.techreadiness.service.object.Role;
import net.techreadiness.ui.action.filters.AbstractConversationFilterSelectionHandler;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@org.springframework.context.annotation.Scope("prototype")
public class RoleFilterSelectionHandlerForUser extends AbstractConversationFilterSelectionHandler<Role> {
	private static final String ROLE_IDS = "roleId";
	private static final String USER_DATAGRID_STATE = "userGrid";
	@Inject
	private RoleService roleService;

	@Override
	public List<Role> getList(Map<String, Object> parameters) {
		String[] term = (String[]) parameters.get("term");
		List<Role> roles;

		if (term != null && term.length > 0 && !term[0].trim().isEmpty()) {
			roles = roleService.findRolesBySearchTerm(getServiceContext(), term[0]);
		} else {
			roles = Lists.newLinkedList(roleService.findRolesFromScope(getServiceContext()));
		}

		return roles;
	}

	@Override
	public List<Role> getSelection() {
		Collection<String> strings = getDataGridState(USER_DATAGRID_STATE).getFilters().get(ROLE_IDS);
		Collection<Long> roleIds = Lists.newArrayList();
		for (String string : strings) {
			roleIds.add(Long.valueOf(string));
		}
		return roleService.findByIds(getServiceContext(), roleIds);
	}

	@Override
	public void add(Long id) {
		getDataGridState(USER_DATAGRID_STATE).getFilters().put(ROLE_IDS, id.toString());
	}

	@Override
	public void remove(Long id) {
		getDataGridState(USER_DATAGRID_STATE).getFilters().remove(ROLE_IDS, id.toString());
	}

	@Override
	public void clear() {
		getDataGridState(USER_DATAGRID_STATE).getFilters().get(ROLE_IDS).clear();
	}
}
