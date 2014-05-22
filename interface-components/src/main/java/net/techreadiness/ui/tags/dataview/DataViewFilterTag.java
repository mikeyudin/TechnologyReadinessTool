package net.techreadiness.ui.tags.dataview;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.annotations.Expose;

public class DataViewFilterTag extends DataViewControlTag {
	@Expose
	private String beanName;
	@Expose
	private String valueKey = "value";
	@Expose
	private String nameKey = "name";
	@Expose
	private boolean multiple;
	@Expose
	private boolean promptIfEmpty;

	@Expose
	private String type = "autocomplete";

	@Expose
	private boolean primary = false;

	@Override
	public String execute() throws Exception {
		setContentAction("show");
		setConfigAction("showOptions");
		setNamespace("/controls");
		String filterCode = getDataGridId() + "." + getCode();
		GenericFilterState state = new GenericFilterState();
		state.setBeanName(beanName);
		state.setMultiple(multiple);
		state.setPromptIfEmpty(promptIfEmpty);
		state.setValueKey(valueKey);
		state.setNameKey(nameKey);
		getSession().setAttribute(filterCode, state);

		getDynamicAttributes().put("filterCode", filterCode);
		String result = super.execute();
		return result;
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

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	@Override
	public boolean isPromptIfEmpty() {
		return promptIfEmpty;
	}

	@Override
	public void setPromptIfEmpty(boolean promptIfEmpty) {
		this.promptIfEmpty = promptIfEmpty;
	}

	public String getNameKey() {
		if (StringUtils.isBlank(nameKey)) {
			nameKey = "name";
		}
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

}
