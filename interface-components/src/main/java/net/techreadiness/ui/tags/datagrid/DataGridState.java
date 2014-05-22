package net.techreadiness.ui.tags.datagrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridColumn;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.common.ViewDef;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Named
public class DataGridState<T> implements DataGrid<T>, Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private int page = 1;
	private int pageSize = 10;
	private String search;
	private final FilterMap filters = new FilterMap();
	private final Map<String, T> selectedItems = Maps.newConcurrentMap();
	private boolean searchCleared;
	private DataGridItemProvider<T> itemProvider;
	private List<String> displayedColumns = null;
	private List<String> displayedFilters = null;
	private boolean clearSelected;
	private boolean paging = true;
	private ViewDef viewDef;
	private String editRowId;
	@Key(String.class)
	@Element(Boolean.class)
	private Map<String, Boolean> selectedRowId = Maps.newConcurrentMap();

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (!pageSize.equals(this.pageSize)) {
			int firstResult = getFirstResult();
			this.page = firstResult / pageSize + 1;
			this.pageSize = pageSize;
		}
	}

	@Override
	public List<DataGridColumn> getColumns() {
		return Lists.newArrayList();
	}

	@Override
	public List<T> getSelectedItems() {
		if (selectedItems.isEmpty()) {
			return Lists.newArrayList();
		}
		return new ArrayList<>(selectedItems.values());
	}

	public void selectItem(String repr, T item) {
		selectedItems.put(repr, item);
		selectedRowId.put(repr, Boolean.TRUE);
	}

	public void deSelectItem(String repr) {
		selectedItems.remove(repr);
		selectedRowId.remove(repr);
	}

	public boolean isItemSelected(String repr) {
		return selectedItems.containsKey(repr);
	}

	public void clearSelectedItems() {
		selectedItems.clear();
	}

	@Override
	public String getSearch() {
		return this.search;
	}

	public void setSearch(String search) {
		this.search = search;
		this.page = 1;
	}

	@Override
	public FilterMap getFilters() {
		return filters;
	}

	@Override
	public int getFirstResult() {
		return (page - 1) * pageSize;
	}

	public void setSearchCleared(boolean searchCleared) {
		this.searchCleared = searchCleared;
		this.page = 1;
	}

	public boolean isSearchCleared() {
		return searchCleared;
	}

	public void setItemProvider(DataGridItemProvider<T> itemProvider) {
		this.itemProvider = itemProvider;
	}

	public DataGridItemProvider<T> getItemProvider() {
		return itemProvider;
	}

	public void setDisplayedColumns(List<String> displayedColumns) {
		this.displayedColumns = displayedColumns;
	}

	public List<String> getDisplayedColumns() {
		return displayedColumns;
	}

	public boolean isClearSelected() {
		return clearSelected;
	}

	public void setClearSelected(boolean clearSelected) {
		this.clearSelected = clearSelected;
	}

	@Override
	public boolean isPaging() {
		return paging;
	}

	public void setPaging(boolean paging) {
		this.paging = paging;
	}

	public List<String> getDisplayedFilters() {
		return displayedFilters;
	}

	public void setDisplayedFilters(List<String> displayedFilters) {
		this.displayedFilters = displayedFilters;
	}

	@Override
	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public String getEditRowId() {
		return editRowId;
	}

	public void setEditRowId(String editRowId) {
		this.editRowId = editRowId;
	}

	public Map<String, Boolean> getSelectedRowId() {
		return selectedRowId;
	}

	public void setSelectedRowId(Map<String, Boolean> selectedRowId) {
		this.selectedRowId = selectedRowId;
	}
}
