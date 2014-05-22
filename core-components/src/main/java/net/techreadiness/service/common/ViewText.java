package net.techreadiness.service.common;

import net.techreadiness.persistence.domain.ViewDefTextDO;

/**
 * Defines the display and validation characteristics for entity's attribute.
 */
public class ViewText extends ViewComponent {
	private static final long serialVersionUID = 1L;
	private Integer viewDefTextId;
	private Integer columnNumber;
	private String text;

	public ViewText() {
	}

	public ViewText(ViewDefTextDO viewText) {
		this.setName(Integer.toString(viewText.getViewDefTextId()));
		viewDefTextId = viewText.getViewDefTextId();
		columnNumber = viewText.getColumnNumber();
		displayOrder = viewText.getDisplayOrder();
		text = viewText.getText();
	}

	public Integer getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getViewDefTextId() {
		return viewDefTextId;
	}

	public void setViewDefTextId(Integer viewDefTextId) {
		this.viewDefTextId = viewDefTextId;
	}
}
