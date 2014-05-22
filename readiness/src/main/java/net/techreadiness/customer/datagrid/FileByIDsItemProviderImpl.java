package net.techreadiness.customer.datagrid;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.domain.FileDO;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProviderImpl;
import net.techreadiness.service.object.File;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("prototype")
@Transactional(readOnly = true)
public class FileByIDsItemProviderImpl extends DataGridItemProviderImpl<File> implements FileByIDsItemProvider {
	private Collection<Long> fileIds;

	@Inject
	MappingService mappingService;
	@Autowired
	CriteriaQuery<FileDO> criteriaQuery;

	@Override
	public List<File> getPage(DataGrid<File> grid) {
		Criteria criteria = createCriteria(grid);
		criteriaQuery.setBaseWhere("main.file_id in (:file_id_list)");
		criteria.getParameters().putAll("file_id_list", fileIds);

		QueryResult<FileDO> result = criteriaQuery.getData(criteria, FileDO.class);
		setTotalNumberOfItems(result.getTotalRowCount());
		return mappingService.mapFromDOList(result.getRows());
	}

	public Collection<Long> getFileIds() {
		return fileIds;
	}

	@Override
	public void setFileIds(Collection<Long> fileIds) {
		this.fileIds = fileIds;
	}

}