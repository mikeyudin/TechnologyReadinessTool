package net.techreadiness.persistence.datagrid;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope("prototype")
@Transactional(readOnly = true)
public class OrgByUserItemProviderImpl implements OrgByUserItemProvider {

	@PersistenceContext
	protected EntityManager em;

	@Inject
	MappingService mappingService;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	private Long userId;
	private Long scopeId;
	private boolean ignoreScope;

	public OrgByUserItemProviderImpl() {
		super();
	}

	@Override
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public void setIgnoreScope(boolean ignoreScope) {
		this.ignoreScope = ignoreScope;
	}

	@Override
	@Transactional(readOnly = true)
	public int getTotalNumberOfItems(DataGrid<Org> dataGrid) {
		return getOrgInfo().size();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Org> getPage(DataGrid<Org> dataGrid) {
		if (dataGrid.getFilters().containsKey("currentScope")) {
			ignoreScope = true;
		}

		return mappingService.mapFromDOList(getOrgInfo());
	}

	private List<OrgDO> getOrgInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("select uo.org");
		sb.append(" from UserOrgDO uo ");
		sb.append(" where uo.user.userId = :userId ");
		if (!ignoreScope) {
			sb.append(" and uo.org.scope.scopeId =:scopeId ");
		}

		TypedQuery<OrgDO> query = em.createQuery(sb.toString(), OrgDO.class);
		query.setParameter("userId", userId);
		if (!ignoreScope) {
			query.setParameter("scopeId", scopeId);
		}

		return query.getResultList();
	}
}
