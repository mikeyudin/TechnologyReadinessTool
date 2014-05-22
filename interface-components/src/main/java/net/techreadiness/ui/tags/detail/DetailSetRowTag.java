package net.techreadiness.ui.tags.detail;

import net.techreadiness.ui.tags.BaseTag;

public class DetailSetRowTag extends BaseTag {
	private String title;

	@Override
	public String execute() throws Exception {
		return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
