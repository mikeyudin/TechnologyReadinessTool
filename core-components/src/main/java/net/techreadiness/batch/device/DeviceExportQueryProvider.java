package net.techreadiness.batch.device;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.DeviceDO;
import net.techreadiness.service.ServiceContext;

import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.context.annotation.Scope;

@Named
@Scope("prototype")
public class DeviceExportQueryProvider implements JpaQueryProvider {
	private EntityManager entityManager;
	@Inject
	private ServiceContext serviceContext;

	@Override
	public Query createQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("select d from DeviceDO d ");
		sb.append("join d.org o ");
		sb.append("join o.orgTrees ot ");
		sb.append("where ot.ancestorOrg.orgId = :orgId");

		TypedQuery<DeviceDO> query = entityManager.createQuery(sb.toString(), DeviceDO.class);
		query.setParameter("orgId", serviceContext.getOrgId());

		return query;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
