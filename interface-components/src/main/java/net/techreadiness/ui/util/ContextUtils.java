package net.techreadiness.ui.util;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.StrutsStatics;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.opensymphony.xwork2.ActionContext;

public class ContextUtils {

	public static <T> T getOptionalBeanOfType(Class<T> type, ServletContext servletContext) {
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		Map<String, T> map = applicationContext.getBeansOfType(type);

		for (ApplicationContext context = applicationContext.getParent(); context != null; context = context.getParent()) {
			map.putAll(context.getBeansOfType(type));
		}

		return Iterables.getOnlyElement(map.values(), null);
	}

	public static <T> T getRequiredBeanOfType(Class<T> type, ServletContext servletContext) {
		return Preconditions.checkNotNull(getOptionalBeanOfType(type, servletContext), "No bean of type %s found.", type);
	}

	private static ServletContext findServletContext() {
		ServletContext servletContext = (ServletContext) ActionContext.getContext().getActionInvocation()
				.getInvocationContext().get(StrutsStatics.SERVLET_CONTEXT);
		return servletContext;
	}

	public static <T> T getOptionalBeanOfType(Class<T> type) {
		return getOptionalBeanOfType(type, findServletContext());
	}

	public static <T> T getRequiredBeanOfType(Class<T> type) {
		return getRequiredBeanOfType(type, findServletContext());
	}
}
