package net.techreadiness.ui.tags.dataview;

import java.util.List;

import net.techreadiness.ui.tags.ParentTag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataViewFiltersTag extends ParentTag {
	private String viewDefType;
	private String filtersJson;

	@Override
	public String execute() throws Exception {
		List<DataViewFilterTag> filters = getChildren(DataViewFilterTag.class);
		for (DataViewFilterTag filter : filters) {
			filter.execute();
		}
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		filtersJson = gson.toJson(filters);
		return null;
	}

	public String getViewDefType() {
		return viewDefType;
	}

	public void setViewDefType(String viewDefType) {
		this.viewDefType = viewDefType;
	}

	public String getFilters() {
		return filtersJson;
	}
}
