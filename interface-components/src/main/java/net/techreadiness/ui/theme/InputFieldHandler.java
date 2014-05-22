package net.techreadiness.ui.theme;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.techreadiness.ui.tags.form.FieldSetColumnTag;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.views.java.Attributes;
import org.apache.struts2.views.java.simple.AbstractTagHandler;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.opensymphony.xwork2.util.ValueStack;

public class InputFieldHandler extends AbstractTagHandler {
	boolean hideLabel;
	boolean hideErrors;
	boolean hideTooltip;
	boolean hideErrorClass;
	String labelStyle;

	@Override
	public void start(String name, Attributes a) throws IOException {
		if ("input".equals(name) || "select".equals(name) || "textarea".equals(name)) {
			hideLabel = Boolean.TRUE.equals(context.getStack().findValue("#attr.tag.hideLabel", Boolean.TYPE))
					|| "true".equals(a.get("hideLabel"));

			hideErrors = Boolean.TRUE.equals(context.getStack().findValue("#attr.tag.hideErrors", Boolean.TYPE))
					|| "true".equals(a.get("hideErrors")) || "hidden".equals(a.get("type"));

			hideTooltip = Boolean.TRUE.equals(context.getStack().findValue("#attr.tag.hideTooltip", Boolean.TYPE))
					|| "true".equals(a.get("hideTooltip")) || "hidden".equals(a.get("type"));

			hideErrorClass = "true".equals(a.get("hideErrorClass"));

			labelStyle = (String) context.getStack().findValue("#attr.tag.field.labelStyle");

			hideErrorClass = "true".equals(a.get("hideErrorClass"));
			Optional<Object> optional = Iterables.tryFind((List<Object>) context.getStack().getRoot(),
					Predicates.instanceOf(FieldSetColumnTag.class));
			if (optional.isPresent()) {
				String labelWidth = ((FieldSetColumnTag) optional.get()).getLabelWidth();
				if (StringUtils.isNotBlank(labelWidth)) {
					labelStyle = "width:" + labelWidth + ";";
				}
			}
			context.getStack().getRoot();
			boolean hideRequired = "true".equals(a.get("hideRequired"));

			if (!a.containsKey("type") || a.containsKey("type") && !a.get("type").equals("hidden")) {
				@SuppressWarnings("rawtypes")
				Map parameters = context.getParameters();
				String fieldName = (String) parameters.get("name");
				if (!hideErrorClass && hasFieldErrors(fieldName, context.getStack())) {
					ReadinessTheme.addClass(a, "input-error");
				}

				String label = ObjectUtils.toString(parameters.get("label"));
				boolean showLabel = StringUtils.isNotEmpty(label) && showLabel(name);
				if (showLabel) {
					Attributes attributes = new Attributes().add("for", ObjectUtils.toString(parameters.get("id")));
					if (StringUtils.isNotBlank(labelStyle)) {
						attributes.add("style", labelStyle);
					}
					super.start("label", attributes);
					characters(label, false);
				}

				if (!hideRequired && "true".equalsIgnoreCase(ObjectUtils.toString(a.get("required")))) {

					super.start("span", new Attributes().add("class", "required"));
					characters("*");
					super.end("span");
				}

				String tooltip = ObjectUtils.toString(parameters.get("tooltip"));
				if (StringUtils.isNotBlank(tooltip) && !hideTooltip) {
					String contextPath = context.getStack().findString("#request['javax.servlet.forward.context_path']");
					String tooltipId = StringUtils.remove((String) parameters.get("id"), "'") + "_tooltip";
					super.start(
							"img",
							new Attributes().add("class", "input-info")
									.add("src", String.format("%s/static/images/icons/moreinfo.png", contextPath))
									.add("rel", "#" + tooltipId));

					super.start("div", new Attributes().add("style", "display: none;").add("id", tooltipId));
					characters(tooltip);
					super.end("div");
					super.end("img");
				}
				if (showLabel) {
					super.end("label");
				}

			}
		}
		a.remove("required");
		super.start(name, a);
	}

	private boolean showLabel(String name) {
		return !hideLabel && !"option".equalsIgnoreCase(name);
	}

	@Override
	public void end(String name) throws IOException {
		super.end(name);
		if (!hideErrors && ("input".equals(name) || "select".equals(name))) {
			printFieldErrors();
		}
	}

	public static boolean hasFieldErrors(String name, ValueStack valueStack) {
		Map<String, List<String>> fieldErrors = (Map<String, List<String>>) valueStack.findValue("fieldErrors", Map.class);
		return fieldErrors != null && StringUtils.isNotEmpty(name) && fieldErrors.containsKey(name);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void printFieldErrors() throws IOException {
		Map<String, List<String>> fieldErrors = (Map) context.getStack().findValue("fieldErrors", Map.class);
		Map<String, Object> parameters = context.getParameters();
		String name = ObjectUtils.toString(parameters.get("name"));

		if (hasFieldErrors(name, context.getStack())) {
			StringBuilder cssClass = new StringBuilder("field-errors");
			if (parameters.containsKey("cssErrorClass")) {
				cssClass.append(" ");
				cssClass.append(parameters.get("cssErrorClass"));
			}
			String cssStyle = ObjectUtils.toString(parameters.get("cssErrorStyle"), "");

			super.start("div", new Attributes().add("class", cssClass.toString()).add("style", cssStyle));
			super.start("ul", new Attributes());

			for (String error : fieldErrors.get(name)) {
				super.start("li", new Attributes().add("class", "inline-validation-error"));
				super.start("em", new Attributes());
				characters(error);
				super.end("em");
				super.end("li");
			}
			super.end("ul");
			super.end("div");
		}
	}

}
