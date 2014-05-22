package net.techreadiness.customer.action.filebatch;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.domain.FileDO;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProviderImpl;
import net.techreadiness.service.object.File;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Component
@org.springframework.context.annotation.Scope("prototype")
@Transactional(readOnly = true)
public class FileItemProviderImpl extends DataGridItemProviderImpl<File> implements FileItemProvider, Serializable {

	private static final long serialVersionUID = 1L;

	private ServiceContext serviceContext;

	@Inject
	MappingService mappingService;

	@Autowired
	CriteriaQuery<FileDO> criteriaQuery;

	@Override
	public List<File> getPage(DataGrid<File> grid) {

		Criteria criteria = createCriteria(grid, "fileStatuses", "fileTypes");
		criteriaQuery.setFullTextSearchColumns(new String[] { "display_filename", "filename" });

		// Create the base query to limit to non deleted users within the user's scope.
		StringBuilder sb = new StringBuilder();
		sb.append(" select f.* from file f, org_tree ot ");
		sb.append("  where ot.ancestor_org_id = :org_id ");
		sb.append("  and ot.org_id = f.org_id ");
		Collection<String> fileTypes = grid.getFilters().get("fileTypes");
		Collection<String> fileStatuses = grid.getFilters().get("fileStatuses");

		Collection<Long> fileTypeIds = Lists.newLinkedList();
		for (String type : fileTypes) {
			fileTypeIds.add(Long.valueOf(type));
		}
		if (!fileTypeIds.isEmpty()) {
			sb.append(" and ( file_type_id in (:fileTypeIds) )");
			for (Long id : fileTypeIds) {
				criteria.getParameters().put("fileTypeIds", id);
			}

		}

		if (!fileStatuses.isEmpty()) {
			sb.append(" and ( status in (:fileStatuses) )");
			for (String status : fileStatuses) {
				criteria.getParameters().put("fileStatuses", status);
			}
		}

		criteria.getParameters().put("org_id", serviceContext.getOrgId().toString());
		sb.append("  order by f.request_date DESC ");
		criteriaQuery.setBaseSubSelect(sb.toString());

		QueryResult<FileDO> result = criteriaQuery.getData(criteria, FileDO.class);
		setTotalNumberOfItems(result.getTotalRowCount());
		return mappingService.mapFromDOList(result.getRows());
	}

	@Override
	public void setServiceContext(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}
}
