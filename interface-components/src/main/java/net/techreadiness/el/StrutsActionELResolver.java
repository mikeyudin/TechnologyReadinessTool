package net.techreadiness.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.BeanELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;

import com.opensymphony.xwork2.ActionContext;

public class StrutsActionELResolver extends ELResolver {
	private final ELResolver beanResolver;

	public StrutsActionELResolver() {
		beanResolver = new BeanELResolver();
	}

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		Object action = getAction();
		if (base == null && property != null && action != null) {
			try {
				Object value = beanResolver.getValue(context, action, property);
				return value;
			} catch (PropertyNotFoundException e) {
				context.setPropertyResolved(false);
				return null;
			} catch (RuntimeException e) {
				context.setPropertyResolved(false);
				throw e;
			}
		}
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		Object action = getAction();
		if (base == null && property != null && action != null) {
			try {
				return beanResolver.getType(context, action, property);
			} catch (PropertyNotFoundException e) {
				context.setPropertyResolved(false);
				return null;
			} catch (RuntimeException e) {
				context.setPropertyResolved(false);
				throw e;
			}
		}
		return null;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		Object action = getAction();
		if (base == null && property != null && action != null) {
			try {
				beanResolver.setValue(context, action, property, value);
			} catch (PropertyNotFoundException e) {
				context.setPropertyResolved(false);
			} catch (RuntimeException e) {
				context.setPropertyResolved(false);
				throw e;
			}
		}
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		Object action = getAction();
		if (base == null && property != null && action != null) {
			try {
				return beanResolver.isReadOnly(context, action, property);
			} catch (PropertyNotFoundException e) {
				context.setPropertyResolved(false);
				return false;
			} catch (RuntimeException e) {
				context.setPropertyResolved(false);
				throw e;
			}
		}
		return false;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		// It is not possible to resolve a Struts action at design time.
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if (base == null && getAction() != null) {
			return String.class;
		}

		return null;
	}

	@Override
	public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
		if (base == null && getAction() != null) {
			// TODO This needs to be looked at
			return beanResolver.invoke(context, getAction(), method, paramTypes, params);
		}

		return null;
	}

	private static Object getAction() {
		try {
			return ActionContext.getContext().getActionInvocation().getAction();
		} catch (NullPointerException e) {
			return null;
		}
	}
}
