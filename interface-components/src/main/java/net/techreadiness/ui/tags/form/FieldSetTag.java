package net.techreadiness.ui.tags.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.techreadiness.service.common.ViewColumn;
import net.techreadiness.service.common.ViewComponent;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewField;
import net.techreadiness.ui.tags.ParentTag;
import net.techreadiness.ui.tags.ToolbarTag;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.util.TextParseUtil;

public class FieldSetTag extends ParentTag {
	private String title;
	private ViewDef viewDef;
	private String field;
	private boolean bottomToolbar;
	private boolean editAlertDisabled;
	private boolean readOnly;
	private String var;
	private List<String> hiddenErrors;

	private List<FieldSetColumnTag> columns;

	@Override
	public String execute() throws Exception {
		columns = new ArrayList<>();
		if (field != null) {
			field = TextParseUtil.translateVariables(field, getValueStack());
		}

		int colCount = 1;
		if (var == null) {
			var = field;
		}

		// Process the columns defined by the view definition.
		if (viewDef != null) {
			if (StringUtils.isNotBlank(viewDef.getName()) && StringUtils.isBlank(title)) {
				title = viewDef.getName();
			}

			for (ViewColumn viewColumn : viewDef.getColumns()) {
				FieldSetColumnTag colTag = new FieldSetColumnTag();
				colTag.setWidth(viewColumn.getWidth());
				colTag.setLabelWidth(viewColumn.getLabelWidth());
				colTag.setId("viewColumn" + colCount++);
				colTag.setParent(this);
				for (ViewComponent viewComponent : viewColumn.getComponents()) {
					FieldSetRowTag rowTag = new FieldSetRowTag();
					rowTag.setViewComponent(viewComponent);
					rowTag.setFieldName(field);
					rowTag.setParent(colTag);
					rowTag.setVar(var);
					rowTag.setReadOnly(readOnly);
					if (viewComponent instanceof ViewField) {
						ViewField viewField = (ViewField) viewComponent;
						rowTag.setDescription(viewField.getDescription());
						rowTag.setCode(viewField.getCode());
						if (StringUtils.isNotBlank(colTag.getLabelWidth())) {
							// Currently the label width is specified on the column and needs to be forced to all labels.
							viewField.setLabelStyle(StringUtils.defaultString(viewField.getLabelStyle()) + ";width:"
									+ colTag.getLabelWidth());
						}
					} else {
						rowTag.setCode(viewComponent.getName());
					}
					rowTag.setDisplayOrder(Integer.toString(viewComponent.getDisplayOrder()));
					colTag.addRow(rowTag);
				}
				columns.add(colTag);
			}

		}

		// Add the columns defined on the page.
		for (FieldSetColumnTag col : getChildren(FieldSetColumnTag.class)) {
			col.setId("viewColumn" + colCount++);
			columns.add(col);
		}

		// If they have some rows not included in a column ... add them to the first column.
		FieldSetColumnTag col1 = null;
		int i = 0;
		for (FieldSetRowTag row : getChildren(FieldSetRowTag.class)) {
			if (col1 == null) {
				if (columns.size() == 0) {
					columns.add(new FieldSetColumnTag());
				}
				col1 = columns.get(0);
			}

			String rowCode = row.getCode();
			row.setPageOrder(Integer.valueOf(i));
			if (StringUtils.isBlank(rowCode)) {
				rowCode = Integer.toString(i);
			}
			i++;
			boolean found = false;
			for (FieldSetColumnTag column : columns) {
				if (column.getRowMap().containsKey(row.getCode())) {
					column.getRowMap().put(row.getCode(), row);
					found = true;
					break;
				}
			}
			if (!found) {
				col1.addRow(row);
			}
		}
		detectHiddenValidationErrors();
		return "/form/fieldset.jsp";
	}

	private void detectHiddenValidationErrors() {
		if (viewDef != null) {
			Map<String, List<String>> fieldErrors = Maps.newHashMap((Map<String, List<String>>) getValueStack().findValue(
					"fieldErrors"));
			List<String> displayedFieldNames = Lists.newArrayList();
			for (ViewField viewField : viewDef.getFields()) {
				displayedFieldNames.add(field + "." + viewField.getCode());
			}
			Iterator<String> keyIterator = fieldErrors.keySet().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next();
				if (!StringUtils.startsWith(key, field)) {
					keyIterator.remove();
				} else {
					displayedFieldNames.add(key);
				}
			}
			fieldErrors.keySet().removeAll(displayedFieldNames);
			if (!fieldErrors.isEmpty()) {
				hiddenErrors = Lists.newArrayList();
				for (Entry<String, List<String>> entry : fieldErrors.entrySet()) {
					hiddenErrors.addAll(entry.getValue());
				}
			}
		}
	}

	public List<String> getHiddenErrors() {
		return hiddenErrors;
	}

	public List<ToolbarTag> getToolbars() {
		return getChildren(ToolbarTag.class);
	}

	public boolean isFieldSetRowTag() {
		return true;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setBottomToolbar(boolean bottomToolbar) {
		this.bottomToolbar = bottomToolbar;
	}

	public boolean isBottomToolbar() {
		return bottomToolbar;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public void setEditAlertDisabled(boolean editAlertDisabled) {
		this.editAlertDisabled = editAlertDisabled;
	}

	public boolean isEditAlertDisabled() {
		return editAlertDisabled;
	}

	public List<FieldSetColumnTag> getColumns() {
		return columns;
	}

	public void setColumns(List<FieldSetColumnTag> columns) {
		this.columns = columns;
	}

	public int getColumnCount() {
		return columns == null ? 0 : columns.size();
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

}
