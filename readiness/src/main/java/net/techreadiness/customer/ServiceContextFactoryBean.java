package net.techreadiness.customer;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.ui.BaseAction;

import org.springframework.beans.factory.FactoryBean;

public class ServiceContextFactoryBean implements FactoryBean<ServiceContext> {
	@Inject
	private HttpServletRequest request;

	@Override
	public ServiceContext getObject() throws Exception {
		ServiceContext context = (ServiceContext) request.getSession().getAttribute(BaseAction.SERVICE_CONTEXT);
		if (context == null) {
			context = new ServiceContext();
		}
		return context;
	}

	@Override
	public Class<?> getObjectType() {
		return ServiceContext.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
