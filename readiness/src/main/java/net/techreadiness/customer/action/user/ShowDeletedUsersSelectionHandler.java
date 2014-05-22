package net.techreadiness.customer.action.user;

import net.techreadiness.ui.action.filters.AbstractCheckboxSelectionHandler;

import org.springframework.stereotype.Component;

@Component
public class ShowDeletedUsersSelectionHandler extends AbstractCheckboxSelectionHandler {

	public static final String SHOW_DELETED = "SHOW_DELETED";
	private static final String DATAGRID_STATE = "userGrid";

	@Override
	public String getFilterName() {
		return SHOW_DELETED;
	}

	@Override
	public String getDataGridName() {
		return DATAGRID_STATE;
	}

}
