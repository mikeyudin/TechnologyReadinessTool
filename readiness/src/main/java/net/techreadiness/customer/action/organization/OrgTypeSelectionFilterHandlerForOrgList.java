package net.techreadiness.customer.action.organization;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.ui.action.filters.AbstractConversationFilterSelectionHandler;
import net.techreadiness.ui.action.filters.DataGridAware;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@org.springframework.context.annotation.Scope("prototype")
public class OrgTypeSelectionFilterHandlerForOrgList extends
		AbstractConversationFilterSelectionHandler<SimpleEntry<Long, String>> implements DataGridAware {
	static final String FILTER_NAME = "orgType";
	static final String SCOPE_PATH = "scopePath";
	private DataGrid<?> grid;

	@Inject
	private OrganizationService orgService;

	@Override
	public List<SimpleEntry<Long, String>> getList(Map<String, Object> parameters) {
		return orgService.findOrgTypes(getServiceContext());
	}

	@Override
	public List<SimpleEntry<Long, String>> getSelection() {
		Collection<Long> orgTypeCodes = Lists.newArrayList();
		for (String orgTypeId : grid.getFilters().get(FILTER_NAME)) {
			orgTypeCodes.add(NumberUtils.toLong(orgTypeId));
		}
		return orgService.findOrgTypesByIds(orgTypeCodes);
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
