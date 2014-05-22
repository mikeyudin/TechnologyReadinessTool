package net.techreadiness.ognl;

import java.util.Map;

import net.techreadiness.service.object.BaseObjectWithExts;
import ognl.ObjectPropertyAccessor;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.PropertyAccessor;

import com.google.common.collect.Iterables;

public class BaseObjectWithExtsPropertyAccessor implements PropertyAccessor {
	private final ObjectPropertyAccessor accessor;

	public BaseObjectWithExtsPropertyAccessor() {
		accessor = new ObjectPropertyAccessor();
	}

	@Override
	public Object getProperty(Map context, Object target, Object name) throws OgnlException {
		if (accessor.hasGetProperty(context, target, name)) {
			return accessor.getProperty(context, target, name);
		}
		BaseObjectWithExts<?> t = (BaseObjectWithExts<?>) target;
		return t.getExtendedAttributes().get(name);
	}

	@Override
	public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
		if (accessor.hasSetProperty(context, target, name)) {
			accessor.setProperty(context, target, name, value);
		} else {
			BaseObjectWithExts<?> t = (BaseObjectWithExts<?>) target;
			Object toConvert = null;
			if (value.getClass().isArray()) {
				Object[] array = (Object[]) value;
				toConvert = array[0];
			} else if (value instanceof Iterable<?>) {
				toConvert = Iterables.getFirst((Iterable<?>) value, null);
			} else {
				toConvert = value;
			}

			if (toConvert == null) {
				t.getExtendedAttributes().put((String) name, null);
			} else {
				t.getExtendedAttributes().put((String) name, String.valueOf(toConvert));
			}
		}
	}

	@Override
	public String getSourceAccessor(OgnlContext context, Object target, Object index) {
		return null;
	}

	@Override
	public String getSourceSetter(OgnlContext context, Object target, Object index) {
		return null;
	}

}
