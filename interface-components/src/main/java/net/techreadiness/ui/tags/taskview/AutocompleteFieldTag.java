package net.techreadiness.ui.tags.taskview;

import net.techreadiness.ui.tags.ParentTag;

import org.apache.commons.lang3.StringUtils;

public class AutocompleteFieldTag<T> extends ParentTag {

	private String labelKey;
	private String name;
	private String namespace;
	private String loadAction;
	private String showAction;
	private boolean required;
	private String instanceId;
	private boolean multiple;

	@Override
	public String execute() throws Exception {
		return "/taskView/autocompleteField.jsp";
	}

	public String getLabelKey() {
		return labelKey;
	}

	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}

	public String getName() {
		if (StringUtils.isNotEmpty(instanceId)) {
			return name + instanceId;
		}
		return name;
	}

	public String getOriginalName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getLoadAction() {
		return loadAction;
	}

	public void setLoadAction(String loadAction) {
		this.loadAction = loadAction;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getShowAction() {
		return showAction;
	}

	public void setShowAction(String showAction) {
		this.showAction = showAction;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
}
