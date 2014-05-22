package net.techreadiness.ui.action.filters;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractCheckboxSelectionHandler extends AbstractConversationFilterSelectionHandler<Boolean> {

	public abstract String getFilterName();

	public abstract String getDataGridName();

	@Override
	public List<Boolean> getList(Map<String, Object> parameters) {
		return null;
	}

	@Override
	public List<Boolean> getSelection() {
		if (getFilterValues().isEmpty()) {
			return Collections.emptyList();
		}
		return Collections.singletonList(Boolean.TRUE);
	}

	private Collection<String> getFilterValues() {
		return getDataGridState(getDataGridName()).getFilters().get(getFilterName());
	}

	@Override
	public void add(Long id) {
		getFilterValues().clear();
		if (id.equals(1L)) {
			getFilterValues().add("true");
		}
	}

	@Override
	public void remove(Long id) {
		getFilterValues().clear();
	}

	@Override
	public void clear() {
		getFilterValues().clear();
	}

}