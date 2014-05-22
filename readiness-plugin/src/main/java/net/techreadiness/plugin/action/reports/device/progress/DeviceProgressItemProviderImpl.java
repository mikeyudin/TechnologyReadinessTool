package net.techreadiness.plugin.action.reports.device.progress;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.action.reports.ReportItemProviderImpl;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.common.DataGrid;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@Qualifier("DeviceProgressItemProvider")
public class DeviceProgressItemProviderImpl extends ReportItemProviderImpl {

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

		Collection<Map<String, String>> results;

		if (org.getOrgTypeName().equals("School")) {
			QueryResult<Map<String, String>> resultQuery = reportService.retrieveProgressDataForOrg(snapshotWindowIds,
					org.getOrgId(), minimumRecommendedFlag);
			setTotalNumberOfItems(resultQuery.getTotalRowCount());
			results = resultQuery.getRows();
		} else {

			QueryResult<Map<String, String>> result = reportService.retrieveProgressDataForChildOrgs(snapshotWindowIds,
					org.getOrgId(), minimumRecommendedFlag, true, 0, 0);
			setTotalNumberOfItems(result.getTotalRowCount());
			results = result.getRows();
		}

		if (exportType.equals(ExportType.pdf)) {
			for (Map<String, String> map : results) {
				for (Map.Entry<String, String> entry : map.entrySet()) {
					// there is a passing percent for each snapshot and we need to format each one.
					if (entry.getKey().startsWith("devicePassingPercent_")) {
						// We found a passing percent column, so retrieve the passing percent.
						String percentKey = entry.getKey();
						String percent = map.get(percentKey) == null ? "" : map.get(percentKey);

						// value after the '_' is the ID of the snapshot
						String snapshotId = percentKey.replace("devicePassingPercent_", "");

						// use the snapshot ID determine the map key for the count and then retrieve the count.
						String tbdKey = "deviceTbdCount_" + snapshotId;
						String tbdCount = map.get(tbdKey);

						if (NumberUtils.toInt(tbdCount) > 0) {
							percent += " <span style=\"color: #9a9a9a\">TBD</span>";
						}

						// update the displayed value.
						map.put(percentKey, percent);

					}
				}
			}
		}

		if (exportType.equals(ExportType.csv)) {
			for (Map<String, String> map : results) {
				if (org.getOrgTypeName().equals("District")) {
					if (map.get("schoolType") != null) {
						map.put("schoolType", SCHOOL_TYPE.get(map.get("schoolType")) == null ? map.get("schoolType")
								: SCHOOL_TYPE.get(map.get("schoolType")));
					}
				}
				for (Map.Entry<String, String> entry : map.entrySet()) {
					// there is a passing percent for each snapshot and we need to format each one.
					if (entry.getKey().startsWith("devicePassingPercent_")) {
						// We found a passing percent column, so retrieve the passing percent.
						String percentKey = entry.getKey();
						String percent = map.get(percentKey) == null ? "" : map.get(percentKey);

						// value after the '_' is the ID of the snapshot
						String snapshotId = percentKey.replace("devicePassingPercent_", "");

						// use the snapshot ID determine the map key for the count and then retrieve the count.
						String tbdKey = "deviceTbdCount_" + snapshotId;
						String tbdCount = map.get(tbdKey);

						if (NumberUtils.toInt(tbdCount) > 0) {
							percent += " TBD";
						}

						// update the displayed value.
						map.put(percentKey, percent);

					}
				}
			}
		}

		return results;
	}
}
