package net.techreadiness.service.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ViewComponent> components = new ArrayList<>();
	String width; // entered by user ... should be a CSS width (33%, 33px, etc)
	String labelWidth; // entered by user ... should be a CSS width (33%, 33px, etc)

	public List<ViewComponent> getComponents() {
		return components;
	}

	public void add(ViewComponent component) {
		components.add(component);
	}

	public void setComponents(List<ViewComponent> components) {
		this.components = components;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getLabelWidth() {
		return labelWidth;
	}

	public void setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
	}
}
