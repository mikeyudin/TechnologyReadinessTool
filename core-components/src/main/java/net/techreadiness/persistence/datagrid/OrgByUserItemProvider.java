package net.techreadiness.persistence.datagrid;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Org;

public interface OrgByUserItemProvider extends DataGridItemProvider<Org> {
	abstract void setUserId(Long userId);

	void setScopeId(Long scopeId);

}
