package net.techreadiness.customer.datagrid;

import java.util.Collection;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Org;

public interface OrgsByIdEditTaskItemProvider extends DataGridItemProvider<Org> {
	void setOrgs(Collection<Org> orgs);

	void setServiceContext(ServiceContext serviceContext);
}
