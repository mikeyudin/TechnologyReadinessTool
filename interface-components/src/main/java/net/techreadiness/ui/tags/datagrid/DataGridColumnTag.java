package net.techreadiness.ui.tags.datagrid;

import java.util.Comparator;
import java.util.List;

import javax.servlet.jsp.JspException;

import net.techreadiness.service.common.DataGridColumn;
import net.techreadiness.service.common.ViewField;
import net.techreadiness.ui.tags.BaseTag;
import net.techreadiness.ui.tags.ViewFieldDisplayOrderComparator;
import net.techreadiness.ui.tags.ViewFieldTag;
import net.techreadiness.ui.tags.form.FieldSetRowTag;

import org.apache.commons.lang3.StringUtils;

public class DataGridColumnTag extends BaseTag implements DataGridColumn, ViewFieldTag {
	private static final Comparator<ViewFieldTag> comparator = new ViewFieldDisplayOrderComparator();
	private String name;
	private String nameKey;
	private String width;
	private ViewField field;
	private RowInfo<?> rowInfo;
	private boolean lastColumn;
	private boolean displayed;
	private boolean header;
	private boolean hideErrors = true;
	private String code;
	private boolean hidden;
	private String description;
	private String displayOrder;
	private boolean required;
	private Integer pageOrder;
	private boolean grouped;
	private String style;
	private String headerStyle;
	private String manageColumnsText;
	private boolean manageable = true;

	@Override
	public String execute() throws JspException {
		name = evaluateOgnl(name);
		code = evaluateOgnl(code);

		DataGridTag<?> dataGridTag = getRequiredParentTag(DataGridTag.class);
		DataGridState<?> state = dataGridTag.getState();
		List<String> displayedColumns = state.getDisplayedColumns();

		if (isManageable()) {
			if (displayedColumns == null) {
				// only display columns by default if it is hardcoded or the field
				// is in Column 1
				displayed = field == null || field.getColumnNumber() == null || field.getColumnNumber() == 1;
			} else {
				displayed = displayedColumns.contains(code) || StringUtils.isEmpty(name); // allow hidden in list
			}
		} else {
			displayed = true;
		}

		return "/dataGrid/dataGridColumn.jsp";

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (code == null ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DataGridColumnTag)) {
			return false;
		}
		DataGridColumnTag other = (DataGridColumnTag) obj;

		if (getParent() == null) {
			if (other.getParent() != null) {
				return false;
			}
		} else if (!getParent().equals(other.getParent())) {
			return false;
		}

		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public DataGridTag<?> getBaseParent() {
		return getRequiredParentTag(DataGridTag.class);
	}

	public FieldSetRowTag getEditField() {
		FieldSetRowTag column = new FieldSetRowTag();
		column.setJspContext(getJspContext());
		column.setParent(this);
		column.setFieldName(getBaseParent().getVar());
		column.setName(field.getName());
		column.setViewComponent(field);
		return column;
	}

	public String getId() {
		return code;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getWidth() {
		return width;
	}

	public ViewField getField() {
		return field;
	}

	public void setField(ViewField field) {
		this.field = field;
	}

	public void setRowInfo(RowInfo<?> rowInfo) {
		this.rowInfo = rowInfo;
	}

	public RowInfo<?> getRowInfo() {
		return rowInfo;
	}

	public void setLastColumn(Boolean lastColumn) {

		this.lastColumn = lastColumn != null && lastColumn.booleanValue();
	}

	public boolean isLastColumn() {
		return lastColumn;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	public boolean isHeader() {
		return header;
	}

	public void setHideErrors(boolean hideErrors) {
		this.hideErrors = hideErrors;
	}

	public boolean isHideErrors() {
		return hideErrors;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getDisplayOrder() {
		return displayOrder;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public int compareTo(ViewFieldTag o) {
		return comparator.compare(this, o);
	}

	@Override
	public Integer getPageOrder() {
		return pageOrder;
	}

	@Override
	public void setPageOrder(Integer pageOrder) {
		this.pageOrder = pageOrder;
	}

	public boolean isGrouped() {
		return grouped;
	}

	public void setGrouped(boolean grouped) {
		this.grouped = grouped;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getHeaderStyle() {
		return headerStyle;
	}

	public void setHeaderStyle(String headerStyle) {
		this.headerStyle = headerStyle;
	}

	public String getManageColumnsText() {
		if (StringUtils.isBlank(manageColumnsText)) {
			return name;
		}
		return manageColumnsText;
	}

	public void setManageColumnsText(String manageColumnsText) {
		this.manageColumnsText = manageColumnsText;
	}

	public boolean isManageable() {
		return manageable;
	}

	public void setManageable(boolean manageable) {
		this.manageable = manageable;
	}

}
