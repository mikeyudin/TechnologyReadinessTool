package net.techreadiness.ui.tags.form;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.techreadiness.ui.tags.ParentTag;

import com.google.common.collect.Lists;

public class FieldSetColumnTag extends ParentTag {
	private String id;
	private String width;
	private String labelWidth;
	private Map<String, FieldSetRowTag> rowMap = new LinkedHashMap<>();

	@Override
	public String execute() throws Exception {
		// Add the columns defined on the page.
		int i = 0;
		for (FieldSetRowTag row : getChildren(FieldSetRowTag.class)) {
			row.setParent(this);
			row.setPageOrder(Integer.valueOf(i++));
			addRow(row);
		}

		return "/form/fieldsetColumn.jsp";
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void addRow(FieldSetRowTag row) {
		rowMap.put(row.getCode(), row);
	}

	public List<FieldSetRowTag> getRows() {
		List<FieldSetRowTag> rowList = Lists.newArrayList(rowMap.values());
		Collections.sort(rowList);
		return rowList;
	}

	public void setRowMap(Map<String, FieldSetRowTag> rows) {
		rowMap = rows;
	}

	public Map<String, FieldSetRowTag> getRowMap() {
		return rowMap;
	}

	public String getLabelWidth() {
		return labelWidth;
	}

	public void setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
