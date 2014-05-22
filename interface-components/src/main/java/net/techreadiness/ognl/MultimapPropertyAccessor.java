package net.techreadiness.ognl;

import java.util.Map;

import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.PropertyAccessor;

import com.google.common.collect.Multimap;
import com.opensymphony.xwork2.conversion.ObjectTypeDeterminer;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;

public class MultimapPropertyAccessor implements PropertyAccessor {
	private XWorkConverter xworkConverter;
	private ObjectTypeDeterminer objectTypeDeterminer;

	@Inject
	public void setXWorkConverter(XWorkConverter conv) {
		xworkConverter = conv;
	}

	@Inject
	public void setObjectTypeDeterminer(ObjectTypeDeterminer ot) {
		objectTypeDeterminer = ot;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getProperty(Map context, Object target, Object name) throws OgnlException {
		ReflectionContextState.updateCurrentPropertyPath(context, name);
		Object result = null;
		Multimap multimap = (Multimap) target;
		try {

			result = multimap.get(name);
		} catch (RuntimeException ex) {
			if (!(ex.getCause() instanceof ClassCastException)) {
				throw ex;
			}
		}

		if (result == null) {
			// find the key class and convert the name to that class
			Class lastClass = (Class) context.get(XWorkConverter.LAST_BEAN_CLASS_ACCESSED);

			String lastProperty = (String) context.get(XWorkConverter.LAST_BEAN_PROPERTY_ACCESSED);
			if (lastClass == null || lastProperty == null) {
				return multimap.get(name);
			}
			Class keyClass = objectTypeDeterminer.getKeyClass(lastClass, lastProperty);

			if (keyClass == null) {
				keyClass = java.lang.String.class;
			}
			Object key = getKey(context, name);
			result = multimap.get(key);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
		Object key = getKey(context, name);
		Multimap multimap = (Multimap) target;
		if (value.getClass().isArray()) {
			Object[] values = (Object[]) value;
			for (Object o : values) {
				Object value2 = getValue(context, o);
				if (value2 != null) {
					multimap.put(key, value2);
				}
			}
		} else {
			Object value2 = getValue(context, value);
			if (value2 != null) {
				multimap.put(key, value2);
			}

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getValue(Map context, Object value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		Class lastClass = (Class) context.get(XWorkConverter.LAST_BEAN_CLASS_ACCESSED);
		String lastProperty = (String) context.get(XWorkConverter.LAST_BEAN_PROPERTY_ACCESSED);
		if (lastClass == null || lastProperty == null) {
			return value;
		}
		Class elementClass = objectTypeDeterminer.getElementClass(lastClass, lastProperty, null);
		if (elementClass == null) {
			return value; // nothing is specified, we assume it will be the value passed in.
		}
		return xworkConverter.convertValue(context, value, elementClass);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getKey(Map context, Object name) {
		Class lastClass = (Class) context.get(XWorkConverter.LAST_BEAN_CLASS_ACCESSED);
		String lastProperty = (String) context.get(XWorkConverter.LAST_BEAN_PROPERTY_ACCESSED);
		if (lastClass == null || lastProperty == null) {
			// return java.lang.String.class;
			// commented out the above -- it makes absolutely no sense for when setting basic maps!
			return name;
		}
		Class keyClass = objectTypeDeterminer.getKeyClass(lastClass, lastProperty);
		if (keyClass == null) {
			keyClass = java.lang.String.class;
		}

		return xworkConverter.convertValue(context, name, keyClass);

	}

	@Override
	public String getSourceAccessor(OgnlContext context, Object target, Object index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceSetter(OgnlContext context, Object target, Object index) {
		// TODO Auto-generated method stub
		return null;
	}
}
