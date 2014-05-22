package net.techreadiness.ui.tags.dataview;

import java.io.Serializable;

public class GenericFilterState implements Serializable {
	private static final long serialVersionUID = 1L;
	private String beanName;
	private String valueKey;
	private String nameKey;
	private boolean multiple;
	private boolean promptIfEmpty;

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

	public boolean isPromptIfEmpty() {
		return promptIfEmpty;
	}

	public void setPromptIfEmpty(boolean promptIfEmpty) {
		this.promptIfEmpty = promptIfEmpty;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

}
