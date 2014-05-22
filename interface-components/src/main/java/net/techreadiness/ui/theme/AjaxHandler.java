package net.techreadiness.ui.theme;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts2.views.java.Attributes;
import org.apache.struts2.views.java.simple.AbstractTagHandler;

public class AjaxHandler extends AbstractTagHandler {

	@Override
	public void start(String name, Attributes attributes) throws IOException {
		Map<String, Object> dynamicAttributes = (Map<String, Object>) context.getParameters().get("dynamicAttributes");

		for (Entry<String, Object> attribute : dynamicAttributes.entrySet()) {
			if (attribute.getKey().startsWith("ajax")) {
				attributes.add(String.format("data-%s", attribute.getKey()), ObjectUtils.toString(attribute.getValue()));
			}
		}
		super.start(name, attributes);
	}
}
