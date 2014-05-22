package net.techreadiness.plugin.action.reports;

import java.text.NumberFormat;
import java.util.Map;

import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionStyleExpression;

public class ReportPdfHighlightCondition extends ConditionStyleExpression {

	private static final long serialVersionUID = 1L;
	private NumberFormat format = NumberFormat.getPercentInstance();
	private double min, max;

	public ReportPdfHighlightCondition(double min, double max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Object evaluate(Map fields, Map variables, Map parameters) {
		Object value = getCurrentValue();
		if (value.toString().startsWith(">")) {
			value = "100%";
		}
		// try parsing number
		try {
			Number num = format.parse(value.toString());
			if (num.doubleValue() > 1.0) {
				num = 1;
			}
			if (num.doubleValue() <= max && num.doubleValue() >= min) {
				return true;
			}
		} catch (Exception e) {
			try {
				double num = Double.parseDouble(value.toString());
				if (num <= max && num >= min) {
					return true;
				}
			} catch (Exception e2) {
				// Ignore error
			}
		}
		return false;
	}

	@Override
	public String getClassName() {
		return Boolean.class.getName();
	}

}
