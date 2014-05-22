package net.techreadiness.service.common;

import java.util.Map;

import com.google.common.collect.Maps;

public enum Operator {

	EQUAL("equals", "==", Boolean.FALSE), NOT_EQUAL("not equal", "!=", Boolean.FALSE), GREATER_THAN("greater than", ">",
			Boolean.FALSE), GREATER_THAN_OR_EQUAL("greater than or equal", ">=", Boolean.FALSE), LESS_THAN("less than", "<",
			Boolean.FALSE), LESS_THAN_OR_EQUAL("less than or equal", "<=", Boolean.FALSE), MATCHES("matches regex",
			"matches", Boolean.FALSE), ENDS_WITH("ends with", "endsWith", Boolean.TRUE), STARTS_WITH("starts with",
			"startsWith", Boolean.TRUE);

	private static final Map<String, Operator> conversionMap = Maps.newHashMap();
	private String description;
	private String operator;
	private Boolean custom;

	private Operator(String description, String operator, Boolean custom) {
		this.description = description;
		this.operator = operator;
		this.custom = custom;
	}

	public String getDescription() {
		return description;
	}

	public String getOperator() {
		return operator;
	}

	public Boolean isCustom() {
		return custom;
	}

	public static Operator getOperator(String operator) {
		if (conversionMap.isEmpty()) {
			for (Operator op : Operator.values()) {
				conversionMap.put(op.getOperator(), op);
			}
		}
		return conversionMap.get(operator);
	}

}
