package net.techreadiness.customer.datagrid;

import java.util.Collection;
import java.util.Map;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;

public interface OrgPartTaskItemProvider extends DataGridItemProvider<Map<String, String>> {

	void setScope(Scope scope);

	void setOrgs(Collection<Org> orgs);
}
