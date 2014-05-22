package net.techreadiness.ui.util;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * Loads all context init parameters as system properties. This allows them to be used by Spring.
 */
public class SystemPropertiesHelper implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		Enumeration<String> params = context.getInitParameterNames();

		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String value = context.getInitParameter(param);
			System.setProperty(param, value);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// Nothing to do
	}
}
