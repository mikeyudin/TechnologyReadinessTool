package net.techreadiness.plugin.action.task.dataentry.dao;

import net.techreadiness.persistence.dao.BaseDAO;
import net.techreadiness.persistence.domain.DeviceDO;

public interface AlertBoxDAO extends BaseDAO<DeviceDO> {

	Long getDeviceCountMissingReadinessDeterminants(Long orgId);

	Long getDeviceCount(Long orgId);
}
