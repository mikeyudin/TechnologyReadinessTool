package net.techreadiness.plugin.action.org.dataentry;

import javax.inject.Named;

import net.techreadiness.ui.action.filters.AbstractCheckboxSelectionHandler;

import org.springframework.context.annotation.Scope;

@Named
@Scope("prototype")
public class SearchAllDataEntryHandler extends AbstractCheckboxSelectionHandler {

	@Override
	public String getFilterName() {
		return "SHOW_ALL";
	}

	@Override
	public String getDataGridName() {
		return "dataEntrySearchGrid";
	}

}
