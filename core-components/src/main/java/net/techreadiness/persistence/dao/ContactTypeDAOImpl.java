package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.techreadiness.persistence.domain.ContactTypeDO;
import net.techreadiness.persistence.domain.ContactTypeDO_;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgDO_;
import net.techreadiness.persistence.domain.OrgPartDO;
import net.techreadiness.persistence.domain.OrgPartDO_;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeDO_;
import net.techreadiness.persistence.domain.ScopeTreeDO;
import net.techreadiness.persistence.domain.ScopeTreeDO_;
import net.techreadiness.persistence.domain.ScopeTypeDO;
import net.techreadiness.persistence.domain.ScopeTypeDO_;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class ContactTypeDAOImpl extends BaseDAOImpl<ContactTypeDO> implements ContactTypeDAO {

	@Override
	public List<ContactTypeDO> findContactTypesForScope(Long scopeId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContactTypeDO> criteria = cb.createQuery(ContactTypeDO.class);
		Root<ContactTypeDO> contactType = criteria.from(ContactTypeDO.class);

		Join<ContactTypeDO, ScopeDO> scope = contactType.join(ContactTypeDO_.scope);
		Join<ScopeDO, ScopeTypeDO> scopeType = scope.join(ScopeDO_.scopeType);
		Join<ScopeDO, ScopeTreeDO> scopeTree = scope.join(ScopeDO_.ancestorScopeTrees);
		Predicate typeClause = cb.equal(scopeType.get(ScopeTypeDO_.allowOrg), Short.valueOf("1"));
		scopeTree.get(ScopeTreeDO_.scope);
		Predicate pathClause = cb.equal(scopeTree.get(ScopeTreeDO_.scope).get(ScopeDO_.scopeId), scopeId);
		criteria.where(typeClause, pathClause);
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public List<ContactTypeDO> findContactTypesForOrgPart(Long orgPartId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContactTypeDO> criteria = cb.createQuery(ContactTypeDO.class);
		Root<OrgPartDO> orgPart = criteria.from(OrgPartDO.class);
		Join<OrgPartDO, OrgDO> org = orgPart.join(OrgPartDO_.org);
		Join<OrgDO, ScopeDO> scope = org.join(OrgDO_.scope);
		Join<ScopeDO, ContactTypeDO> contactType = scope.join(ScopeDO_.contactTypes);
		criteria.select(contactType);
		Predicate orgPartClause = cb.equal(orgPart.get(OrgPartDO_.orgPartId), orgPartId);
		criteria.where(orgPartClause);
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public ContactTypeDO getContactType(Long scopeId, String contactTypeCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContactTypeDO> criteria = cb.createQuery(ContactTypeDO.class);
		Root<ContactTypeDO> contactType = criteria.from(ContactTypeDO.class);

		Join<ContactTypeDO, ScopeDO> scope = contactType.join(ContactTypeDO_.scope);
		Join<ScopeDO, ScopeTypeDO> scopeType = scope.join(ScopeDO_.scopeType);
		Join<ScopeDO, ScopeTreeDO> scopeTree = scope.join(ScopeDO_.ancestorScopeTrees);
		Predicate typeClause = cb.equal(scopeType.get(ScopeTypeDO_.allowOrg), Short.valueOf("1"));
		scopeTree.get(ScopeTreeDO_.scope);
		Predicate pathClause = cb.equal(scopeTree.get(ScopeTreeDO_.scope).get(ScopeDO_.scopeId), scopeId);
		Predicate contactTypeClause = cb.equal(contactType.get(ContactTypeDO_.code), contactTypeCode);
		criteria.where(typeClause, pathClause, contactTypeClause);

		return getSingleResult(em.createQuery(criteria));
	}

}
