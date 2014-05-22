package net.techreadiness.persistence.datagrid;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Role;

public interface RoleByUserItemProvider extends DataGridItemProvider<Role> {
	abstract void setUserId(Long userId);

	void setScopeId(Long scopeId);

}
