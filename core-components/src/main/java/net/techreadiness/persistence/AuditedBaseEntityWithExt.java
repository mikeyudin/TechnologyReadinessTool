package net.techreadiness.persistence;

import java.util.Map;

public abstract class AuditedBaseEntityWithExt extends AuditedBaseEntity {
	private transient Map<String, String> extAttributes;

	public Map<String, String> getExtAttributes() {
		if (extAttributes == null) {
			populateExtAttributes();
			extAttributes.putAll(getAsMap());
		}
		return extAttributes;
	}

	public boolean isExtAttributesNull() {
		return extAttributes == null;
	}

	public void setExtAttributes(Map<String, String> extAttributes) {
		this.extAttributes = extAttributes;
	}

	protected abstract void populateExtAttributes();
}
