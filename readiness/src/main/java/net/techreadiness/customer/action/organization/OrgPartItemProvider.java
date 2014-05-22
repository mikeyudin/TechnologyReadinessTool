package net.techreadiness.customer.action.organization;

import java.util.Map;

import net.techreadiness.service.common.DataGridItemProvider;

public interface OrgPartItemProvider extends DataGridItemProvider<Map<String, String>> {

	void setIgnoreScope(boolean ignoreScope);

	void setScopeId(Long scopeId);

	void setOrgId(Long orgId);
}
