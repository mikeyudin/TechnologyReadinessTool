package net.techreadiness.ui.tags.dataview;

import net.techreadiness.ui.tags.ParentTag;
import net.techreadiness.ui.tags.ToolbarTag;

import com.google.common.collect.Iterables;
import com.google.gson.annotations.Expose;

public class DataViewControlTag extends ParentTag {
	@Expose
	String code;
	@Expose
	String name;
	String configLinkName;
	String configAction;
	String contentAction;
	String namespace;
	@Expose
	String dataGridId;
	String side = "left";
	int number;
	private boolean configPopup = true;
	private boolean configFullPopup = false;

	private boolean displayed = true;
	private boolean promptIfEmpty = false;
	private ToolbarTag toolbar;

	@Override
	public String execute() throws Exception {
		name = evaluateOgnl(name);
		configLinkName = evaluateOgnl(configLinkName);

		toolbar = Iterables.getFirst(getChildren(ToolbarTag.class), null);
		if (displayed) {
			return "/dataView/dataViewControl.jsp";
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isLeftSide() {
		return side.equals("left");
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfigLinkName() {
		return configLinkName;
	}

	public void setConfigLinkName(String configLinkName) {
		this.configLinkName = configLinkName;
	}

	public String getConfigAction() {
		return configAction;
	}

	public void setConfigAction(String configAction) {
		this.configAction = configAction;
	}

	public String getContentAction() {
		return contentAction;
	}

	public void setContentAction(String contentAction) {
		this.contentAction = contentAction;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getDataGridId() {
		return dataGridId;
	}

	public void setDataGridId(String dataGridId) {
		this.dataGridId = dataGridId;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public boolean isConfigPopup() {
		return configPopup;
	}

	public void setConfigPopup(boolean configPopup) {
		this.configPopup = configPopup;
	}

	public boolean isPromptIfEmpty() {
		return promptIfEmpty;
	}

	public void setPromptIfEmpty(boolean promptIfEmpty) {
		this.promptIfEmpty = promptIfEmpty;
	}

	public boolean getConfigFullPopup() {
		return configFullPopup;
	}

	public void setConfigFullPopup(boolean configFullPopup) {
		this.configFullPopup = configFullPopup;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ToolbarTag getToolbar() {
		return toolbar;
	}
}
