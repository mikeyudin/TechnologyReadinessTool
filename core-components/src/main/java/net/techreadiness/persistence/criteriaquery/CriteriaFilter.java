package net.techreadiness.persistence.criteriaquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CriteriaFilter {
	private String fieldName;

	public enum FilterType {
		EQUALS, LIKE, ISNULL, ISNOTNULL
	}

	private FilterType filterType;
	private Collection<String> values = new ArrayList<>();

	public CriteriaFilter(String fieldName, FilterType filterType, Collection<String> values) {
		this.fieldName = fieldName;
		this.filterType = filterType;
		this.values = values;
	}

	public CriteriaFilter(String fieldName, Collection<String> values) {
		this.fieldName = fieldName;
		filterType = FilterType.EQUALS;
		this.values = values;
	}

	public CriteriaFilter(String fieldName, String[] values) {
		this.fieldName = fieldName;
		filterType = FilterType.EQUALS;
		this.values = Arrays.asList(values);
	}

	public CriteriaFilter(String fieldName, FilterType filterType, String[] values) {
		this.fieldName = fieldName;
		this.filterType = filterType;
		this.values = Arrays.asList(values);
	}

	public CriteriaFilter(String fieldName, FilterType filterType, String value) {
		this.fieldName = fieldName;
		this.filterType = filterType;
		values = new ArrayList<>();
		values.add(value);
	}

	public CriteriaFilter(String fieldName, String value) {
		this.fieldName = fieldName;
		filterType = FilterType.EQUALS;
		values = new ArrayList<>();
		values.add(value);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	public Collection<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
}
