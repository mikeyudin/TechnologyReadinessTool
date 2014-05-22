package net.techreadiness.persistence.datagrid;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.object.Role;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Scope("prototype")
@Repository
@Transactional(readOnly = true)
public class RoleByUserItemProviderImpl implements RoleByUserItemProvider {

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

	public RoleByUserItemProviderImpl() {
		super();
	}

	@Override
	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	@Override
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	@Transactional(readOnly = true)
	public int getTotalNumberOfItems(DataGrid<Role> dataGrid) {
		return getRoleInfo().size();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Role> getPage(DataGrid<Role> dataGrid) {
		if (dataGrid.getFilters().containsKey("currentScope")) {
			ignoreScope = true;
		}

		return mappingService.mapFromDOList(getRoleInfo());
	}

	private List<RoleDO> getRoleInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("select r ");
		sb.append("from RoleDO r left outer join r.userRoles ur");
		sb.append(" where ur.user.userId = :userId ");
		if (!ignoreScope) {
			sb.append(" and ur.role.scope.scopeId =:scopeId ");
		}

		Query query = em.createQuery(sb.toString());
		query.setParameter("userId", userId);
		if (!ignoreScope) {
			query.setParameter("scopeId", scopeId);
		}
		return query.getResultList();
	}

}
