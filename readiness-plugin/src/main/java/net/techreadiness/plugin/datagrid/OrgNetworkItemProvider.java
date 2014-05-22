package net.techreadiness.plugin.datagrid;

import java.util.Collection;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Org;

public interface OrgNetworkItemProvider extends DataGridItemProvider<Org> {
	void setOrgs(Collection<Org> orgs);

}
