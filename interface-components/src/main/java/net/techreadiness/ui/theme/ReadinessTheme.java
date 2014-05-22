package net.techreadiness.ui.theme;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.views.java.Attributes;
import org.apache.struts2.views.java.DefaultTagHandlerFactory;
import org.apache.struts2.views.java.simple.DynamicAttributesHandler;
import org.apache.struts2.views.java.simple.ScriptingEventsHandler;
import org.apache.struts2.views.java.simple.SimpleTheme;

import com.google.common.base.Joiner;

public class ReadinessTheme extends SimpleTheme {

	public ReadinessTheme() {
		setName("readiness");

		appendHandler("a-close", DynamicAttributesHandler.class, ScriptingEventsHandler.class, ToolbarItemHandler.class);

		appendHandler("submit", ToolbarItemHandler.class);

		appendHandler("submit-close", ToolbarItemHandler.class);
		appendHandler("text", InputFieldHandler.class);
		appendHandler("select", InputFieldHandler.class);
		appendHandler("password", InputFieldHandler.class);
		appendHandler("checkbox", InputFieldHandler.class);
		appendHandler("textarea", InputFieldHandler.class);
		appendHandler("radiomap", RadioMapHandler.class);
		appendHandler("radiomap", InputFieldHandler.class);
		appendHandler("label", LabelHandler.class);

	}

	void appendHandler(String tagName, Class<?>... handlers) {
		for (Class<?> handler : handlers) {
			insertTagHandlerFactory(tagName, handlerFactories.get(tagName).size() - 1, new DefaultTagHandlerFactory(handler));
		}
	}

	public static void addClass(Attributes attributes, String className, String... additionalClassNames) {
		attributes.put(
				"class",
				Joiner.on(" ").join(StringUtils.defaultString(attributes.get("class")), className,
						(Object[]) additionalClassNames));
	}
}
