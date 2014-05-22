package net.techreadiness.plugin.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.dao.BaseDAO;
import net.techreadiness.plugin.persistence.domain.SnapshotWindowDO;

public interface SnapshotWindowDao extends BaseDAO<SnapshotWindowDO> {

	SnapshotWindowDO findByScopeAndName(Long scopeId, String name);

	List<SnapshotWindowDO> findUnexecutedSnapshots();

	List<SnapshotWindowDO> findActiveSnapshots(Long scopeId);

	void delete(Long snapshotWindowId);
}
