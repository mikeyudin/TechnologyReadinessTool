package net.techreadiness.persistence.dao;

import java.util.List;

import javax.inject.Named;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeExtDO;

import org.springframework.stereotype.Repository;

@Repository
@Named("scopeExtDAOImpl")
public class ScopeExtDAOImpl extends BaseDAOImpl<ScopeExtDO> implements ScopeExtDAO, ExtDAO<ScopeDO, ScopeExtDO> {
	/**
	 * This method is used to find the most relevant configuration item from the scope_ext table. By most relevant we mean to
	 * say the closest(lowest) in the tree to the passed in scopeId.
	 */
	@Override
	public ScopeExtDO getLowestExistingConfigurationItem(Long scopeId, String entityCode) {

		StringBuilder sb = new StringBuilder();
		sb.append("select se ");
		sb.append("from ScopeExtDO se, ScopeTreeDO tree, EntityFieldDO ef ");
		sb.append("where se.scope.scopeId = tree.ancestorScope.scopeId ");
		sb.append("and tree.scope.scopeId =:scopeId ");
		sb.append("and se.entityField.entityFieldId=ef.entityFieldId ");
		sb.append("and ef.code=:fieldCode ");
		sb.append("order by tree.ancestorDepth desc ");

		TypedQuery<ScopeExtDO> query = em.createQuery(sb.toString(), ScopeExtDO.class);

		query.setParameter("scopeId", scopeId);
		query.setParameter("fieldCode", entityCode);
		query.setMaxResults(1);

		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getSingleResult(query);
	}

	@Override
	public List<ScopeExtDO> getExtDOs(ScopeDO baseEntityWithExt) {
		return baseEntityWithExt.getScopeExts();
	}

	@Override
	public ScopeExtDO getNew() {
		return new ScopeExtDO();
	}

	@Override
	public ScopeExtDO getMostRecentlyUpdated(Long scopeId, Long viewDefId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select scopeExt from ScopeExtDO scopeExt where scopeExt.changeDate = ");
		sb.append("(select max(se.changeDate) from ScopeExtDO se ");
		sb.append("join se.entityField ef ");
		sb.append("join ef.viewDefFields vdf ");
		sb.append("where se.scope.scopeId = :scopeId and vdf.viewDef.viewDefId = :viewDefId)");

		TypedQuery<ScopeExtDO> query = em.createQuery(sb.toString(), ScopeExtDO.class);
		query.setParameter("scopeId", scopeId);
		query.setParameter("viewDefId", viewDefId);
		query.setMaxResults(1);

		return getSingleResult(query);
	}
}
