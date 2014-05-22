package net.techreadiness.plugin.action.snapshot;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.persistence.domain.SnapshotWindowDO;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProviderImpl;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@org.springframework.context.annotation.Scope("prototype")
@Transactional(readOnly = true)
public class SnapshotItemProviderImpl extends DataGridItemProviderImpl<SnapshotWindow> implements SnapshotItemProvider,
		Serializable {

	private static final long serialVersionUID = 1L;

	private ServiceContext serviceContext;

	@Inject
	MappingService mappingService;

	@Inject
	CriteriaQuery<SnapshotWindowDO> criteriaQuery;

	@Override
	public List<SnapshotWindow> getPage(DataGrid<SnapshotWindow> grid) {

		Criteria criteria = createCriteria(grid);
		criteriaQuery.setFullTextSearchColumns(new String[] { "name" });

		StringBuilder sb = new StringBuilder("select sw.* from readiness.snapshot_window sw ");
		sb.append("where sw.scope_id = :scopeId ");
		sb.append("and sw.name != :name");

		criteriaQuery.setBaseSubSelect(sb.toString());
		criteria.getParameters().put("scopeId", serviceContext.getScopeId());
		criteria.getParameters().put("name", ReportsService.DEFAULT_SNAPSHOT_WINDOW);

		QueryResult<SnapshotWindowDO> result = criteriaQuery.getData(criteria, SnapshotWindowDO.class);
		setTotalNumberOfItems(result.getTotalRowCount());
		return mappingService.mapFromDOList(result.getRows());
	}

	@Override
	public void setServiceContext(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}
}
