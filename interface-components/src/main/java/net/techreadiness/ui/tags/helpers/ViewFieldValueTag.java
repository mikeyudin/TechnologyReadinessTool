package net.techreadiness.ui.tags.helpers;

import net.techreadiness.service.common.ViewComponent;
import net.techreadiness.service.common.ViewField;
import net.techreadiness.ui.tags.BaseTag;

import com.opensymphony.xwork2.util.TextParseUtil;

public class ViewFieldValueTag extends BaseTag {

	private ViewComponent field;
	private String rowVar;
	private String actionFieldName;
	private boolean valueOnly = false;
	private boolean hideLabel = false;
	private boolean hideTooltip = false;
	private boolean hideErrors = false;
	private boolean readOnly = false;

	@Override
	public String execute() throws Exception {
		return "/helper/viewFieldValue.jsp";
	}

	public void setRowVar(String rowVar) {
		this.rowVar = rowVar;
	}

	public String getRowVar() {
		return rowVar;
	}

	public void setValueOnly(boolean valueOnly) {
		this.valueOnly = valueOnly;
	}

	public boolean isValueOnly() {
		return valueOnly;
	}

	public void setActionFieldName(String actionFieldName) {
		this.actionFieldName = actionFieldName;
	}

	public String getActionFieldName() {
		return actionFieldName;
	}

	public String getEvaluatedActionFieldName() {
		String value = TextParseUtil.translateVariables('%', actionFieldName, getValueStack());
		return value;
	}

	public void setHideLabel(boolean hideLabel) {
		this.hideLabel = hideLabel;
	}

	public boolean isHideLabel() {
		return hideLabel;
	}

	public void setHideErrors(boolean hideErrors) {
		this.hideErrors = hideErrors;
	}

	public boolean isHideErrors() {
		return hideErrors;
	}

	public boolean isReadOnly() {
		// Either forced to be readonly by the fieldset, or the
		// field is configured to be readonly
		if (!readOnly && field instanceof ViewField) {
			return ((ViewField) field).isReadOnly();
		}
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public ViewComponent getField() {
		return field;
	}

	public void setField(ViewComponent field) {
		this.field = field;
	}

	public boolean isHideTooltip() {
		return hideTooltip;
	}

	public void setHideTooltip(boolean hideTooltip) {
		this.hideTooltip = hideTooltip;
	}
}
