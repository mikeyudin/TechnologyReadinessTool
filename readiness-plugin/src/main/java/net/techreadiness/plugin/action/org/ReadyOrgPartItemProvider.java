package net.techreadiness.plugin.action.org;

import java.util.Map;

import net.techreadiness.service.common.DataGridItemProvider;

public interface ReadyOrgPartItemProvider extends DataGridItemProvider<Map<String, String>> {
	void setIgnoreScope(boolean ignoreScope);

	void setScopeId(Long scopeId);

	void setOrgId(Long orgId);
}
