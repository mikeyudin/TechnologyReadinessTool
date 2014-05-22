package net.techreadiness.batch;

import java.util.HashMap;

import net.techreadiness.service.object.BaseObjectWithExts;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;

public class ExtendedAttributePropertyAccessor implements PropertyAccessor {

	@Override
	public Class<?>[] getSpecificTargetClasses() {
		return new Class[] { BaseObjectWithExts.class };
	}

	@Override
	public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
		if (target instanceof BaseObjectWithExts && name != null) {
			for (PropertyAccessor accessor : context.getPropertyAccessors()) {
				if (accessor instanceof ReflectivePropertyAccessor) {
					return !accessor.canRead(context, target, name);
				}
			}
		}
		return false;
	}

	@Override
	public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
		BaseObjectWithExts<?> objWithExt = (BaseObjectWithExts<?>) target;
		return new TypedValue(objWithExt.getExtendedAttributes().get(name));
	}

	@Override
	public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
		if (target instanceof BaseObjectWithExts && name != null) {
			for (PropertyAccessor accessor : context.getPropertyAccessors()) {
				if (accessor instanceof ReflectivePropertyAccessor) {
					return !accessor.canWrite(context, target, name);
				}
			}
		}
		return false;
	}

	@Override
	public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
		BaseObjectWithExts<?> objWithExt = (BaseObjectWithExts<?>) target;
		if (objWithExt.getExtendedAttributes() == null) {
			objWithExt.setExtendedAttributes(new HashMap<String, String>());
		}
		// TODO Attempt to convert instead of cast to string
		objWithExt.getExtendedAttributes().put(name, (String) newValue);
	}

}
