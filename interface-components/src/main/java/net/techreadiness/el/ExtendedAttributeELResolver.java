package net.techreadiness.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.BeanELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;

import net.techreadiness.service.object.BaseObjectWithExts;

public class ExtendedAttributeELResolver extends ELResolver {
	private final ELResolver beanResolver;

	public ExtendedAttributeELResolver() {
		beanResolver = new BeanELResolver();
	}

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (isResolvable(base, property)) {
			try {
				Object value = beanResolver.getValue(context, base, property);
				return value;
			} catch (PropertyNotFoundException e) {
				context.setPropertyResolved(true);
				BaseObjectWithExts<?> entity = (BaseObjectWithExts<?>) base;
				return entity.getExtendedAttributes().get(property);
			} catch (RuntimeException e) {
				context.setPropertyResolved(false);
				throw e;
			}
		}
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (isResolvable(base, property)) {
			try {
				Class<?> value = beanResolver.getType(context, base, property);
				return value;
			} catch (PropertyNotFoundException e) {
				context.setPropertyResolved(true);
				return String.class;
			} catch (RuntimeException e) {
				context.setPropertyResolved(false);
				throw e;
			}
		}
		return null;
	}

	private static boolean isResolvable(Object base, Object property) {
		return base instanceof BaseObjectWithExts && property instanceof String;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		if (isResolvable(base, property)) {
			try {
				beanResolver.setValue(context, base, property, value);
			} catch (PropertyNotFoundException e) {
				String prop = (String) property;
				BaseObjectWithExts<?> entity = (BaseObjectWithExts<?>) base;
				// TODO Is formatting necessary?
				entity.getExtendedAttributes().put(prop, value == null ? null : value.toString());
				context.setPropertyResolved(true);
			} catch (RuntimeException e) {
				context.setPropertyResolved(false);
				throw e;
			}
		}
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		if (isResolvable(base, property)) {
			try {
				return beanResolver.isReadOnly(context, base, property);
			} catch (PropertyNotFoundException e) {
				context.setPropertyResolved(true);
				return false;
			} catch (RuntimeException e) {
				context.setPropertyResolved(false);
				throw e;
			}
		}
		return true;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return beanResolver.getFeatureDescriptors(context, base);
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return beanResolver.getCommonPropertyType(context, base);
	}

}
