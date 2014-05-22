package net.techreadiness.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.DeviceDO;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class DeviceDAOImpl extends BaseDAOImpl<DeviceDO> implements DeviceDAO {

	@Override
	public List<DeviceDO> findByOrgId(Long orgId) {

		StringBuilder sb = new StringBuilder();
		sb.append("select d ");
		sb.append("from DeviceDO d ");
		sb.append("where d.org.orgId =:orgId ");

		TypedQuery<DeviceDO> query = em.createQuery(sb.toString(), DeviceDO.class);
		query.setParameter("orgId", orgId);

		return getResultList(query);
	}

	@Override
	public List<DeviceDO> findById(List<Long> deviceIds) {
		if (deviceIds.size() < 1) {
			return new ArrayList<>();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select d ");
		sb.append("from DeviceDO d ");
		sb.append("where d.deviceId in (:deviceIds) ");
		TypedQuery<DeviceDO> query = em.createQuery(sb.toString(), DeviceDO.class);
		query.setParameter("deviceIds", deviceIds);

		return getResultList(query);
	}

	@Override
	public int deleteDevicesForOrg(Long orgId) {

		int numDeleted = 0;

		StringBuilder sb = new StringBuilder();
		sb.append("select d ");
		sb.append("from DeviceDO d ");
		sb.append("where d.org.orgId = :orgId ");

		TypedQuery<DeviceDO> query = em.createQuery(sb.toString(), DeviceDO.class);
		query.setParameter("orgId", orgId);

		List<DeviceDO> devices = getResultList(query);

		if (devices != null) {
			for (DeviceDO deviceDO : devices) {
				em.remove(deviceDO);
				numDeleted++;
			}
		}
		return numDeleted;
	}

	@Override
	public int createSnapshotSetOfDevicesForChildOrgs(Long scopeId, Long parentOrgId, Long snapshotSummaryId) {
		int numUpdate = 0;

		StringBuilder sb = new StringBuilder();
		sb.append("insert into device ");
		sb.append("(org_id, name, location, count, operating_system, processor, memory, storage, flash_version, java_version, browser, screen_resolution, display_size) ");
		sb.append("select d.org_id, d.name, d.location, d.count, d.operating_system, d.processor, d.memory, d.storage, d.flash_version, d.java_version, d.browser, d.screen_resolution, d.display_size");
		sb.append("from device d where d.org_id in ");
		sb.append("(select o.org_id from org o, org_part op ");
		sb.append("where o.parent_org_id = :parentOrgId and ");
		sb.append("op.org_id = o.org_id and ");
		sb.append("op.scope_id = :scopeId);");

		Query createQuery = em.createQuery(sb.toString());
		createQuery.setParameter("snapshotSummaryId", snapshotSummaryId);
		createQuery.setParameter("parentOrgId", parentOrgId);
		createQuery.setParameter("scopeId", scopeId);

		numUpdate = createQuery.executeUpdate();

		return numUpdate;
	}
}
