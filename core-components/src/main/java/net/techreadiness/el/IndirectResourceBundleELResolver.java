package net.techreadiness.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndirectResourceBundleELResolver extends ELResolver {
	private Logger log = LoggerFactory.getLogger(IndirectResourceBundleELResolver.class);
	private ResourceBundle bundle;

	public IndirectResourceBundleELResolver(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return null;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {

		if (isResolvable(base, property)) {
			return ResourceBundle.class;
		}
		return null;
	}

	@Override
	public Object getValue(ELContext context, Object base, Object property) {

		if (isResolvable(base, property)) {
			context.setPropertyResolved(true);
			return bundle;
		}
		return null;
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		if (isResolvable(base, property)) {
			context.setPropertyResolved(true);
			return true;
		}
		return false;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		if (isResolvable(base, property)) {
			throw new PropertyNotWritableException("resolver is read-only");
		}
	}

	private boolean isResolvable(Object base, Object property) {
		log.debug("Base: {}, Property: {}", base, property);
		boolean resolvable = base == null && property != null && property instanceof String && property.equals("msg");
		log.debug("Resolvable: {}", resolvable);
		return resolvable;
	}
}
