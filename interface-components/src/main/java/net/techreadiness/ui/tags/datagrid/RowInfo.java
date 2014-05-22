package net.techreadiness.ui.tags.datagrid;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

public class RowInfo<T> {
	private final DataGridTag<T> dataGrid;
	private final T row;
	private final boolean editMode;
	private final String rowId;
	private final boolean rowSelected;
	private Map<String, List<String>> fieldErrors;
	private boolean rowInError;
	private T errorRow;
	private boolean selectionDisabled = false;

	private String defaultClass = "standard-row";
	private String selectedClass = "rowSelected";
	private String errorClass = "rowErrors";
	private String inlineEditableClass = "inlineEditable";
	private String inlineEditingClass = "inlineEdit";
	private String fieldNamePrefix;
	private boolean multiEdit;

	public RowInfo(DataGridTag<T> dataGrid, T row) {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		this.dataGrid = dataGrid;
		this.editMode = dataGrid.isEditMode(row);
		this.rowId = dataGrid.getRowId(row);
		this.rowSelected = dataGrid.isRowSelected(row);
		this.multiEdit = dataGrid.isMultiEditable();
		this.row = row;

		if (this.dataGrid.getSelectedDisabledTest() != null) {// this only gets
																// run if the
																// datagrid has
																// the attribute
			try {
				valueStack.push(row);
				Map<String, Object> pushVals = Maps.newHashMap();
				pushVals.put(this.dataGrid.getVar(), row);
				try {
					valueStack.push(pushVals);
					if ("true".equals(TextParseUtil.translateVariables('%', this.dataGrid.getSelectedDisabledTest(),
							valueStack))) {
						selectionDisabled = true;
					}
				} finally {
					valueStack.pop();
				}
			} finally {
				valueStack.pop();
			}
		}

		if (StringUtils.isEmpty(dataGrid.getFieldName())) {
			fieldNamePrefix = dataGrid.getVar();
			setErrorInfo();
		} else {
			try {
				valueStack.push(row);
				Map<String, Object> rowMap = Maps.newHashMap();
				rowMap.put(dataGrid.getVar(), row);
				try {
					valueStack.push(rowMap);
					fieldNamePrefix = TextParseUtil.translateVariables('%', dataGrid.getFieldName(), valueStack);
					setErrorInfo();
				} finally {
					valueStack.pop();
				}
			} finally {
				valueStack.pop();
			}
		}

	}

	public boolean isGroupChanged() {
		if (StringUtils.isBlank(dataGrid.getGroupExpression())) {
			return true;
		}
		String currentGroupValue = TextParseUtil.translateVariables('%',
				StringUtils.defaultString(dataGrid.getGroupExpression()), ActionContext.getContext().getValueStack());
		boolean groupChanged = !currentGroupValue.equals(dataGrid.getPrevGroupValue());
		dataGrid.setPrevGroupValue(currentGroupValue);
		return groupChanged;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setErrorInfo() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		Map<String, List<String>> errors = (Map<String, List<String>>) valueStack.findValue("fieldErrors");
		fieldErrors = getErrorsForCurrentRow(errors);

		this.rowInError = editMode && fieldErrors != null && !fieldErrors.isEmpty();
		if (rowInError) {
			T errorData = (T) valueStack.findValue(fieldNamePrefix);
			if (row instanceof Map) {
				Map<?, ?> rowData = (Map<?, ?>) row;
				rowData.putAll((Map) errorData);
				errorRow = (T) rowData;
			} else {
				merge(errorData, row);
				this.errorRow = row;
			}
		} else {
			this.errorRow = row;
		}
	}

	private void merge(T target, T destination) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());

			// Iterate over all the attributes
			for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

				// Only copy writable attributes
				if (descriptor.getWriteMethod() != null) {
					Object originalValue = descriptor.getReadMethod().invoke(target);

					// Only copy values values where the destination values is
					// null
					if (originalValue == null) {
						Object defaultValue = descriptor.getReadMethod().invoke(destination);
						descriptor.getWriteMethod().invoke(target, defaultValue);
					}

				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isRowInError() {
		return rowInError;
	}

	public String getRowClass() {
		StringBuilder rowClass = new StringBuilder();
		rowClass.append(defaultClass);
		rowClass.append(" ").append(getEditClass());
		rowClass.append(" ").append(getDisplayClass());

		return rowClass.toString();
	}

	public String getEditClass() {
		if (editMode && !multiEdit) {
			if (isRowInError()) {
				return errorClass;
			}
			return inlineEditingClass;
		} else if (dataGrid.isInlineEditable() && !dataGrid.isInlineEditing()) {
			return inlineEditableClass;
		} else {
			return "";
		}
	}

	public String getDisplayClass() {
		if (rowSelected) {
			return selectedClass;

		}
		return "";
	}

	public String getColumnEditClass() {
		if (editMode) {
			return inlineEditingClass;
		} else if (dataGrid.isInlineEditable() && !dataGrid.isInlineEditing()) {
			return inlineEditableClass;
		}
		return "";
	}

	public T getRow() {
		return row;
	}

	public String getDefaultClass() {
		return defaultClass;
	}

	public void setDefaultClass(String defaultClass) {
		this.defaultClass = defaultClass;
	}

	public String getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(String selectedClass) {
		this.selectedClass = selectedClass;
	}

	public String getErrorClass() {
		return errorClass;
	}

	public void setErrorClass(String errorClass) {
		this.errorClass = errorClass;
	}

	public String getInlineEditableClass() {
		return inlineEditableClass;
	}

	public void setInlineEditableClass(String inlineEditableClass) {
		this.inlineEditableClass = inlineEditableClass;
	}

	public String getInlineEditingClass() {
		return inlineEditingClass;
	}

	public void setInlineEditingClass(String inlineEditingClass) {
		this.inlineEditingClass = inlineEditingClass;
	}

	public DataGridTag<T> getDataGrid() {
		return dataGrid;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public String getRowId() {
		return rowId;
	}

	public boolean isRowSelected() {
		return rowSelected;
	}

	public Map<?, ?> getFieldErrors() {
		return fieldErrors;
	}

	public T getErrorRow() {
		return errorRow;
	}

	protected Map<String, List<String>> getErrorsForCurrentRow(Map<String, List<String>> errors) {
		return Maps.filterKeys(errors, new FieldErrorFilter());
	}

	public void setSelectionDisabled(boolean selectionDisabled) {
		this.selectionDisabled = selectionDisabled;
	}

	public boolean isSelectionDisabled() {
		return selectionDisabled;
	}

	class FieldErrorFilter implements Predicate<String> {

		@Override
		public boolean apply(String input) {
			return StringUtils.startsWith(input, fieldNamePrefix);
		}

	}
}
