package net.techreadiness.ui.action.filters;

import java.util.Collection;
import java.util.List;

import net.techreadiness.ui.tags.datagrid.FilterMap;

import com.google.common.collect.Lists;

public abstract class AbstractDataGridFilterSelectionHandler<T> extends AbstractConversationFilterSelectionHandler<T> {

	public abstract String getDataGridName();

	public abstract String getFilterName();

	public abstract List<T> getSelection(List<Long> filterValues);

	@Override
	public List<T> getSelection() {
		Collection<String> strings = getFilters().get(getFilterName());
		List<Long> orgIds = Lists.newArrayList();
		for (String string : strings) {
			orgIds.add(Long.valueOf(string));
		}
		return getSelection(orgIds);
	}

	@Override
	public void add(Long id) {
		getFilters().put(getFilterName(), id.toString());
	}

	@Override
	public void remove(Long id) {
		getFilters().remove(getFilterName(), id.toString());
	}

	@Override
	public void clear() {
		getFilters().get(getFilterName()).clear();
	}

	private FilterMap getFilters() {
		return getDataGridState(getDataGridName()).getFilters();
	}

	public Collection<String> getFilterValues() {
		return getDataGridState(getDataGridName()).getFilters().get(getFilterName());
	}
}
