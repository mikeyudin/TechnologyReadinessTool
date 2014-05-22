package net.techreadiness.service.common;

import java.util.ArrayList;
import java.util.List;

public class ViewFieldGroup extends ViewComponent {
	private static final long serialVersionUID = 1L;

	private List<ViewColumn> columns = new ArrayList<>();
	{
		columns.add(new ViewColumn());
	}
	boolean collapsible = false;

	public List<ViewColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ViewColumn> columns) {
		this.columns = columns;
	}

	public boolean isCollapsible() {
		return collapsible;
	}

	public void setCollapsible(boolean collapsible) {
		this.collapsible = collapsible;
	}
}
