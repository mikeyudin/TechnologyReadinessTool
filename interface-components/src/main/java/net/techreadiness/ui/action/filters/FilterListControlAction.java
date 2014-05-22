package net.techreadiness.ui.action.filters;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.service.ConfigService;
import net.techreadiness.service.common.ViewDef;
import net.techreadiness.service.common.ViewDef.ViewDefTypeCode;
import net.techreadiness.service.common.ViewField;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.Conversation;
import net.techreadiness.ui.util.ConversationAware;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/filterList")
public class FilterListControlAction extends BaseAction implements ConversationAware, ApplicationContextAware {
	private static final long serialVersionUID = 1L;

	ViewDef viewDef;

	private List<ViewField> activeFields;
	private List<ViewField> inactiveFields;

	private List<String> displayedFilters = Lists.newArrayList();

	private Map<String, Collection<String>> existingFilters;
	private Map<String, String> filters;
	private List<Map<String, String>> jsonItems;

	private String filterName;
	private String filterValue;
	private String updateField;

	private String dataGridId;

	@Inject
	ConfigService configService;

	private Conversation conversation;

	private String viewDefType;

	private boolean fullTextShown;

	private String search;

	private ApplicationContext applicationContext;

	private String dataViewFilters;
	private String beanName;
	private String nameKey;
	private String valueKey;

	// incoming filter value to save
	private String updateDataViewFilter;

	// incoming filter value to save
	private Map<String, String> dataViewFilterValues;

	@Action(value = "show", results = @Result(name = SUCCESS, location = "/filters/filterList/filters.jsp"))
	public String show() {
		activeFields = Lists.newArrayList();
		inactiveFields = Lists.newArrayList();

		if (StringUtils.isNotBlank(viewDefType)) {
			viewDef = configService.getViewDefinition(getServiceContext(), ViewDefTypeCode.valueOf(viewDefType));
			for (ViewField field : viewDef.getFields()) {
				if (displayFilter(field)) {
					getActiveFields().add(field);
				} else {
					getInactiveFields().add(field);
					getDataGridState().getFilters().removeAll(field.getCode());
				}
			}

		}
		existingFilters = getDataGridState().getFilters().asMap();
		search = getDataGridState().getSearch();
		return SUCCESS;
	}

	@Action(value = "manage", results = @Result(name = SUCCESS, location = "/filters/filterList/manage.jsp"))
	public String manage() {
		return show();
	}

	@Action(value = "updateDisplayed", results = @Result(name = SUCCESS, type = "redirect", location = "show", params = {
			"ajax", "true", "dataGridId", "%{dataGridId}", "viewDefType", "%{viewDefType}", "fullTextShown",
			"%{fullTextShown}", "dataViewFilters", "%{dataViewFilters}" }))
	public String updateDisplayed() {
		getDataGridState().setDisplayedFilters(Lists.newArrayList(displayedFilters));
		setExpanded(true);
		return SUCCESS;
	}

	@Action(value = "clear", results = @Result(name = SUCCESS, type = "redirect", location = "show", params = { "ajax",
			"true", "dataGridId", "%{dataGridId}", "viewDefType", "%{viewDefType}", "fullTextShown", "%{fullTextShown}",
			"dataViewFilters", "%{dataViewFilters}" }))
	public String clear() {
		getDataGridState().getFilters().clear();
		getDataGridState().setSearch("");
		return SUCCESS;
	}

	private boolean displayFilter(ViewField viewField) {
		if (getDataGridState() == null || getDataGridState().getDisplayedFilters() == null) {
			return true;
		}
		for (String filter : getDataGridState().getDisplayedFilters()) {
			if (filter.equals(viewField.getCode())) {
				return true;
			}
		}
		return false;
	}

	@Action(value = "update", results = @Result(name = SUCCESS, type = "redirect", location = "show", params = { "ajax",
			"true", "dataGridId", "%{dataGridId}", "viewDefType", "%{viewDefType}", "fullTextShown", "%{fullTextShown}",
			"dataViewFilters", "%{dataViewFilters}" }))
	public String update() {
		if (StringUtils.isNotBlank(updateField)) {
			String incomingFilter = getFilters().get(updateField);
			if (StringUtils.isNotBlank(incomingFilter)) {
				getDataGridState().getFilters().put(updateField, incomingFilter);
			}
		} else if (StringUtils.isNotBlank(updateDataViewFilter)) {
			String incomingFilter = dataViewFilterValues.get(updateDataViewFilter);

			if (StringUtils.isNotBlank(incomingFilter) && StringUtils.isNumeric(incomingFilter)) {
				getSelectionHandler(updateDataViewFilter).add(Long.valueOf(incomingFilter));
			} else {
				getSelectionHandler(updateDataViewFilter).clear();
			}
		}
		getDataGridState().setSearch(search);
		return SUCCESS;
	}

	@Action(value = "search", results = @Result(name = SUCCESS, type = "redirect", location = "show", params = { "ajax",
			"true", "dataGridId", "%{dataGridId}", "viewDefType", "%{viewDefType}", "fullTextShown", "%{fullTextShown}",
			"dataViewFilters", "%{dataViewFilters}" }))
	public String search() {
		getDataGridState().setSearch(search);
		return SUCCESS;
	}

	@Action(value = "removeFilter", results = @Result(name = SUCCESS, type = "redirect", location = "show", params = {
			"ajax", "true", "dataGridId", "%{dataGridId}", "viewDefType", "%{viewDefType}", "fullTextShown",
			"%{fullTextShown}", "dataViewFilters", "%{dataViewFilters}" }))
	public String removeFilter() {
		getDataGridState().getFilters().get(filterName).remove(filterValue);
		return SUCCESS;
	}

	@Action(value = "removeDataViewFilter", results = @Result(name = SUCCESS, type = "redirect", location = "show", params = {
			"ajax", "true", "dataGridId", "%{dataGridId}", "viewDefType", "%{viewDefType}", "fullTextShown",
			"%{fullTextShown}", "dataViewFilters", "%{dataViewFilters}" }))
	public String removeDataViewFilter() {
		getSelectionHandler().remove(Long.valueOf(filterValue));
		return SUCCESS;
	}

	@Action(value = "filterOptions", results = { @Result(type = "json", params = { "root", "jsonItems" }) })
	public String filterOptions() throws OgnlException {
		FilterSelectionHandler<?> selectionHandler = getSelectionHandler();
		List<?> items = selectionHandler.getList(ActionContext.getContext().getParameters());
		jsonItems = Lists.newArrayList();

		for (Object item : items) {
			Map<String, String> itemMap = Maps.newHashMap();
			itemMap.put("label", (String) Ognl.getValue(nameKey, item, String.class));
			itemMap.put("value", (String) Ognl.getValue(valueKey, item, String.class));
			jsonItems.add(itemMap);
		}

		return SUCCESS;
	}

	public List<?> getSelectedItems(String beanName) {
		return getSelectionHandler(beanName).getSelection();
	}

	@Override
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private FilterSelectionHandler<?> getSelectionHandler() {
		return getSelectionHandler(beanName);
	}

	private FilterSelectionHandler<?> getSelectionHandler(String beanName) {
		FilterSelectionHandler<?> selectionHandler = applicationContext.getBean(beanName, FilterSelectionHandler.class);
		if (selectionHandler instanceof ConversationAware) {
			((ConversationAware) selectionHandler).setConversation(conversation);
		}
		if (selectionHandler instanceof DataGridAware) {
			((DataGridAware) selectionHandler).setDataGrid(getDataGridState());
		}
		return selectionHandler;
	}

	public DataGridState<?> getDataGridState() {
		return conversation.get(DataGridState.class, dataGridId);
	}

	private String getExpandedSessionParam() {
		return dataGridId + "filterexpanded";
	}

	public boolean isExpanded() {
		return Boolean.TRUE.equals(getSession().get(getExpandedSessionParam()));
	}

	public void setExpanded(boolean expanded) {
		getSession().put(getExpandedSessionParam(), expanded);
	}

	public List<?> getDataViewFilterOptions(String beanName) {
		List<?> list = getSelectionHandler(beanName).getList(getSession());
		return list;
	}

	public void setViewDef(ViewDef viewDef) {
		this.viewDef = viewDef;
	}

	public ViewDef getViewDef() {
		return viewDef;
	}

	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}

	public Map<String, String> getFilters() {
		return filters;
	}

	public Map<String, Collection<String>> getExistingFilters() {
		return existingFilters;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setUpdateField(String updateField) {
		this.updateField = updateField;
	}

	public String getUpdateField() {
		return updateField;
	}

	public void setDataGridId(String dataGridId) {
		this.dataGridId = dataGridId;
	}

	public String getDataGridId() {
		return dataGridId;
	}

	public String getViewDefType() {
		return viewDefType;
	}

	public void setViewDefType(String viewDefType) {
		this.viewDefType = viewDefType;
	}

	public List<ViewField> getActiveFields() {
		return activeFields;
	}

	public void setActiveFields(List<ViewField> activeFields) {
		this.activeFields = activeFields;
	}

	public List<ViewField> getInactiveFields() {
		return inactiveFields;
	}

	public void setInactiveFields(List<ViewField> inactiveFields) {
		this.inactiveFields = inactiveFields;
	}

	public List<String> getDisplayedFilters() {
		return displayedFilters;
	}

	public void setDisplayedFilters(List<String> displayedFilters) {
		this.displayedFilters = displayedFilters;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public boolean isFullTextShown() {
		return fullTextShown;
	}

	public void setFullTextShown(boolean fullTextShown) {
		this.fullTextShown = fullTextShown;
	}

	public List<Map<String, String>> getJsonItems() {
		return jsonItems;
	}

	public void setJsonItems(List<Map<String, String>> jsonItems) {
		this.jsonItems = jsonItems;
	}

	public List<?> getSelectedItems() {
		return getSelectionHandler().getSelection();
	}

	public String getDataViewFilters() {
		return dataViewFilters;
	}

	public void setDataViewFilters(String dataViewFilters) {
		this.dataViewFilters = dataViewFilters;
	}

	public List<Map<String, Object>> getDataViewFiltersAsObject() {
		if (StringUtils.isBlank(dataViewFilters)) {
			return Collections.emptyList();
		}
		Gson gson = new GsonBuilder().create();
		Type filterType = new TypeToken<List<Map<String, Object>>>() {
			// Define a type for JSON parsing
		}.getType();
		return gson.fromJson(dataViewFilters, filterType);
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getValueKey() {
		return valueKey;
	}

	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public String getUpdateDataViewFilter() {
		return updateDataViewFilter;
	}

	public void setUpdateDataViewFilter(String updateDataViewFilter) {
		this.updateDataViewFilter = updateDataViewFilter;
	}

	public Map<String, String> getDataViewFilterValues() {
		return dataViewFilterValues;
	}

	public void setDataViewFilterValues(Map<String, String> dataViewFilterValues) {
		this.dataViewFilterValues = dataViewFilterValues;
	}
}
