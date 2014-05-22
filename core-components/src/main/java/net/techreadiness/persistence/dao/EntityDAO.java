package net.techreadiness.persistence.dao;

import net.techreadiness.persistence.domain.EntityDO;

public interface EntityDAO extends BaseDAO<EntityDO> {

	public enum EntityTypeCode {
		ORG, ORG_PART, SCOPE, CONTACT, USER, ROLE, DEVICE, SNAPSHOT;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	EntityDO getHighestPriority(Long scopeId, EntityDAO.EntityTypeCode typeCode);
}
