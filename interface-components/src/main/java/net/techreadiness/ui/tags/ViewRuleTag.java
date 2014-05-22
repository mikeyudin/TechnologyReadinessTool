package net.techreadiness.ui.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;

import net.techreadiness.service.RuleService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.BaseObject;

import org.apache.struts2.util.ComponentUtils;
import org.apache.struts2.views.jsp.TagUtils;

import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

public class ViewRuleTag extends ConditionalTagSupport {
	private static final long serialVersionUID = 1L;
	private Long ruleId;
	private RuleService ruleService;
	private String row;

	@Override
	protected boolean condition() throws JspTagException {
		boolean ruleValue = true;
		Object rowValue = findValue(row);
		if (rowValue instanceof BaseObject && ruleService != null && ruleId != null) {
			ServiceContext context = (ServiceContext) pageContext.getSession().getAttribute("serviceContext");
			ruleValue = ruleService.executeViewRule(context, ruleId, (BaseObject<?>) rowValue);
		}

		return ruleValue;
	}

	public RuleService getRuleService() {
		return ruleService;
	}

	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		if (ruleId.equals(Long.valueOf(0))) {
			this.ruleId = null;
		} else {
			this.ruleId = ruleId;
		}
	}

	protected ValueStack getStack() {
		return TagUtils.getStack(pageContext);
	}

	protected String findString(String expr) {
		return findValue(expr, String.class);
	}

	protected Object findValue(String expr) {
		expr = ComponentUtils.stripExpressionIfAltSyntax(getStack(), expr);

		return getStack().findValue(expr);
	}

	protected <T> T findValue(String expr, Class<T> toType) {
		if (ComponentUtils.altSyntax(getStack()) && toType == String.class) {
			return (T) TextParseUtil.translateVariables('%', expr, getStack());
		}
		expr = ComponentUtils.stripExpressionIfAltSyntax(getStack(), expr);
		return (T) getStack().findValue(expr, toType);
	}
}
