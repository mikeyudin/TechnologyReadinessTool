package net.techreadiness.ui.tags.taskview;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.techreadiness.service.common.DataGridItemProvider;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.ui.tags.ParentTag;
import net.techreadiness.ui.tags.ToolbarTag;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.tags.dataview.DataViewControlTag;
import net.techreadiness.ui.tags.taskflow.TaskNavigationTag;
import net.techreadiness.ui.task.TaskFlowData;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

public class TaskViewTag<T> extends ParentTag {
	private List<DataViewControlTag> viewControls;
	private ViewDef dataGridViewDef;
	private ViewDef fieldSetViewDef;
	private boolean detailMode = false;
	private String value;
	private String var;
	private String fieldName;
	private String nameExpression;
	private String listHeader;
	private String rowIdentifier;
	private DataGridItemProvider<T> itemProvider;
	private List<EntityFieldTag> columns;
	private List<EntityFieldGroupTag> entityGroups;
	private DataGridState<T> state;
	private Collection<T> items;
	private ToolbarTag toolBar;
	private Collection<?> selection;
	private String selectionTitle;
	private String selectionDisplayExpression;
	private boolean hideNameColumnInGrid;
	private TaskFlowData taskFlowData;
	private TaskNavigationTag navigationTag;
	private String groupExpression;
	private boolean paging;
	private boolean suppressSave;
	private boolean selectAllRows;

	@Override
	public String execute() throws Exception {
		viewControls = getChildren(DataViewControlTag.class);

		state = (DataGridState<T>) getValueStack().findValue(value);
		if (state == null) {
			state = new DataGridState<>();
			state.setPaging(false);
		} else if (state instanceof TaskViewState) {
			TaskViewState<T> taskViewState = (TaskViewState<T>) state;
			detailMode = taskViewState.isDetailMode();
		}

		if (state.getItemProvider() != null && itemProvider == null) {
			itemProvider = state.getItemProvider();
		}

		if (detailMode && itemProvider != null) {
			try {
				items = itemProvider.getPage(state);
			} catch (IllegalStateException e) {
				items = Collections.emptyList();
			}
		}

		columns = getChildren(EntityFieldTag.class);
		int i = 0;
		for (EntityFieldTag column : columns) {
			column.setPageOrder(Integer.valueOf(i));
			column.execute();
			i++;
		}

		Collections.sort(columns);

		entityGroups = getChildren(EntityFieldGroupTag.class);
		for (EntityFieldGroupTag columnGroup : entityGroups) {
			columnGroup.execute();
			for (EntityFieldTag field : columnGroup.getFields()) {
				field.execute();
			}
		}

		toolBar = Iterables.getOnlyElement(getChildren(ToolbarTag.class), null);

		navigationTag = Iterables.getOnlyElement(getChildren(TaskNavigationTag.class), new TaskNavigationTag());
		if (taskFlowData != null) {
			navigationTag.setTaskFlow(taskFlowData.getTaskFlowState());
		}
		navigationTag.setDetailMode(isDetailMode());
		navigationTag.setAllowModeSwitch(isModeToggleable());
		navigationTag.setValue(value);
		navigationTag.setSuppressSave(isSuppressSave());

		return "/taskView/taskView.jsp";
	}

	public boolean isRowInError() {
		Map<String, List<String>> fieldErrors = Maps.filterKeys(
				(Map<String, List<String>>) getValueStack().findValue("fieldErrors"), new FieldErrorFilter());
		return !fieldErrors.isEmpty();
	}

	public boolean isFilterBarDisplayed() {
		return viewControls != null && !viewControls.isEmpty();
	}

	public boolean isSelectable() {
		return rowIdentifier != null;
	}

	public boolean isSelectAllRows() {
		return selectAllRows;
	}

	public void setSelectAllRows(boolean selectAllRows) {
		this.selectAllRows = selectAllRows;
	}

	public boolean isColumnSelectable() {
		return dataGridViewDef != null;
	}

	public boolean isSelectionDisplayed() {
		return selectionDisplayExpression != null;
	}

	public boolean isModeToggleable() {
		return state instanceof TaskViewState;
	}

	public List<DataViewControlTag> getViewControls() {
		return viewControls;
	}

	public boolean isDetailMode() {
		return detailMode;
	}

	public void setDetailMode(boolean detailMode) {
		this.detailMode = detailMode;
	}

	public DataGridItemProvider<T> getItemProvider() {
		return itemProvider;
	}

	public void setItemProvider(DataGridItemProvider<T> itemProvider) {
		this.itemProvider = itemProvider;
	}

	public List<EntityFieldTag> getColumns() {
		return columns;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Iterator<RowInfo<T>> getItems() {
		return Iterables.transform(items, new Function<T, RowInfo<T>>() {
			@Override
			public RowInfo<T> apply(T row) {
				RowInfo<T> info = new RowInfo<>(row, getValueStack(), var, fieldName);
				return info;
			}
		}).iterator();
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getNameExpression() {
		return nameExpression;
	}

	public void setNameExpression(String nameExpression) {
		this.nameExpression = nameExpression;
	}

	public String getListHeader() {
		return listHeader;
	}

	public void setListHeader(String listHeader) {
		this.listHeader = listHeader;
	}

	public ViewDef getDataGridViewDef() {
		return dataGridViewDef;
	}

	public void setDataGridViewDef(ViewDef dataGridViewDef) {
		this.dataGridViewDef = dataGridViewDef;
	}

	public ViewDef getFieldSetViewDef() {
		return fieldSetViewDef;
	}

	public void setFieldSetViewDef(ViewDef fieldSetViewDef) {
		this.fieldSetViewDef = fieldSetViewDef;
	}

	public ToolbarTag getToolBar() {
		return toolBar;
	}

	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public Collection<?> getSelection() {
		return selection;
	}

	public void setSelection(Collection<?> selection) {
		this.selection = selection;
	}

	public String getSelectionTitle() {
		return selectionTitle;
	}

	public void setSelectionTitle(String selectionTitle) {
		this.selectionTitle = selectionTitle;
	}

	public String getSelectionDisplayExpression() {
		return selectionDisplayExpression;
	}

	public void setSelectionDisplayExpression(String selectionDisplayExpression) {
		this.selectionDisplayExpression = selectionDisplayExpression;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}

	public List<EntityFieldGroupTag> getEntityGroups() {
		return entityGroups;
	}

	public boolean isHideNameColumnInGrid() {
		return hideNameColumnInGrid;
	}

	public void setHideNameColumnInGrid(boolean hideNameColumnInGrid) {
		this.hideNameColumnInGrid = hideNameColumnInGrid;
	}

	public TaskFlowData getTaskFlowData() {
		return taskFlowData;
	}

	public void setTaskFlow(TaskFlowData taskFlowData) {
		this.taskFlowData = taskFlowData;
	}

	public TaskNavigationTag getNavigationTag() {
		return navigationTag;
	}

	public void setNavigationTag(TaskNavigationTag navigationTag) {
		this.navigationTag = navigationTag;
	}

	public String getGroupExpression() {
		return groupExpression;
	}

	public void setGroupExpression(String groupExpression) {
		this.groupExpression = groupExpression;
	}

	public boolean isPaging() {
		return paging;
	}

	public void setPaging(boolean paging) {
		this.paging = paging;
	}

	public boolean isSuppressSave() {
		return suppressSave;
	}

	public void setSuppressSave(boolean suppressSave) {
		this.suppressSave = suppressSave;
	}

	class FieldErrorFilter implements Predicate<String> {

		@Override
		public boolean apply(String input) {
			return StringUtils.startsWith(input, evaluateOgnl(fieldName));
		}

	}
}
