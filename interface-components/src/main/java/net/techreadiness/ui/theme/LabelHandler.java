package net.techreadiness.ui.theme;

import java.io.IOException;

import org.apache.struts2.views.java.Attributes;
import org.apache.struts2.views.java.simple.AbstractTagHandler;

public class LabelHandler extends AbstractTagHandler {
	private boolean required;

	@Override
	public void start(String name, Attributes a) throws IOException {
		if (a.get("required") != null && (a.get("required").equals("true") || a.get("required").equals("required"))) {
			required = true;
		} else {
			required = false;
		}
		super.start(name, a);
	}

	@Override
	public void end(String name) throws IOException {
		if (required) {
			Attributes spanAttributes = new Attributes();
			spanAttributes.add("class", "required");
			super.start("span", spanAttributes);
			characters("*");
			super.end("span");
		}
		super.end(name);
	}
}
