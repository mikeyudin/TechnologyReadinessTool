package net.techreadiness.ui.tags.taskview;

import net.techreadiness.ui.tags.datagrid.DataGridState;

public class TaskViewState<T> extends DataGridState<T> {
	private static final long serialVersionUID = 1L;

	private boolean detailMode = true;
	private boolean selectAllRows = false;

	public boolean isDetailMode() {
		return detailMode;
	}

	public void setDetailMode(boolean detailMode) {
		this.detailMode = detailMode;
	}

	public boolean isSelectAllRows() {
		return selectAllRows;
	}

	public void setSelectAllRows(boolean selectAllRows) {
		this.selectAllRows = selectAllRows;
	}

}
