package net.techreadiness.ui.tags.datagrid;

import java.util.List;

import net.techreadiness.ui.tags.ParentTag;

public class DataGridColumnGroupTag extends ParentTag {
	private String name;
	private List<DataGridColumnTag> columns;

	@Override
	public String execute() throws Exception {
		columns = getChildren(DataGridColumnTag.class);
		name = evaluateOgnl(name);
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataGridColumnTag> getColumns() {
		return columns;
	}
}
