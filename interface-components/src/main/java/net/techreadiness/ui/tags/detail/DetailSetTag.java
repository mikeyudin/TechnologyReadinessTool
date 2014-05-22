package net.techreadiness.ui.tags.detail;

import java.util.List;

import net.techreadiness.ui.tags.ParentTag;

public class DetailSetTag extends ParentTag {
	private boolean collapsible;

	private List<DetailSetRowTag> rows;

	@Override
	public String execute() throws Exception {
		rows = getChildren(DetailSetRowTag.class);
		return "/detail/detailset.jsp";
	}

	public List<DetailSetRowTag> getRows() {
		return rows;
	}

	public boolean isCollapsible() {
		return collapsible;
	}

	public void setCollapsible(boolean collapsible) {
		this.collapsible = collapsible;
	}
}
