package net.techreadiness.persistence.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.techreadiness.persistence.domain.OrgTypeDO;
import net.techreadiness.persistence.domain.OrgTypeDO_;
import net.techreadiness.persistence.domain.ScopeDO_;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class OrgTypeDAOImpl extends BaseDAOImpl<OrgTypeDO> implements OrgTypeDAO {

	@Override
	public List<OrgTypeDO> findOrgTypesForScope(Long scopeId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrgTypeDO> criteria = cb.createQuery(OrgTypeDO.class);
		Root<OrgTypeDO> root = criteria.from(OrgTypeDO.class);
		criteria.where(cb.equal(root.get(OrgTypeDO_.scope).get(ScopeDO_.scopeId), scopeId));
		TypedQuery<OrgTypeDO> query = em.createQuery(criteria);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);
		return query.getResultList();
	}

	@Override
	public OrgTypeDO getByCode(String orgTypeCode, Long scopeId) {
		StringBuilder sb = new StringBuilder();
		TypedQuery<OrgTypeDO> query = null;

		sb.append(" select ot ");
		sb.append(" from OrgTypeDO ot");
		sb.append(" where ot.code = :orgTypeCode");
		sb.append(" and ot.scope.scopeId = :scopeId");
		query = em.createQuery(sb.toString(), OrgTypeDO.class);
		query.setParameter("orgTypeCode", orgTypeCode);
		query.setParameter("scopeId", scopeId);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getSingleResult(query);
	}

	@Override
	public List<OrgTypeDO> findChildOrgTypes(Long parentOrgTypeId, Long scopeId) {
		StringBuilder sb = new StringBuilder();
		TypedQuery<OrgTypeDO> query = null;

		if (parentOrgTypeId == null) {
			sb.append(" select ot ");
			sb.append(" from OrgTypeDO ot, ScopeTreeDO st ");
			sb.append(" where ot.parentOrgType is null");
			sb.append(" and ot.scope = st.ancestorScope ");
			sb.append(" and st.scope.scopeId = :scopeId ");

			query = em.createQuery(sb.toString(), OrgTypeDO.class);
			query.setParameter("scopeId", scopeId);
		} else {
			sb.append(" select ot ");
			sb.append(" from OrgTypeDO ot ");
			sb.append(" where ot.parentOrgType.orgTypeId = :parentOrgTypeId");

			query = em.createQuery(sb.toString(), OrgTypeDO.class);
			query.setParameter("parentOrgTypeId", parentOrgTypeId);
		}
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getResultList(query);

	}

	@Override
	public List<OrgTypeDO> findOrgTypesByIds(Collection<Long> orgTypeIds) {
		StringBuilder sb = new StringBuilder();
		if (orgTypeIds == null || orgTypeIds.size() == 0) {
			return new ArrayList<>();
		}
		sb.append(" select ot ");
		sb.append(" from OrgTypeDO ot ");
		sb.append(" where ot.orgTypeId in (:orgTypeIds)");
		TypedQuery<OrgTypeDO> query = em.createQuery(sb.toString(), OrgTypeDO.class);
		query.setParameter("orgTypeIds", orgTypeIds);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getResultList(query);
	}

	@Override
	public List<OrgTypeDO> findOrgTypesForScope(Long scopeId, Collection<String> orgTypeCodes) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrgTypeDO> criteria = cb.createQuery(OrgTypeDO.class);
		Root<OrgTypeDO> root = criteria.from(OrgTypeDO.class);
		criteria.where(cb.equal(root.get(OrgTypeDO_.scope).get(ScopeDO_.scopeId), scopeId));

		In<String> inOrgTypeCode = cb.in(root.get(OrgTypeDO_.code));
		if (orgTypeCodes.isEmpty()) {
			inOrgTypeCode.value((String) null);
		} else {
			for (String orgTypeCode : orgTypeCodes) {
				inOrgTypeCode.value(orgTypeCode);
			}
		}
		criteria.where(inOrgTypeCode);
		TypedQuery<OrgTypeDO> query = em.createQuery(criteria);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return query.getResultList();
	}

	@Override
	public List<Long> findDisallowedChildOrgTypeIdsForOrg(Long orgId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select o.orgType.orgTypeId ");
		sb.append("from OrgTreeDO ot, OrgDO o ");
		sb.append("where o.orgId = ot.ancestorOrg.orgId ");
		sb.append("and ot.org.orgId =:orgId ");

		TypedQuery<Long> query = em.createQuery(sb.toString(), Long.class);
		query.setParameter("orgId", orgId);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return query.getResultList();
	}

}
