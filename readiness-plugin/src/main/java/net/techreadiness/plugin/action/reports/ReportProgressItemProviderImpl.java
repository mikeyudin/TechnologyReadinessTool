package net.techreadiness.plugin.action.reports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.common.DataGrid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@Qualifier("ReportProgressItemProvider")
public class ReportProgressItemProviderImpl extends ReportItemProviderImpl {

	@Autowired
	CriteriaQuery<Map<String, String>> criteriaQuery;

	@Inject
	private ReportsService reportService;

	@Override
	public Collection<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {

		if (org == null) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}

		if (consortium == null) {
			throw new IllegalStateException("No Data to Display.  Please ensure the appropriate consortium is selected.");
		}

		// Build list of snapshotIds
		Collection<Long> snapshotWindowIds = new ArrayList<>();
		if (snapshotWindows == null || snapshotWindows.isEmpty()) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}

		for (SnapshotWindow snapshot : snapshotWindows) {
			snapshotWindowIds.add(snapshot.getSnapshotWindowId());
		}

		if (org.getOrgTypeName().equals("School")) {
			QueryResult<Map<String, String>> results = reportService.retrieveProgressDataForOrg(snapshotWindowIds,
					org.getOrgId(), minimumRecommendedFlag);
			setTotalNumberOfItems(results.getTotalRowCount());
			return results.getRows();
		}

		QueryResult<Map<String, String>> result = reportService.retrieveProgressDataForChildOrgs(snapshotWindowIds,
				org.getOrgId(), minimumRecommendedFlag, false, grid.getFirstResult(), grid.getPageSize());
		setTotalNumberOfItems(result.getTotalRowCount());
		return result.getRows();
	}

	@Override
	public Collection<Map<String, String>> export(ExportType exportType) {
		List<Long> snapshotWindowIds = Lists.newArrayList();
		if (snapshotWindows == null || snapshotWindows.isEmpty()) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}

		for (SnapshotWindow snapshot : snapshotWindows) {
			snapshotWindowIds.add(snapshot.getSnapshotWindowId());
		}

		if (org.getOrgTypeName().equals("School")) {
			QueryResult<Map<String, String>> results = reportService.retrieveProgressDataForOrg(snapshotWindowIds,
					org.getOrgId(), minimumRecommendedFlag);
			setTotalNumberOfItems(results.getTotalRowCount());
			return results.getRows();
		}

		QueryResult<Map<String, String>> result = reportService.retrieveProgressDataForChildOrgs(snapshotWindowIds,
				org.getOrgId(), minimumRecommendedFlag, true, 0, 0);
		Collection<Map<String, String>> results = result.getRows();
		if (exportType.equals(ExportType.csv) && org.getOrgTypeName().equals("District")) {
			for (Map<String, String> map : results) {
				if (map.get("schoolType") != null) {
					map.put("schoolType", SCHOOL_TYPE.get(map.get("schoolType")) == null ? map.get("schoolType")
							: SCHOOL_TYPE.get(map.get("schoolType")));
				}
			}
		}

		setTotalNumberOfItems(result.getTotalRowCount());

		return results;
	}
}
