package net.techreadiness.plugin.datagrid;

import java.util.Collection;
import java.util.Map;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.object.Org;

public interface DataEntryCompleteItemProvider extends DataGridItemProvider<Map<String, String>> {
	void setOrgs(Collection<Org> orgs);

	void setServiceContext(ServiceContext serviceContext);

}
