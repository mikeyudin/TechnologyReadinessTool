package net.techreadiness.ui.tags.security;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import net.techreadiness.service.PermissionService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.UserService;
import net.techreadiness.ui.util.ContextUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class AbstractSecurityTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	public static final String SERVICE_CONTEXT = "serviceContext";

	protected UserService userService;
	protected PermissionService permissionService;
	protected ApplicationContext applicationContext;
	protected String var;

	protected void initializeIfRequired() {
		if (applicationContext != null) {
			return;
		}

		applicationContext = getContext(pageContext);

		userService = getBeanOfType(UserService.class);
		permissionService = getBeanOfType(PermissionService.class);
	}

	protected int skipBody() {
		if (var != null) {
			pageContext.setAttribute(var, Boolean.FALSE, PageContext.PAGE_SCOPE);
		}
		return Tag.SKIP_BODY;
	}

	protected int evalBody() {
		if (var != null) {
			pageContext.setAttribute(var, Boolean.TRUE, PageContext.PAGE_SCOPE);
		}
		return Tag.EVAL_BODY_INCLUDE;
	}

	protected ApplicationContext getContext(PageContext pageContext) {
		ServletContext servletContext = pageContext.getServletContext();
		return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	private static <T> T getBeanOfType(Class<T> type) {
		return ContextUtils.getOptionalBeanOfType(type);
	}

	protected ServiceContext getServiceContext() {
		return (ServiceContext) pageContext.getAttribute(SERVICE_CONTEXT, PageContext.SESSION_SCOPE);
	}

	public void setVar(String var) {
		this.var = var;
	}
}
