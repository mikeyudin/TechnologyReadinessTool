package net.techreadiness.ui.theme;

import java.io.IOException;

import org.apache.struts2.components.template.TemplateRenderingContext;
import org.apache.struts2.views.java.Attributes;
import org.apache.struts2.views.java.simple.AbstractTagHandler;

public class RadioMapHandler extends AbstractTagHandler {

	Attributes labelAttrs;
	Attributes inputAttrs;

	boolean labelDisplayed = false;

	@Override
	public void setup(TemplateRenderingContext context) {
		super.setup(context);
	}

	@Override
	public void start(String name, Attributes a) throws IOException {
		a.put("hideLabel", "true");
		a.put("hideRequired", "true");
		a.put("hideErrors", "true");
		a.put("hideErrorClass", "true");

		if ("input".equals(name)) {
			super.start("li", a);
		}

		super.start(name, a);
	}

	@Override
	public void end(String name) throws IOException {
		super.end(name);

		if (!"input".equals(name)) {
			super.end("li");
		}
	}

}
