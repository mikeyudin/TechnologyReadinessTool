package net.techreadiness.customer.action;

import net.techreadiness.service.CustomTextService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.ui.BaseAction;
import net.techreadiness.ui.util.ContextUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionContext;

public class HelpAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final String HELP_KEY_PREFIX = "help_";
	private static final String DEFAULT_HELP_KEY = HELP_KEY_PREFIX + "default";
	private String html;
	private String helpKey;
	private String helpUrl;

	@Override
	@Action(results = { @Result(name = "success", location = "/help.jsp") })
	public String execute() {
		CustomTextService textService = ContextUtils.getRequiredBeanOfType(CustomTextService.class);
		ActionContext actionContext = ActionContext.getContext();
		ServiceContext context = (ServiceContext) actionContext.getSession().get(BaseAction.SERVICE_CONTEXT);
		String code = DEFAULT_HELP_KEY;
		if (StringUtils.isNotBlank(helpKey)) {
			code = helpKey;
		} else if (StringUtils.isNotBlank(helpUrl)) {
			code = StringUtils.replace(StringUtils.substringAfter(StringUtils.substringAfter(helpUrl, "/"), "/"), "/", ".");
			code = HELP_KEY_PREFIX + StringUtils.removeEnd(code, ".action");
		}
		html = textService.getCustomTextByCode(context, code);
		if (StringUtils.isBlank(html)) {
			html = textService.getCustomTextByCode(context, DEFAULT_HELP_KEY);
		}
		return SUCCESS;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getHelpKey() {
		return helpKey;
	}

	public void setHelpKey(String helpKey) {
		this.helpKey = helpKey;
	}

	public String getHelpUrl() {
		return helpUrl;
	}

	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}
}
