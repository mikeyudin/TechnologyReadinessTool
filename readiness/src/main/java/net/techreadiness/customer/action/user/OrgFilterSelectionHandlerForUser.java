package net.techreadiness.customer.action.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.filters.AbstractConversationFilterSelectionHandler;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@org.springframework.context.annotation.Scope("prototype")
public class OrgFilterSelectionHandlerForUser extends AbstractConversationFilterSelectionHandler<Org> {
	private static final String ORG_IDS = "orgId";
	private static final String USER_DATAGRID_STATE = "userGrid";
	@Inject
	private OrganizationService orgService;

	@Override
	public List<Org> getList(Map<String, Object> parameters) {
		String[] term = (String[]) parameters.get("term");
		List<Org> orgs;

		if (term != null && term.length > 0 && term[0].length() > 0) {
			orgs = orgService.findOrgsBySearchTerm(getServiceContext(), term[0], 50);
		} else {
			orgs = orgService.findOrgsByScope(getServiceContext(), 50);
		}

		return orgs;
	}

	@Override
	public List<Org> getSelection() {
		Collection<String> strings = getDataGridState(USER_DATAGRID_STATE).getFilters().get(ORG_IDS);
		Collection<Long> orgIds = Lists.newArrayList();
		for (String string : strings) {
			orgIds.add(Long.valueOf(string));
		}
		return orgService.findByIds(getServiceContext(), orgIds);
	}

	@Override
	public void add(Long id) {
		getDataGridState(USER_DATAGRID_STATE).getFilters().put(ORG_IDS, id.toString());
	}

	@Override
	public void remove(Long id) {
		getDataGridState(USER_DATAGRID_STATE).getFilters().remove(ORG_IDS, id.toString());
	}

	@Override
	public void clear() {
		getDataGridState(USER_DATAGRID_STATE).getFilters().get(ORG_IDS).clear();
	}
}
