package net.techreadiness.ui.tags.dataview;

import net.techreadiness.ui.tags.BaseTag;

public class ShoppingCartTag extends BaseTag {
	private String name;

	@Override
	public String execute() throws Exception {
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.evaluateOgnl(name);
	}
}
