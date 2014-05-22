package net.techreadiness.customer.datagrid;

import java.util.Collection;
import java.util.List;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Org;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@Scope("prototype")
public class OrgsByIdEditTaskItemProviderImpl implements OrgsByIdEditTaskItemProvider {

	private Collection<Org> orgs;

	@Override
	public List<Org> getPage(DataGrid<Org> grid) {
		return Lists.newArrayList(orgs);
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Org> grid) {
		return orgs.size();
	}

	@Override
	public void setOrgs(Collection<Org> orgs) {
		this.orgs = orgs;

	}

	@Override
	public void setServiceContext(ServiceContext serviceContext) {
		// No service context needed
	}

}
