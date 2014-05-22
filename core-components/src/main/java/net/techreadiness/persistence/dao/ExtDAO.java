package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.AbstractAuditedBaseEntityWithExt;
import net.techreadiness.persistence.AuditedBaseEntityWithExt;

public interface ExtDAO<EntityType extends AuditedBaseEntityWithExt, ExtType extends AbstractAuditedBaseEntityWithExt<EntityType>>
		extends BaseDAO<ExtType> {

	List<ExtType> getExtDOs(EntityType baseEntityWithExt);

	ExtType getNew();

}
