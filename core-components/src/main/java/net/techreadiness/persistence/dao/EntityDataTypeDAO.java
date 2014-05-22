package net.techreadiness.persistence.dao;

import net.techreadiness.persistence.domain.EntityDataTypeDO;

public interface EntityDataTypeDAO extends BaseDAO<EntityDataTypeDO> {

	public enum EntityDataTypeCodes {
		STRING, NUMBER, BOOLEAN, DATE, DATETIME;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
}
