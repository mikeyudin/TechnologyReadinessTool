package net.techreadiness.persistence.datagrid;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.PermissionDO;
import net.techreadiness.service.common.DataGrid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Scope("prototype")
@Repository
@Transactional(readOnly = true)
public class PermissionByScopeItemProviderImpl implements PermissionByScopeItemProvider {
	private final Logger log = LoggerFactory.getLogger(PermissionByScopeItemProviderImpl.class);
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<PermissionDO> getPage(DataGrid<PermissionDO> grid) {

		int start = (grid.getPage() - 1) * grid.getPageSize();
		TypedQuery<PermissionDO> query = getQuery(grid);
		query.setFirstResult(start);
		query.setMaxResults(grid.getPageSize());
		return new ArrayList<>(query.getResultList());
	}

	@Override
	public int getTotalNumberOfItems(DataGrid<PermissionDO> grid) {
		TypedQuery<PermissionDO> query = getQuery(grid);

		log.debug("size is: {}" + query.getResultList().size());

		return query.getResultList().size();
	}

	private TypedQuery<PermissionDO> getQuery(DataGrid<PermissionDO> grid) {

		TypedQuery<PermissionDO> query = em.createQuery("select p from PermissionDO p ", PermissionDO.class);
		return query;
	}
}
