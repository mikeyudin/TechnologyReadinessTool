package net.techreadiness.customer.datagrid;

import java.util.Collection;
import java.util.Map;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Org;

public interface OrgsByIdItemProvider extends DataGridItemProvider<Map<String, String>> {
	void setOrgs(Collection<Org> orgs);

}
