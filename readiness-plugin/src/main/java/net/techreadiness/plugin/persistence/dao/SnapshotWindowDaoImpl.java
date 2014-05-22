package net.techreadiness.plugin.persistence.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.dao.BaseDAOImpl;
import net.techreadiness.plugin.persistence.domain.SnapshotWindowDO;
import net.techreadiness.plugin.service.reports.ReportsService;

import org.springframework.stereotype.Repository;

@Repository
public class SnapshotWindowDaoImpl extends BaseDAOImpl<SnapshotWindowDO> implements SnapshotWindowDao {

	@Override
	public SnapshotWindowDO findByScopeAndName(Long scopeId, String window) {

		TypedQuery<SnapshotWindowDO> query = em.createQuery(
				"select swdo from SnapshotWindowDO swdo where  swdo.scope.scopeId = :scopeId and swdo.name = :window ",
				SnapshotWindowDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("window", window);

		return getSingleResult(query);
	}

	@Override
	public List<SnapshotWindowDO> findUnexecutedSnapshots() {
		TypedQuery<SnapshotWindowDO> query = em.createQuery(
				"select swdo from SnapshotWindowDO swdo where swdo.executeDate is null", SnapshotWindowDO.class);

		return getResultList(query);
	}

	@Override
	public List<SnapshotWindowDO> findActiveSnapshots(Long scopeId) {
		TypedQuery<SnapshotWindowDO> query = em.createQuery(
				"select swdo from SnapshotWindowDO swdo where swdo.executeDate is not null " + " and (swdo.name = '"
						+ ReportsService.DEFAULT_SNAPSHOT_WINDOW + "'" + " or swdo.visible = 1) "
						+ " and swdo.scope.scopeId = :scopeId " + " order by swdo.snapshotWindowId desc ",
				SnapshotWindowDO.class);
		query.setParameter("scopeId", scopeId);
		return getResultList(query);
	}

	@Override
	public void delete(Long snapshotWindowId) {

		Query query = em.createNativeQuery("call readiness.snapshot_delete(:snapshotWindowId)");
		query.setParameter("snapshotWindowId", snapshotWindowId);
		query.executeUpdate();
	}
}
