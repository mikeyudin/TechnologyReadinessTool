package net.techreadiness.batch;

import org.springframework.batch.item.file.transform.FieldSet;

public class BaseData {
	protected FieldSet fieldSet;

	// The line number of this fieldset in the import file.
	protected int lineNumber = -1;

	// The raw data provided in the import file.
	protected String rawData;

	public FieldSet getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(FieldSet fieldSet) {
		this.fieldSet = fieldSet;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

}
