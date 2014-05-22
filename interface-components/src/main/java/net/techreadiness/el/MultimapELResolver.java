package net.techreadiness.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;

import com.google.common.collect.Multimap;

public class MultimapELResolver extends ELResolver {

	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (isResolvable(context, base)) {
			context.setPropertyResolved(true);
			@SuppressWarnings({ "rawtypes" })
			Multimap multimap = (Multimap) base;
			return multimap.get(property);
		}
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (isResolvable(context, base)) {
			context.setPropertyResolved(true);
			return Object.class;
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		if (isResolvable(context, base)) {
			context.setPropertyResolved(true);
			@SuppressWarnings({ "rawtypes" })
			Multimap multimap = (Multimap) base;
			multimap.put(property, value);
		}
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		if (isResolvable(context, base)) {
			context.setPropertyResolved(true);
			return false;
		}
		return false;

	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if (isResolvable(context, base)) {
			return Object.class;
		}
		return null;

	}

	private static boolean isResolvable(ELContext context, Object base) {
		return context != null && base instanceof Multimap;
	}

}
