package net.techreadiness.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import net.techreadiness.el.ExtendedAttributeELResolver;
import net.techreadiness.el.MultimapELResolver;
import net.techreadiness.el.StrutsActionELResolver;

@WebListener
public class CoreServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		JspApplicationContext jspApplicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(
				sce.getServletContext());
		jspApplicationContext.addELResolver(new StrutsActionELResolver());
		jspApplicationContext.addELResolver(new ExtendedAttributeELResolver());
		jspApplicationContext.addELResolver(new MultimapELResolver());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Nothing to do when context is destroyed
	}

}
