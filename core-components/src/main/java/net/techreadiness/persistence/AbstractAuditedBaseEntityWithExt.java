package net.techreadiness.persistence;

import net.techreadiness.persistence.domain.EntityFieldDO;

public abstract class AbstractAuditedBaseEntityWithExt<T extends AuditedBaseEntityWithExt> extends AuditedBaseEntity {

	public abstract T getParent();

	public abstract void setParent(T baseEntityWithExt);

	public abstract EntityFieldDO getEntityField();

	public abstract void setEntityField(EntityFieldDO entityFieldDO);

	public abstract String getValue();

	public abstract void setValue(String value);

}
