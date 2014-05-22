package net.techreadiness.persistence;

import net.techreadiness.service.object.BaseObject;

public interface ServiceObjectMapped {
	Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType();
}
