package net.techreadiness.persistence.datagrid;

import java.util.Map;

import net.techreadiness.service.common.DataGridItemProvider;

public interface RoleItemProvider extends DataGridItemProvider<Map<String, String>> {
	void setSearchCriteria(String searchString);

	void setScope(Long scopeId);
}
