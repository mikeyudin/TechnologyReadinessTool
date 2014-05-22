package net.techreadiness.persistence.datagrid;

import java.util.Map;

import net.techreadiness.service.common.DataGridItemProvider;

public interface ScopeItemProvider extends DataGridItemProvider<Map<String, String>> {
	void setSearchCriteria(String searchString);
}
