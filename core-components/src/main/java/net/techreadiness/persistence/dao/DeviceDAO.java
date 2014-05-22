package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.DeviceDO;

public interface DeviceDAO extends BaseDAO<DeviceDO> {

	List<DeviceDO> findByOrgId(Long orgId);

	List<DeviceDO> findById(List<Long> deviceIds);

	int deleteDevicesForOrg(Long orgId);

	int createSnapshotSetOfDevicesForChildOrgs(Long scopeId, Long parentOrgId, Long snapshotSummaryId);
}
