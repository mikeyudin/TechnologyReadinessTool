package net.techreadiness.ui.tags.datagrid;

import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class DataGridTagExtraInfo extends TagExtraInfo {
	private static final String SELECTABLE = "selectable";
	private static final String ROW_VALUE = "rowValue";
	private static final String INLINE_EDITABLE = "inlineEditable";
	private static final String INLINE_SAVE_ACTION = "inlineSaveAction";

	@Override
	public ValidationMessage[] validate(TagData data) {
		List<ValidationMessage> messages = Lists.newArrayList();

		if (!isRequestTimeValue(data, SELECTABLE)) {
			boolean selectable = Boolean.parseBoolean(data.getAttributeString(SELECTABLE));
			if (selectable && StringUtils.isBlank(data.getAttributeString(ROW_VALUE))) {
				ValidationMessage message = new ValidationMessage(ROW_VALUE,
						"If the data grid is selectable the 'rowValue' attribute must be spedified.");
				messages.add(message);
			}
		}

		if (!isRequestTimeValue(data, INLINE_EDITABLE)) {
			boolean inlineEditable = Boolean.parseBoolean(data.getAttributeString(INLINE_EDITABLE));
			if (inlineEditable && StringUtils.isBlank(data.getAttributeString(INLINE_SAVE_ACTION))) {
				ValidationMessage message = new ValidationMessage(INLINE_SAVE_ACTION,
						"If the data grid is inline editable the 'inlineSaveAction' attribute must be spedified.");
				messages.add(message);
			}
		}

		return messages.toArray(new ValidationMessage[messages.size()]);
	}

	private static boolean isRequestTimeValue(TagData data, String attributeName) {
		Object value = data.getAttribute(attributeName);
		return value != null && TagData.REQUEST_TIME_VALUE.equals(value);
	}
}
