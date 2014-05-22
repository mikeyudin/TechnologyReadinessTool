package net.techreadiness.ognl;

import java.util.Map;

import net.techreadiness.persistence.AuditedBaseEntityWithExt;
import ognl.ObjectPropertyAccessor;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.PropertyAccessor;

public class BaseDOWithExtPropertyAccessor implements PropertyAccessor {
	private ObjectPropertyAccessor accessor;

	public BaseDOWithExtPropertyAccessor() {
		accessor = new ObjectPropertyAccessor();
	}

	@Override
	public Object getProperty(Map context, Object target, Object name) throws OgnlException {
		if (accessor.hasGetProperty(context, target, name)) {
			return accessor.getProperty(context, target, name);
		}
		AuditedBaseEntityWithExt entity = (AuditedBaseEntityWithExt) target;
		if (name instanceof String) {
			return entity.getExtAttributes().get(name);
		}
		return null;
	}

	@Override
	public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
		if (accessor.hasSetProperty(context, target, name)) {
			accessor.setProperty(context, target, name, value);
		} else {
			AuditedBaseEntityWithExt entity = (AuditedBaseEntityWithExt) target;
			if (name instanceof String) {
				entity.getExtAttributes().put((String) name, (String) value);
			}
		}

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
