package net.techreadiness.plugin.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.template.TemplateRenderingContext;
import org.apache.struts2.views.java.Attributes;
import org.apache.struts2.views.java.TagSerializer;
import org.apache.struts2.views.java.XHTMLTagSerializer;

public class ReportLegend extends SimpleTagSupport {
	private String title = "Percent Coverage";
	private String cssClass;
	private String style;

	@Override
	public void doTag() throws JspException, IOException {
		TagSerializer s = new XHTMLTagSerializer();
		TemplateRenderingContext renderingContext = new TemplateRenderingContext(null, getJspContext().getOut(), null, null,
				null);
		s.setup(renderingContext);
		StringBuilder sb = new StringBuilder("report-legend-tag");
		if (StringUtils.isNotBlank(cssClass)) {
			sb.append(" ");
			sb.append(cssClass);
		}
		s.start("div", new Attributes().add("class", sb.toString()).add("style", style));
		s.start("h3", new Attributes().add("class", "report-legend-tag-title"));
		s.characters(title);
		s.end("h3");

		s.start("div", new Attributes().add("class", "color-group"));
		s.start("span", new Attributes().add("class", "color report-level1"));
		s.end("span");
		s.characters("0% - 25%");
		s.end("div");

		s.start("div", new Attributes().add("class", "color-group"));
		s.start("span", new Attributes().add("class", "color report-level2"));
		s.end("span");
		s.characters("26% - 50%");
		s.end("div");

		s.start("div", new Attributes().add("class", "color-group"));
		s.start("span", new Attributes().add("class", "color report-level3"));
		s.end("span");
		s.characters("51% - 75%");
		s.end("div");

		s.start("div", new Attributes().add("class", "color-group"));
		s.start("span", new Attributes().add("class", "color report-level4"));
		s.end("span");
		s.characters("76% - 100%");
		s.end("div");

		s.end("div");
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyle() {
		return style;
	}
}
