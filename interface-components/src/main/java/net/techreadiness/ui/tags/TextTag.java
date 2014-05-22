package net.techreadiness.ui.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.TextProvider;

public class TextTag extends SimpleTagSupport {

	private String name;
	private String var;
	
	private boolean escapeHtml = true;

	@Override
	public void doTag() throws JspException, IOException {
		ActionContext actionContext = ActionContext.getContext();
		TextProvider textProvider = actionContext.getContainer().getInstance(TextProvider.class);
		textProvider.getText(name);
		String message = textProvider.getText(name);
		if (escapeHtml) {
			message = StringEscapeUtils.escapeHtml4(message);
		}
		if (!message.equals(name)) {
			if (StringUtils.isBlank(var)) {
				getJspContext().getOut().append(message);
			} else {
				getJspContext().setAttribute(var, message);
			}
		}
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEscapeHtml(boolean escapeHtml) {
		this.escapeHtml = escapeHtml;
	}

	public void setVar(String var) {
		this.var = var;
	}
}
