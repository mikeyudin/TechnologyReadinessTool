package net.techreadiness.ui.action.filters;

import java.util.List;
import java.util.Map;

public interface FilterSelectionHandler<T> {

	List<T> getList(Map<String, Object> parameters);

	List<T> getSelection();

	void add(Long id);

	void remove(Long id);

	void clear();
}
