package net.techreadiness.plugin.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.template.TemplateRenderingContext;
import org.apache.struts2.views.java.Attributes;
import org.apache.struts2.views.java.TagSerializer;
import org.apache.struts2.views.java.XHTMLTagSerializer;

public class StatusColorTag extends SimpleTagSupport {
	private String status;
	private String size;

	@Override
	public void doTag() throws JspException, IOException {
		TagSerializer s = new XHTMLTagSerializer();
		TemplateRenderingContext renderingContext = new TemplateRenderingContext(null, getJspContext().getOut(), null, null,
				null);
		s.setup(renderingContext);
		Attributes attrs = new Attributes();
		StringBuilder sb = new StringBuilder();
		sb.append("color");
		sb.append(" ");
		sb.append(getPercentColor(status));
		if (StringUtils.isNotBlank(size)) {
			sb.append(" ");
			sb.append(size);
		}
		attrs.add("class", sb.toString());

		s.start("span", attrs);
		s.end("span");
	}

	public String getColor(Long percentage) {
		if (percentage < 26) {
			return "report-level1";
		} else if (percentage < 51) {
			return "report-level2";
		} else if (percentage < 76) {
			return "report-level3";
		} else {
			return "report-level4";
		}
	}

	public String getPercentColor(String value) {
		String percent = StringUtils.defaultString(value).replaceAll("[>%]", "");
		try {
			return getColor(Long.valueOf(percent));
		} catch (Exception e) {
			return "report-level-null";
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
