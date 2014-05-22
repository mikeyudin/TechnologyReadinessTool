package net.techreadiness.ui.tags;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Ordering;

public class ViewFieldDisplayOrderComparator implements Comparator<ViewFieldTag> {

	@Override
	public int compare(ViewFieldTag o1, ViewFieldTag o2) {
		Integer displayOrder1 = getIntegerValue(o1.getDisplayOrder());
		Integer displayOrder2 = getIntegerValue(o2.getDisplayOrder());

		int displayOrderResult = Ordering.natural().compare(displayOrder1, displayOrder2);
		if (displayOrderResult != 0) {
			return displayOrderResult;
		}
		int pageOrderResult = Ordering.natural().nullsFirst().compare(o1.getPageOrder(), o2.getPageOrder());
		if (pageOrderResult != 0) {
			return pageOrderResult;
		}
		return o1.getCode().compareTo(o2.getCode());
	}

	private static Integer getIntegerValue(String displayOrder) {
		String defaultString = StringUtils.defaultString(displayOrder);

		try {
			Integer order = NumberUtils.createInteger(defaultString);
			return order;
		} catch (NumberFormatException e) {
			if (defaultString.equalsIgnoreCase("first") || StringUtils.isBlank(defaultString)) {
				return Integer.valueOf(Integer.MIN_VALUE);
			} else if (defaultString.equalsIgnoreCase("last")) {
				return Integer.valueOf(Integer.MAX_VALUE);
			} else {
				throw new IllegalArgumentException("The display order must be an integer, 'first' or 'last'.");
			}
		}
	}

	public int compare(String o1, String o2) {
		String value1 = StringUtils.defaultString(o1);
		String value2 = StringUtils.defaultString(o2);

		if (value1.equalsIgnoreCase(value2)) {
			return 0;
		} else if (StringUtils.isBlank(value1) && StringUtils.isBlank(value2)) {
			return 0;
		} else if (value1.equalsIgnoreCase("first") || value2.equalsIgnoreCase("last")) {
			return -1;
		} else if (value1.equalsIgnoreCase("last") || value2.equalsIgnoreCase("first")) {
			return 1;
		} else if (StringUtils.isBlank(value2) || !StringUtils.isNumeric(value2)) {
			return -1;
		} else if (StringUtils.isBlank(value1) || !StringUtils.isNumeric(value1)) {
			return 1;
		} else {
			return Integer.valueOf(value1).compareTo(Integer.valueOf(value2));
		}
	}
}
