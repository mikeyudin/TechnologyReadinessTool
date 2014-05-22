package net.techreadiness.customer.action.organization;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.action.filters.AbstractConversationFilterSelectionHandler;
import net.techreadiness.ui.action.filters.DataGridAware;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ActionContext;

@Component
@org.springframework.context.annotation.Scope("prototype")
public class ParentOrgSelectionFilterForOrgList extends AbstractConversationFilterSelectionHandler<Org> implements
		DataGridAware {
	static final String FILTER_NAME = "parentOrg";
	static final String SCOPE_PATH = "scopePath";
	private DataGrid<?> grid;

	@Inject
	private OrganizationService orgService;

	@Override
	public List<Org> getList(Map<String, Object> parameters) {
		Object[] term = (Object[]) parameters.get("term");
		ServiceContext context = (ServiceContext) ActionContext.getContext().getSession().get(BaseAction.SERVICE_CONTEXT);
		List<Org> orgs;

		if (term != null && term.length > 0) {
			orgs = orgService.findOrgsThatCanHaveChildrenBySearchTerm(context, (String) term[0], 50);
		} else {
			orgs = orgService.findOrgsThatCanHaveChildren(context, 50);
		}

		return orgs;
	}

	@Override
	public List<Org> getSelection() {
		Collection<Long> orgIds = Lists.newArrayList();
		for (String orgId : grid.getFilters().get(FILTER_NAME)) {
			orgIds.add(Long.valueOf(orgId));
		}
		return orgService.findByIds(getServiceContext(), orgIds);
	}

	@Override
	public void add(Long id) {
		grid.getFilters().get(FILTER_NAME).add(id.toString());
	}

	@Override
	public void remove(Long id) {
		grid.getFilters().remove(FILTER_NAME, id.toString());
	}

	@Override
	public void clear() {
		grid.getFilters().get(FILTER_NAME).clear();
	}

	@Override
	public void setDataGrid(DataGrid<?> grid) {
		this.grid = grid;

	}

}
