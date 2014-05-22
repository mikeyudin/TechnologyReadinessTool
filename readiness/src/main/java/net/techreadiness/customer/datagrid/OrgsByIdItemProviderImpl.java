package net.techreadiness.customer.datagrid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Org;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
@Scope("prototype")
@Transactional(readOnly = true)
public class OrgsByIdItemProviderImpl implements OrgsByIdItemProvider {

	@PersistenceContext
	private EntityManager em;

	private Collection<Org> orgs;

	@Override
	public List<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		List<Map<String, String>> maps = Lists.newArrayList();
		int enrollmentCounts = 0;

		for (OrgDeleteRowInfo orgDeleteRowInfo : getOrgs()) {
			Map<String, String> orgMap = Maps.newHashMap();
			orgDeleteRowInfoToMap(enrollmentCounts, orgDeleteRowInfo, orgMap);
			maps.add(orgMap);
		}

		return maps;
	}

	private static void orgDeleteRowInfoToMap(int enrollmentCounts, OrgDeleteRowInfo orgDeleteRowInfo,
			Map<String, String> orgMap) {
		orgMap.put("orgId", String.valueOf(orgDeleteRowInfo.getOrgId()));
		orgMap.put("name", orgDeleteRowInfo.getName());
		orgMap.put("code", orgDeleteRowInfo.getCode());
		if (orgDeleteRowInfo.getInactive()) {
			orgMap.put("inactive", "closed");
		} else {
			orgMap.put("inactive", "open");
		}
		orgMap.put("participations", String.valueOf(orgDeleteRowInfo.getParticipations()));
		orgMap.put("contacts", String.valueOf(orgDeleteRowInfo.getContacts()));
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<Map<String, String>> grid) {
		return getOrgs().size();
	}

	@Override
	public void setOrgs(Collection<Org> orgs) {
		this.orgs = orgs;
	}

	private List<OrgDeleteRowInfo> getOrgs() {
		Collection<Long> orgIds = Lists.newArrayList();

		for (Org org : orgs) {
			orgIds.add(org.getOrgId());
		}
		return findOrgDeleteInfoByIds(orgIds);
	}

	public List<OrgDeleteRowInfo> findOrgDeleteInfoByIds(Collection<Long> orgIds) {
		if (orgIds.isEmpty()) {
			return Lists.<OrgDeleteRowInfo> newArrayList();
		}

		Query query = em.createQuery("select " + "new net.techreadiness.customer.datagrid.OrgDeleteRowInfo("
				+ "o.orgId, o.name, o.code, o.inactive, " + "count(distinct c.contactId), "
				+ "count(distinct op.orgPartId)) " + "from OrgDO o left join o.contacts c left join o.orgParts op "
				+ "where o.orgId in (:orgIds) " + "group by o.orgId ");

		query.setParameter("orgIds", orgIds);

		return query.getResultList();
	}
}
