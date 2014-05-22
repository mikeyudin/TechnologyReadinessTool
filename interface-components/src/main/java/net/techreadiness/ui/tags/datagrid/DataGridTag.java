package net.techreadiness.ui.tags.datagrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridColumn;
import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.common.SelectableItemProvider;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewField;
import net.techreadiness.ui.tags.ParentTag;
import net.techreadiness.ui.tags.ToolbarTag;
import net.techreadiness.ui.tags.dataview.DataViewControlTag;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class DataGridTag<T> extends ParentTag implements DataGrid<T>, Serializable {
	private final Logger log = LoggerFactory.getLogger(DataGridTag.class);
	private static final String JSP_LOCATION = "/dataGrid/dataGrid.jsp";
	private static final long serialVersionUID = 1L;
	private String action;
	private final List<DataGridColumnTag> allColumns;
	private final List<Entry<String, String>> appliedFilters;
	private List<DataGridColumnGroupTag> columnGroups;
	private boolean columnSelectable;
	private Collection<T> currentPage;
	private List<DataViewControlTag> dataViewControls;
	private final Map<String, DataGridColumnTag> displayedColumns;
	private boolean editAlertDisabled;
	private String errorMessage;
	private boolean executed;
	private String fieldName;
	private List<DataGridColumnTag> hiddenColumns;
	private Set<String> hiddenFieldsInError;
	private boolean illegalState;
	private boolean inlineEditable;
	private String inlineSaveAction;
	private DataGridItemProvider<T> itemProvider;
	private boolean multiEditable;
	private String namespace;
	private String rowValue;
	private String saveAction;
	private boolean searchable;
	private boolean selectable;
	private boolean selectAllRows;
	private String selectedDisabledTest;
	private boolean showRequired;
	private DataGridState<T> state;
	private ToolbarTag toolbar;
	private int totalNumberOfItems;
	private String value;
	private String var;
	private ViewDef viewDef;
	private String groupExpression;
	private String prevGroupValue;
	private String title;
	private DataGridHeader headerBody;

	public DataGridTag() {
		allColumns = Lists.newArrayList();
		appliedFilters = Lists.newArrayList();
		columnGroups = Lists.newArrayList();
		displayedColumns = Maps.newLinkedHashMap();
		hiddenColumns = Lists.newArrayList();
		illegalState = false;
		var = "row";
		viewDef = new ViewDef();
		selectAllRows = false;

	}

	@Override
	public String execute() throws Exception {
		if (executed && isShouldRefreshGrid()) {
			return JSP_LOCATION;
		} else if (executed && !isShouldRefreshGrid()) {
			return null;
		}

		executed = true;

		this.state = (DataGridState<T>) getValueStack().findValue(value);

		Preconditions.checkNotNull(state, "The value for the datagrid with value %s was null. "
				+ "A datagrid must have a value in order to be displayed.", value);

		if (state.getItemProvider() != null && itemProvider == null) {
			this.itemProvider = state.getItemProvider();
		}

		Preconditions.checkNotNull(itemProvider, "The item provider for the datagrid with value %s was null. "
				+ "A datagrid must have an itemProvider to display items.", value);

		if (state.isSearchCleared()) {
			state.setSearchCleared(false);
			state.setSearch("");
			state.getFilters().clear();
		}

		if (state.isClearSelected()) {
			state.setClearSelected(false);
			state.clearSelectedItems();
			state.getSelectedRowId().clear();
		}

		if (state.getFilters().isModified()) {
			state.setPage(1);
			state.getFilters().setModified(false);
		}

		allColumns.addAll(mergeColumns().values());
		Collections.sort(allColumns);

		for (DataGridColumnTag column : allColumns) {
			column.execute();
			if (column.isDisplayed()) {
				displayedColumns.put(column.getCode(), column);
			} else {
				hiddenColumns.add(column);
			}
		}

		// add applied filters
		for (Entry<String, String> filter : state.getFilters().entries()) {
			if (!filter.getKey().equals("scopePath")) {
				appliedFilters.add(Maps.immutableEntry(filter.getKey(), getFilterValue(filter.getKey(), filter.getValue())));
			}
		}

		toolbar = Iterables.getOnlyElement(getChildren(ToolbarTag.class), null);
		setHeaderBody(Iterables.getOnlyElement(getChildren(DataGridHeader.class), null));
		// fetch items and counts
		try {
			if (!isShouldRefreshGrid() && itemProvider instanceof SelectableItemProvider) {
				selectRows();
			} else {
				currentPage = itemProvider.getPage(this);
				updateSelectedRows();
				totalNumberOfItems = itemProvider.getTotalNumberOfItems(this);
				if (currentPage.isEmpty() && totalNumberOfItems > 0) {
					state.setPage(Integer.valueOf(1));
					currentPage = itemProvider.getPage(this);
				}
			}

			if (!isShouldRefreshGrid()) {
				return null;
			}

		} catch (IllegalStateException e) {
			illegalState = true;
			errorMessage = e.getMessage();
		}
		return JSP_LOCATION;
	}

	private Map<String, DataGridColumnTag> mergeColumns() throws Exception {
		Map<String, DataGridColumnTag> columns = Maps.newHashMap();
		if (viewDef != null) {
			for (ViewField field : viewDef.getFields()) {
				DataGridColumnTag column = new DataGridColumnTag();
				column.setJspContext(getJspContext());
				column.setParent(this);
				column.setCode(field.getCode());
				column.setName(field.getName());
				column.setRequired(field.isRequired());
				column.setField(field);
				column.setDisplayOrder(Integer.toString(field.getDisplayOrder()));
				columns.put(field.getCode(), column);
			}
		}

		List<DataGridColumnTag> hardCodedColumns = Lists.newArrayList();
		// Get all of the columns that are not a part of groups
		hardCodedColumns.addAll(getChildren(DataGridColumnTag.class));

		// Get all of the columns that are a part of groups
		for (DataGridColumnGroupTag group : getChildren(DataGridColumnGroupTag.class)) {
			group.execute();
			columnGroups.add(group);
			hardCodedColumns.addAll(group.getColumns());
		}

		int columnIndex = 0;
		for (DataGridColumnTag columnTag : hardCodedColumns) {
			columnTag.setPageOrder(Integer.valueOf(columnIndex++));
			log.debug("Processing Column: {}", columnTag.getCode());
			DataGridColumnTag viewDefTag = columns.get(columnTag.getCode());
			if (viewDefTag != null && viewDefTag.getField() != null) {
				ViewField field = viewDefTag.getField();
				// The column definition from the ViewDef has been overriden in the JSP
				mergeColumn(columnTag, field);
				// Replace the ViewDef column with the hard coded column
				columns.put(columnTag.getCode(), columnTag);
			} else {
				// The column is hard coded in the JSP and is not in the ViewDef
				columns.put(columnTag.getCode(), columnTag);
			}
		}

		return columns;
	}

	private void mergeColumn(DataGridColumnTag hardCoded, ViewField viewField) {
		if (StringUtils.isBlank(hardCoded.getName())) {
			// If the name was not specified then use the one from the ViewDef
			hardCoded.setName(viewField.getName());
		}
		if (StringUtils.isBlank(hardCoded.getDisplayOrder())) {
			// If the order was not specified then use the one from the ViewDef
			if (viewField == null) {
				log.debug("Column: {} - {}", hardCoded.getCode(), hardCoded.getDisplayOrder());
				log.debug("Column Parent: {}", hardCoded.getParent());
			} else {
				hardCoded.setDisplayOrder(Integer.toString(viewField.getDisplayOrder()));
			}
		}
	}

	public boolean isShouldRefreshGrid() {
		return !"false".equals(getRequest().getParameter("refreshGrid"));
	}

	public String getAction() {
		if (action == null) {
			ActionInvocation ai = (ActionInvocation) ActionContext.getContext().get(ActionContext.ACTION_INVOCATION);
			action = ai.getProxy().getActionName();
		}
		return action;
	}

	public List<DataGridColumn> getAllColumns() {
		return new ArrayList<DataGridColumn>(allColumns);
	}

	public List<Entry<String, String>> getAppliedFilters() {
		return appliedFilters;
	}

	public List<DataGridColumnGroupTag> getColumnGroups() {
		return columnGroups;
	}

	@Override
	public List<DataGridColumn> getColumns() {
		return new ArrayList<DataGridColumn>(displayedColumns.values());
	}

	public Iterator<RowInfo<T>> getCurrentPage() {
		return Iterables.transform(currentPage, new Function<T, RowInfo<T>>() {
			@Override
			public RowInfo<T> apply(T row) {
				RowInfo<T> info = new RowInfo<>(DataGridTag.this, row);
				return info;
			}
		}).iterator();
	}

	public List<DataViewControlTag> getDataViewControls() {
		return dataViewControls;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getFieldName() {
		return fieldName;
	}

	@Override
	public Multimap<String, String> getFilters() {
		return state.getFilters();
	}

	public String getFilterValue(String fieldCode, String value) {
		if (viewDef == null) {
			return null;
		}

		for (ViewField field : viewDef.getFields()) {
			if (field.getCode().equals(fieldCode)) {
				return field.getOptions().isEmpty() ? value : field.getOptions().get(value);
			}
		}
		return value;
	}

	public int getFirstPage() {
		return Math.max(1, getPage() - 10);
	}

	@Override
	public int getFirstResult() {
		return state.getFirstResult();
	}

	public List<DataGridColumnTag> getHiddenColumns() {
		return hiddenColumns;
	}

	public Set<String> getHiddenFieldsInError() {
		return hiddenFieldsInError;
	}

	@Override
	public String getId() {
		return value;
	}

	public String getInlineSaveAction() {
		return inlineSaveAction;
	}

	public DataGridItemProvider<?> getItemProvider() {
		return itemProvider;
	}

	public int getLastPage() {
		return Math.min(getPage() + 10, getTotalNumberOfPages());
	}

	public String getNamespace() {
		if (namespace == null) {
			ActionInvocation ai = (ActionInvocation) ActionContext.getContext().get(ActionContext.ACTION_INVOCATION);
			namespace = ai.getProxy().getNamespace();
		}
		return namespace;
	}

	public int getNumberOfColumns() {
		return displayedColumns.size() + (isSelectable() ? 1 : 0) + (isInlineEditable() ? 1 : 0);
	}

	public int getNumberOfOuterColumns() {
		return (StringUtils.isBlank(title) ? 1 : 2) + (headerBody == null ? 0 : 1);
	}

	@Override
	public int getPage() {
		return state.getPage();
	}

	public int getPageEnd() {
		return Math.min(getPageStart() + getPageSize() - 1, getTotalNumberOfItems());
	}

	@Override
	public int getPageSize() {
		return state.getPageSize();
	}

	public int getPageStart() {
		return (getPage() - 1) * getPageSize() + 1;
	}

	public String getRowId(T item) {
		try {
			getValueStack().push(item);
			return getValueStack().findString(rowValue);
		} finally {
			getValueStack().pop();
		}
	}

	public String getRowValue() {
		return rowValue;
	}

	public String getSaveAction() {
		return saveAction;
	}

	@Override
	public String getSearch() {
		return state.getSearch();
	}

	public String getSelectedDisabledTest() {
		return selectedDisabledTest;
	}

	@Override
	public List<T> getSelectedItems() {
		return state.getSelectedItems();
	}

	public DataGridState<T> getState() {
		if (state == null) {
			state = (DataGridState<T>) getValueStack().findValue(value);
		}
		return state;
	}

	public ToolbarTag getToolbar() {
		return toolbar;
	}

	public int getTotalNumberOfItems() {
		return totalNumberOfItems;
	}

	public int getCurrentPageSize() {
		if (currentPage != null) {
			return currentPage.size();
		}

		return 0;
	}

	public int getTotalNumberOfPages() {
		return (int) Math.ceil((double) getTotalNumberOfItems() / (double) getPageSize());
	}

	public String getValue() {
		return value;
	}

	public String getVar() {
		return var;
	}

	@Override
	public ViewDef getViewDef() {
		return viewDef;
	}

	public boolean isColumnSelectable() {
		return columnSelectable;
	}

	public boolean isEditAlertDisabled() {
		return editAlertDisabled;
	}

	public boolean isEditMode(T row) {
		boolean editing;
		if (multiEditable) {
			editing = true;
		} else if (!inlineEditable) {
			editing = false;
		} else {
			String paramValue = state.getEditRowId();
			state.setEditRowId(null);
			String rowId = getRowId(row);

			editing = paramValue != null && paramValue.equals(rowId);
		}

		if (viewDef != null && editing && !multiEditable) {
			Map<String, List<String>> fieldErrors = Maps.newHashMap((Map<String, List<String>>) getValueStack().findValue(
					"fieldErrors"));
			List<String> displayedFieldNames = Lists.newArrayList();
			for (ViewField viewField : viewDef.getFields()) {
				if (displayedColumns.keySet().contains(viewField.getCode())) {
					displayedFieldNames.add(var + "." + viewField.getCode());
				}
			}
			fieldErrors.keySet().removeAll(displayedFieldNames);
			hiddenFieldsInError = fieldErrors.keySet();
		}

		return editing;
	}

	public boolean isFewerPages() {
		return getFirstPage() > 1;
	}

	public boolean isIllegalState() {
		return illegalState;
	}

	public boolean isInlineEditable() {
		return inlineEditable;
	}

	public boolean isInlineEditing() {
		if (!inlineEditable) {
			return false;
		}
		boolean editing = StringUtils.isNotEmpty(state.getEditRowId());

		return editing;
	}

	public boolean isMorePages() {
		return getLastPage() < getTotalNumberOfPages();
	}

	public boolean isMultiEditable() {
		return multiEditable;
	}

	public boolean isNoResults() {
		if (totalNumberOfItems > 0) {
			return false;
		}
		return true;
	}

	public boolean isOnFirstPage() {
		return getPage() == 1;
	}

	public boolean isOnLastPage() {
		return getPage() * getPageSize() >= getTotalNumberOfItems();
	}

	@Override
	public boolean isPaging() {
		return state.isPaging();
	}

	public boolean isResultsHeaderShown() {
		return isPaging() || isColumnSelectable();
	}

	public boolean isRowSelected(T row) {
		if (!selectable) {
			return false;
		}
		String representation = getRowId(row);

		return state.isItemSelected(representation);
	}

	public boolean isSearchable() {
		return searchable;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public boolean isSelectAllRows() {
		return selectAllRows;
	}

	public boolean isToolbarUsed() {
		return toolbar != null;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setColumnGroups(List<DataGridColumnGroupTag> columnGroups) {
		this.columnGroups = columnGroups;
	}

	public void setColumnSelectable(boolean columnSelectable) {
		this.columnSelectable = columnSelectable;
	}

	public void setEditAlertDisabled(boolean editAlertDisabled) {
		this.editAlertDisabled = editAlertDisabled;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setHiddenColumns(List<DataGridColumnTag> hiddenColumns) {
		this.hiddenColumns = hiddenColumns;
	}

	public void setInlineEditable(boolean inlineEditable) {
		this.inlineEditable = inlineEditable;
	}

	public void setInlineSaveAction(String inlineSaveAction) {
		this.inlineSaveAction = inlineSaveAction;
	}

	public void setItemProvider(DataGridItemProvider<T> itemProvider) {
		this.itemProvider = itemProvider;
	}

	public void setMultiEditable(boolean multiEditable) {
		this.multiEditable = multiEditable;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setPageSize(Integer pageSize) {
		state.setPageSize(pageSize);
	}

	public void setPaging(boolean paging) {
		getState().setPaging(paging);
	}

	public void setRowValue(String rowValue) {
		this.rowValue = rowValue;
	}

	public void setSaveAction(String saveAction) {
		this.saveAction = saveAction;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public void setSelectAllRows(boolean selectAllRows) {
		this.selectAllRows = selectAllRows;
	}

	public void setSelectedDisabledTest(String selectedDisabledTest) {
		this.selectedDisabledTest = selectedDisabledTest;
	}

	public void setState(DataGridState<T> state) {
		this.state = state;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	/**
	 * update the selected rows
	 * 
	 * @return true if any rows were updated
	 */
	public boolean updateSelectedRows() {
		List<T> itemsToUpdate = Lists.newArrayList(currentPage);

		itemsToUpdate.addAll(state.getSelectedItems());
		boolean updated = false;
		for (T item : itemsToUpdate) {
			if (updateRowSelection(item)) {
				updated = true;
			}
		}
		return updated;
	}

	/**
	 * update
	 * 
	 * @return true if the selection was updated.
	 */
	public boolean updateRowSelection(T row) {
		String representation = getRowId(row);

		if (StringUtils.isBlank(representation)) {
			return false;
		}
		Boolean selected = state.getSelectedRowId().get(representation);
		if (selected != null && selected) {
			state.selectItem(representation, row);
		} else if (selected != null && !selected) {
			state.deSelectItem(representation);
		}
		return true;
	}

	public void selectRows() {
		if (itemProvider instanceof SelectableItemProvider) {
			SelectableItemProvider<T> sip = (SelectableItemProvider<T>) itemProvider;
			for (Entry<String, Boolean> selection : state.getSelectedRowId().entrySet()) {
				if (selection.getValue()) {
					state.selectItem(selection.getKey(), sip.getObjectForKey(selection.getKey()));
				} else {
					state.deSelectItem(selection.getKey());
				}
			}
			state.getSelectedRowId().clear();
		}
	}

	public boolean dataFiltered(String id) {
		return state.getFilters().asMap().containsKey(id);
	}

	public void setShowRequired(boolean showRequired) {
		this.showRequired = showRequired;
	}

	public boolean isShowRequired() {
		return showRequired;
	}

	public String getGroupExpression() {
		return groupExpression;
	}

	public void setGroupExpression(String groupExpression) {
		this.groupExpression = groupExpression;
	}

	public String getPrevGroupValue() {
		return prevGroupValue;
	}

	public void setPrevGroupValue(String prevGroupValue) {
		this.prevGroupValue = prevGroupValue;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DataGridHeader getHeaderBody() {
		return headerBody;
	}

	public void setHeaderBody(DataGridHeader headerBody) {
		this.headerBody = headerBody;
	}

}
