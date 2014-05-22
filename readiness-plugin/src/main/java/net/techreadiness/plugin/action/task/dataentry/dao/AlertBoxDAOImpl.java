package net.techreadiness.plugin.action.task.dataentry.dao;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.dao.BaseDAOImpl;
import net.techreadiness.persistence.domain.DeviceDO;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class AlertBoxDAOImpl extends BaseDAOImpl<DeviceDO> implements AlertBoxDAO {

	@Override
	public Long getDeviceCountMissingReadinessDeterminants(Long orgId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(d) ");
		sb.append("FROM DeviceDO d ");
		sb.append("WHERE (d.operatingSystem IS NULL OR d.operatingSystem = '' ");
		sb.append("OR d.memory IS NULL OR d.memory = '' ");
		sb.append("OR d.screenResolution IS NULL OR d.screenResolution = '' ");
		sb.append("OR d.environment IS NULL OR d.environment = '' ");
		sb.append("OR d.monitorDisplaySize IS NULL OR d.monitorDisplaySize = '') ");
		sb.append("AND d.org.orgId = :orgId");

		TypedQuery<Long> query = em.createQuery(sb.toString(), Long.class);
		query.setParameter("orgId", orgId);

		return getSingleResult(query);
	}

	@Override
	public Long getDeviceCount(Long orgId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(d) ");
		sb.append("FROM DeviceDO d ");
		sb.append("WHERE d.org.orgId = :orgId");

		TypedQuery<Long> query = em.createQuery(sb.toString(), Long.class);
		query.setParameter("orgId", orgId);

		return getSingleResult(query);
	}

}
