package net.techreadiness.service.common;

import java.util.LinkedHashMap;
import java.util.Map;

import net.techreadiness.persistence.domain.EntityFieldDO;
import net.techreadiness.persistence.domain.ViewDefFieldDO;

import org.apache.commons.lang3.StringUtils;

/**
 * Defines the display and validation characteristics for entity's attribute.
 */
public class ViewField extends ViewComponent {
	private static final long serialVersionUID = 1L;

	public static enum LabelPosition {
		STANDARD, HIDDEN, AFTER
	}

	public static enum DataType {
		STRING, NUMBER, BOOLEAN;
	}

	public static enum InputType {
		DROPDOWN, RADIO;
	}

	private String overrideName;
	private Long entityFieldId;
	private Long viewDefFieldId;
	private String code;
	private String description;
	private Integer maxLength;
	private Integer minLength;
	private String regex;
	private String regexDisplay;
	private Map<String, String> options = new LinkedHashMap<>();
	private boolean readOnly;
	private boolean visible;
	private boolean required;
	private boolean sortable;
	private Integer columnNumber;
	private String labelPosition;
	private String labelStyle;
	private String inputStyle;
	private String dataType;
	private String inputType;
	private String displayWidth;

	public ViewField() {

	}

	public ViewField(EntityFieldDO ef, ViewField vf) {
		this(ef);
		setDisplayOrder(vf.getDisplayOrder());
		setInputType(vf.getInputType());
		setColumnNumber(vf.getColumnNumber());
		setLabelPosition(vf.getLabelPosition());
		setDisplayWidth(vf.getDisplayWidth());
		setLabelStyle(vf.getLabelStyle());
		setInputStyle(vf.getInputStyle());
		setViewDefFieldId(vf.getViewDefFieldId());
		setOverrideName(vf.getOverrideName());
		setReadOnly(vf.isReadOnly());
		setDisplayRuleId(vf.getDisplayRuleId());
		setEditRuleId(vf.getEditRuleId());
		if (StringUtils.isNotBlank(vf.getOverrideName())) {
			setName(vf.getOverrideName());
		}
	}

	public ViewField(EntityFieldDO ef, ViewDefFieldDO vdf) {
		this(ef);
		setOverrideName(vdf.getOverrideName());
		if (StringUtils.isNotBlank(vdf.getOverrideName())) {
			setName(vdf.getOverrideName());
		}
		setDisplayOrder(vdf.getDisplayOrder());
		setReadOnly(vdf.getReadOnly());
		setInputType(vdf.getInputType());
		setColumnNumber(vdf.getColumnNumber());
		setLabelPosition(vdf.getLabelPosition());
		setDisplayWidth(vdf.getDisplayWidth());
		setLabelStyle(vdf.getLabelStyle());
		setInputStyle(vdf.getInputStyle());
		setViewDefFieldId(vdf.getViewDefFieldId());
		if (vdf.getDisplayRule() != null) {
			setDisplayRuleId(vdf.getDisplayRule().getEntityRuleId());
		}
		if (vdf.getEditRule() != null) {
			setEditRuleId(vdf.getEditRule().getEntityRuleId());
		}
	}

	public ViewField(EntityFieldDO ef) {
		super();
		setEntityFieldId(ef.getEntityFieldId());
		setCode(ef.getCode());
		setName(ef.getName());
		setDescription(ef.getDescription());
		setMaxLength(ef.getMaxLength());
		setMinLength(ef.getMinLength());
		setRegex(ef.getRegex());
		setRegexDisplay(ef.getRegexDisplay());
		if (ef.getOptionList() != null) {
			setOptions(ef.getOptionList().getOptionListAsMap());
		}
		setDataType(ef.getEntityDataType().getCode());
		setRequired(ef.getRequired());
		setVisible(true);
		setReadOnly(false);
		setDisplayOrder(-1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(code);
		sb.append("(");
		sb.append("name=" + name);
		sb.append(", type=" + dataType);
		sb.append(", order=" + displayOrder);
		sb.append(", req=" + required);
		sb.append(", vis=" + visible);
		sb.append(", ro=" + readOnly);
		sb.append(", length=" + (minLength == null ? 0 : minLength.intValue()));
		sb.append(", sortable=" + sortable);
		sb.append("-" + (maxLength == null ? "na" : maxLength));
		if (options != null && options.size() > 0) {
			sb.append(", options=[" + StringUtils.join(options.entrySet().toArray()) + "]");
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {

		if (o instanceof ViewField) {
			ViewField ea = (ViewField) o;
			if (getName().equals(ea.getName())) {
				return true;
			}
		}
		return false;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getName() == null ? 0 : getName().hashCode());
		return result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRegexDisplay() {
		return regexDisplay;
	}

	public void setRegexDisplay(String regexDisplay) {
		this.regexDisplay = regexDisplay;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Map<String, String> getOptions() {
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Integer getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	public Long getEntityFieldId() {
		return entityFieldId;
	}

	public void setEntityFieldId(Long entityFieldId) {
		this.entityFieldId = entityFieldId;
	}

	public Long getViewDefFieldId() {
		return viewDefFieldId;
	}

	public void setViewDefFieldId(Long viewDefFieldId) {
		this.viewDefFieldId = viewDefFieldId;
	}

	public String getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getOverrideName() {
		return overrideName;
	}

	public void setOverrideName(String overrideName) {
		this.overrideName = overrideName;
	}

	public String getDisplayWidth() {
		return displayWidth;
	}

	public void setDisplayWidth(String displayWidth) {
		this.displayWidth = displayWidth;
	}

	public String getLabelStyle() {
		return labelStyle;
	}

	public void setLabelStyle(String labelStyle) {
		this.labelStyle = labelStyle;
	}

	public String getInputStyle() {
		return inputStyle;
	}

	public void setInputStyle(String inputStyle) {
		this.inputStyle = inputStyle;
	}

}
