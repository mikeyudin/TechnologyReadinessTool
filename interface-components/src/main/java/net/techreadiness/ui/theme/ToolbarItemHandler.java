package net.techreadiness.ui.theme;

import static net.techreadiness.ui.theme.ReadinessTheme.addClass;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.template.TemplateRenderingContext;
import org.apache.struts2.views.java.Attributes;
import org.apache.struts2.views.java.simple.AbstractTagHandler;

public class ToolbarItemHandler extends AbstractTagHandler {

	private boolean renderingToolbar;

	@Override
	public void setup(TemplateRenderingContext context) {
		super.setup(context);
		renderingToolbar = "true".equals(context.getStack().findString("renderingToolbar"));
		if (renderingToolbar) {
			context.getParameters().put("type", "button");
		}
	}

	private boolean isButton() {
		return "button".equals(context.getParameters().get("type"));
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void start(String name, Attributes attributes) throws IOException {
		if (isButton() && !StringUtils.contains(attributes.get("class"), "-button")) {
			addClass(attributes, "gray-button");
		}

		if (attributes.containsKey("disabled")) {
			addClass(attributes, "disabled-button");
		}

		if (((Map) context.getParameters().get("dynamicAttributes")).containsKey("confirm")) {
			addClass(attributes, "confirm");
		}

		super.start(name, attributes);
	}

	@Override
	public void end(String name) throws IOException {
		super.end(name);
	}
}
