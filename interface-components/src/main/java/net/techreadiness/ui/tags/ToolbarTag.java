package net.techreadiness.ui.tags;

public class ToolbarTag extends BaseTag {
	private String visibleModes;

	@Override
	public String execute() throws Exception {
		return null;
	}

	public void setVisibleModes(String visibleModes) {
		this.visibleModes = visibleModes;
	}

	public String getVisibleModes() {
		return visibleModes;
	}

}
