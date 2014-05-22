package net.techreadiness.customer.action.organization;

import net.techreadiness.ui.action.filters.AbstractCheckboxSelectionHandler;

import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Scope("prototype")
public class ShowAllOrganizationsSelectionHandler extends AbstractCheckboxSelectionHandler {

	public static final String SHOW_ALL = "SHOW_ALL";
	private static final String DATAGRID_STATE = "orgSearchGrid";

	@Override
	public String getFilterName() {
		return SHOW_ALL;
	}

	@Override
	public String getDataGridName() {
		return DATAGRID_STATE;
	}

}
