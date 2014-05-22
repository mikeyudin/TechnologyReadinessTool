package net.techreadiness.ui.tags.taskview;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

public class RowInfo<T> {
	private T row;
	private T errorRow;
	private String fieldNamePrefix;
	private Map<String, List<String>> rowErrors;
	private boolean rowInError = false;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RowInfo(T row, ValueStack valueStack, String var, String fieldName) {
		this.row = row;
		try {
			valueStack.push(row);
			Map<String, Object> rowMap = Maps.newHashMap();
			rowMap.put(var, row);
			try {
				valueStack.push(rowMap);
				fieldNamePrefix = TextParseUtil.translateVariables('%', fieldName, valueStack);
			} finally {
				valueStack.pop();
			}
		} finally {
			valueStack.pop();
		}

		Map<String, List<String>> errors = (Map<String, List<String>>) valueStack.findValue("fieldErrors");
		rowErrors = getErrorsForCurrentRow(errors);

		rowInError = rowErrors != null && !rowErrors.isEmpty();
		if (rowInError) {
			T target = (T) valueStack.findValue(fieldNamePrefix);

			if (row instanceof Map) {
				Map<?, ?> rowData = (Map<?, ?>) row;
				rowData.putAll((Map) target);
				errorRow = (T) rowData;
			} else {
				merge(target, row);
				errorRow = target;
			}
		} else {
			errorRow = row;
		}
	}

	private void merge(T target, T destination) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());

			// Iterate over all the attributes
			for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

				// Only copy writable attributes
				if (descriptor.getWriteMethod() != null) {
					Object originalValue = descriptor.getReadMethod().invoke(target);

					// Only copy values values where the destination values is null
					if (originalValue == null) {
						Object defaultValue = descriptor.getReadMethod().invoke(destination);
						descriptor.getWriteMethod().invoke(target, defaultValue);
					}

				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public T getRow() {
		return row;
	}

	public T getErrorRow() {
		return errorRow;
	}

	public boolean isRowInError() {
		return rowInError;
	}

	protected Map<String, List<String>> getErrorsForCurrentRow(Map<String, List<String>> errors) {
		return Maps.filterKeys(errors, new FieldErrorFilter());
	}

	class FieldErrorFilter implements Predicate<String> {

		@Override
		public boolean apply(String input) {
			return StringUtils.startsWith(input, fieldNamePrefix);
		}

	}
}
