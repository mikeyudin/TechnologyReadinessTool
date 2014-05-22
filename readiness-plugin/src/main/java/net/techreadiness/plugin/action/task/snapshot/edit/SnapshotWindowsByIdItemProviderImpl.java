package net.techreadiness.plugin.action.task.snapshot.edit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.persistence.domain.SnapshotWindowDO;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProviderImpl;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@org.springframework.context.annotation.Scope("prototype")
@Transactional(readOnly = true)
public class SnapshotWindowsByIdItemProviderImpl extends DataGridItemProviderImpl<SnapshotWindow> implements
		SnapshotWindowsByIdItemProvider, Serializable {

	private static final long serialVersionUID = 1L;

	private List<SnapshotWindow> snapshotWindows;

	@Inject
	MappingService mappingService;

	@Inject
	CriteriaQuery<SnapshotWindowDO> criteriaQuery;

	@Override
	public List<SnapshotWindow> getPage(DataGrid<SnapshotWindow> grid) {

		Criteria criteria = createCriteria(grid);

		StringBuilder sb = new StringBuilder("select sw.* from readiness.snapshot_window sw ");

		List<Long> iDs = new ArrayList<>();
		for (SnapshotWindow sw : snapshotWindows) {
			iDs.add(sw.getId());
		}

		sb.append(" where sw.snapshot_window_id in(:ids)");
		criteria.getParameters().putAll("ids", iDs);

		criteriaQuery.setBaseSubSelect(sb.toString());

		QueryResult<SnapshotWindowDO> result = criteriaQuery.getData(criteria, SnapshotWindowDO.class);
		setTotalNumberOfItems(result.getTotalRowCount());
		return mappingService.mapFromDOList(result.getRows());
	}

	@Override
	public void setSnapshotWindows(List<SnapshotWindow> snapshots) {
		snapshotWindows = snapshots;
	}
}
