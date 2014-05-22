package net.techreadiness.plugin.action.reports;

import java.util.Map;

import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionStyleExpression;

public class ReportPdfBooleanCondition extends ConditionStyleExpression {

	private static final long serialVersionUID = 1L;
	private String value;

	public ReportPdfBooleanCondition(String value) {
		this.value = value;
	}

	@Override
	public Object evaluate(Map fields, Map variables, Map parameters) {
		Object value = getCurrentValue();
		// try parsing number
		try {
			if (value != null && value.toString().equalsIgnoreCase(this.value)) {
				return true;
			}
		} catch (Exception e) {
			// Ignore errors
		}
		return false;
	}

	@Override
	public String getClassName() {
		return Boolean.class.getName();
	}

}
