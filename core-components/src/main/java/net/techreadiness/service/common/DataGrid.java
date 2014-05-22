package net.techreadiness.service.common;

import java.util.List;

import com.google.common.collect.Multimap;

public interface DataGrid<T> {
	String getId();

	int getFirstResult();

	int getPageSize();

	int getPage();

	List<DataGridColumn> getColumns();

	List<T> getSelectedItems();

	String getSearch();

	Multimap<String, String> getFilters();

	ViewDef getViewDef();

	boolean isPaging();
}
