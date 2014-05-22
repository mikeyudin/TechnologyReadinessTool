package net.techreadiness.ui.action.filters;

import java.util.List;
import java.util.Map;

public interface MultipleFilterSelectionHandler<T> {

	List<T> getList(Map<String, Object> parameters, String key);

	List<T> getSelection(String key);

	void add(Long id, String key);

	void addSet(String key);

	void remove(Long id, String key);

	void clear(String key);
}
