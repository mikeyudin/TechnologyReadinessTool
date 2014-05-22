package net.techreadiness.batch;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class FieldSetPropertyAccessor implements PropertyAccessor {

	private final FieldSet fieldSet;

	public FieldSetPropertyAccessor(FieldSet fieldSet) {
		this.fieldSet = fieldSet;
	}

	@Override
	public Class<?>[] getSpecificTargetClasses() {
		return new Class[] { FieldSet.class };
	}

	@Override
	public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
		return true;
	}

	@Override
	public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
		String value = fieldSet.readString(name);
		TypedValue typedValue = new TypedValue(value);
		return typedValue;
	}

	@Override
	public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
		return false;
	}

	@Override
	public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
		throw new AccessException("Values contained in a FieldSet are read-only.");
	}

}
