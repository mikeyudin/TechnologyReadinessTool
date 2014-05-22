package net.techreadiness.batch;

import java.util.HashMap;

import net.techreadiness.persistence.AuditedBaseEntityWithExt;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;

public class EntityExtAttrPropertyAccessor implements PropertyAccessor {

	@Override
	public Class<?>[] getSpecificTargetClasses() {
		return new Class[] { AuditedBaseEntityWithExt.class };
	}

	@Override
	public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
		if (target instanceof AuditedBaseEntityWithExt && name != null) {
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
		AuditedBaseEntityWithExt objWithExt = (AuditedBaseEntityWithExt) target;
		return new TypedValue(objWithExt.getExtAttributes().get(name));
	}

	@Override
	public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
		if (target instanceof AuditedBaseEntityWithExt && name != null) {
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
		AuditedBaseEntityWithExt objWithExt = (AuditedBaseEntityWithExt) target;
		if (objWithExt.getExtAttributes() == null) {
			objWithExt.setExtAttributes(new HashMap<String, String>());
		}
		objWithExt.getExtAttributes().put(name, (String) newValue);
	}

}
