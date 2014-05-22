package net.techreadiness.plugin.datagrid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Org;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("prototype")
@Transactional(readOnly = true)
public class OrgNetworkItemProviderImpl implements OrgNetworkItemProvider {

	private Collection<Org> orgs;

	@Override
	public List<Org> getPage(DataGrid<Org> grid) {
		return new ArrayList<>(orgs);
	}

	@Override
	public void setOrgs(Collection<Org> orgs) {
		this.orgs = orgs;
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Org> grid) {
		return orgs.size();
	}

}
