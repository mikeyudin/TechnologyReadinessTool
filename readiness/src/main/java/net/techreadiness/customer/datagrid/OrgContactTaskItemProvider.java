package net.techreadiness.customer.datagrid;

import java.util.Map;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;

public interface OrgContactTaskItemProvider extends DataGridItemProvider<Map<String, String>> {
	void setScope(Scope scope);

	void setOrg(Org org);
}
