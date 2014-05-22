package net.techreadiness.ui.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.TextProvider;

public class ConfigurableDateConverter extends StrutsTypeConverter {
	private DateFormat dateFormat;

	public ConfigurableDateConverter(TextProvider textProvider) {
		dateFormat = new SimpleDateFormat(textProvider.getText("core.date.format"));
	}

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (values == null || values.length == 0) {
			return null;
		}
		try {
			return dateFormat.parse(values[0]);
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public String convertToString(Map context, Object o) {
		return dateFormat.format(o);
	}

}
