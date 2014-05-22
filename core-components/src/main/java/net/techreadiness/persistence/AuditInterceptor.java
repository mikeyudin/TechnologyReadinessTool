package net.techreadiness.persistence;

import java.io.Serializable;

import net.techreadiness.service.ServiceContext;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AuditInterceptor extends EmptyInterceptor implements ApplicationContextAware {
	private static final long serialVersionUID = 1L;
	private ApplicationContext applicationContext;
	private final Logger log = LoggerFactory.getLogger(AuditInterceptor.class);

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		ServiceContext context = applicationContext.getBean(ServiceContext.class);
		log.debug("onDelete context={}", context);
		super.onDelete(entity, id, state, propertyNames, types);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
