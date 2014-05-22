package net.techreadiness.plugin.action.device;

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
public class OrgSelectionFilterForDeviceList extends AbstractConversationFilterSelectionHandler<Org> {
	static final String FILTER_NAME = "org";
	static final String DATA_GRID_ID = "deviceSearchGrid";
	@Inject
	private OrganizationService orgService;

	@Override
	public List<Org> getList(Map<String, Object> parameters) {
		String[] term = (String[]) parameters.get("term");
		return orgService.findOrgsBySearchTerm(getServiceContext(), term[0], 50);
	}

	@Override
	public List<Org> getSelection() {
		Collection<Long> orgIds = Lists.newArrayList();
		for (String orgId : getDataGridState(DATA_GRID_ID).getFilters().get(FILTER_NAME)) {
			orgIds.add(Long.valueOf(orgId));
		}
		return orgService.findByIds(getServiceContext(), orgIds);
	}

	@Override
	public void add(Long id) {
		getDataGridState(DATA_GRID_ID).getFilters().get(FILTER_NAME).add(id.toString());
	}

	@Override
	public void remove(Long id) {
		getDataGridState(DATA_GRID_ID).getFilters().remove(FILTER_NAME, id.toString());
	}

	@Override
	public void clear() {
		getDataGridState(DATA_GRID_ID).getFilters().get(FILTER_NAME).clear();
	}

}