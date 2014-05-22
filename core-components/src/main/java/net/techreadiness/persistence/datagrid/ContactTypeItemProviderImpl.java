package net.techreadiness.persistence.datagrid;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.techreadiness.persistence.dao.ScopeDAO;
import net.techreadiness.persistence.domain.ContactTypeDO;
import net.techreadiness.persistence.domain.ContactTypeDO_;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeDO_;
import net.techreadiness.persistence.domain.ScopeTreeDO;
import net.techreadiness.persistence.domain.ScopeTreeDO_;
import net.techreadiness.persistence.domain.ScopeTypeDO;
import net.techreadiness.persistence.domain.ScopeTypeDO_;
import net.techreadiness.service.common.DataGrid;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("prototype")
@Transactional(readOnly = true)
public class ContactTypeItemProviderImpl extends AbstractCriteriaItemProvider<ContactTypeDO> implements
		ContactTypeItemProvider {

	private Long scopeId;
	@Inject
	private ScopeDAO scopeDao;

	@Override
	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	@Override
	protected CriteriaQuery<ContactTypeDO> getCriteria(DataGrid<ContactTypeDO> grid) {
		ScopeDO selectedScope = scopeDao.getById(scopeId);
		if (!selectedScope.getScopeType().isAllowOrg()) {
			throw new IllegalStateException("Contact types can only be defined at scopes that allow organizations.");
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContactTypeDO> criteria = cb.createQuery(ContactTypeDO.class);
		Root<ContactTypeDO> contactType = criteria.from(ContactTypeDO.class);

		Join<ContactTypeDO, ScopeDO> scope = contactType.join(ContactTypeDO_.scope);
		Join<ScopeDO, ScopeTypeDO> scopeType = scope.join(ScopeDO_.scopeType);
		Join<ScopeDO, ScopeTreeDO> scopeTree = scope.join(ScopeDO_.scopeTrees);
		Predicate typeClause = cb.equal(scopeType.get(ScopeTypeDO_.allowOrg), Short.valueOf("1"));
		scopeTree.get(ScopeTreeDO_.scope);
		Predicate pathClause = cb.equal(scopeTree.get(ScopeTreeDO_.ancestorScope).get(ScopeDO_.scopeId), scopeId);
		criteria.where(typeClause, pathClause);
		return criteria;
	}

}
