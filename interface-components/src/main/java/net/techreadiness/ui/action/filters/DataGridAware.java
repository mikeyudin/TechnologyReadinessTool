package net.techreadiness.ui.action.filters;

import net.techreadiness.service.common.DataGrid;

public interface DataGridAware {
	void setDataGrid(DataGrid<?> grid);
}
