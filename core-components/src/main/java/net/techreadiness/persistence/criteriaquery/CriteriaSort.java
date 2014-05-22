package net.techreadiness.persistence.criteriaquery;

import javax.xml.bind.annotation.XmlAttribute;

public class CriteriaSort {
	String fieldName;
	boolean descending;

	public CriteriaSort() {
	}

	public CriteriaSort(String fieldName) {
		this.fieldName = fieldName;
		descending = false;
	}

	public CriteriaSort(String fieldName, boolean descending) {
		this.fieldName = fieldName;
		this.descending = descending;
	}

	@XmlAttribute
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@XmlAttribute
	public boolean isDescending() {
		return descending;
	}

	public void setDescending(boolean descending) {
		this.descending = descending;
	}

}
