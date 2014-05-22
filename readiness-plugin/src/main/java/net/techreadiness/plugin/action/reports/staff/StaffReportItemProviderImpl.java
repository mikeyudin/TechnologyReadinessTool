package net.techreadiness.plugin.action.reports.staff;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.action.reports.ReportItemProviderImpl;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.common.DataGrid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Component
@Qualifier("StaffReportItemProvider")
public class StaffReportItemProviderImpl extends ReportItemProviderImpl {

	@Inject
	private ReportsService dao;

	@Override
	public Collection<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		if (org == null) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}

		if (consortium == null) {
			throw new IllegalStateException("No Data to Display.  Please ensure the appropriate consortium is selected.");
		}

		Collection<Map<String, String>> results = Lists.newArrayList();

		if (org.getOrgTypeName().equals("School")) {
			Map<String, String> orgSummary = Iterables.getFirst(
					dao.retrieveSurveySummaryForOrg(snapshotWindow.getSnapshotWindowId(), org.getOrgId(), question)
							.getRows(), null);

			if (orgSummary == null || orgSummary.isEmpty()) {
				setTotalNumberOfItems(0);
				return Lists.newArrayList();
			}
			setTotalNumberOfItems(1);

			results.add(orgSummary);
		} else {
			QueryResult<Map<String, String>> result = dao.retrieveSurveySummaryForChildOrgs(
					snapshotWindow.getSnapshotWindowId(), org.getOrgId(), question, false, grid.getFirstResult(),
					grid.getPageSize());
			setTotalNumberOfItems(result.getTotalRowCount());
			results = result.getRows();
		}

		if (org.getOrgTypeName().equals("School") || org.getOrgTypeName().equals("District")) {
			for (Map<String, String> map : results) {
				if (StringUtils.isBlank(map.get("levelOfConcern"))) {
					map.put("levelOfConcernCount0to3", "(missing)");
					map.put("levelOfConcernCount4to5", "(missing)");
					map.put("levelOfConcernCount6to7", "(missing)");
					map.put("levelOfConcernCount8to10", "(missing)");
					map.put("levelOfConcernOther", "(missing)");
				} else if ("0".equals(map.get("levelOfConcernCount0to3")) && "0".equals(map.get("levelOfConcernCount4to5"))
						&& "0".equals(map.get("levelOfConcernCount6to7")) && "0".equals(map.get("levelOfConcernCount8to10"))) {
					map.put("levelOfConcernOther", map.get("levelOfConcern"));
				}
				if (StringUtils.isBlank(map.get("dataEntryComplete"))) {
					map.put("dataEntryComplete", "(missing)");
				} else {
					map.put("dataEntryComplete", map.get("dataEntryComplete"));
				}
			}
		}

		return results;
	}

	@Override
	public Collection<Map<String, String>> export(ExportType exportType) {
		if (org == null) {
			return Lists.newArrayList();
		}

		Collection<Map<String, String>> results = Lists.newArrayList();

		if (org.getOrgTypeName().equals("School")) {
			Map<String, String> orgSummary = Iterables.getFirst(
					dao.retrieveSurveySummaryForOrg(snapshotWindow.getSnapshotWindowId(), org.getOrgId(), question)
							.getRows(), null);

			if (orgSummary == null || orgSummary.isEmpty()) {
				return Lists.newArrayList();
			}
			results.add(orgSummary);
		} else {
			QueryResult<Map<String, String>> result = dao.retrieveSurveySummaryForChildOrgs(
					snapshotWindow.getSnapshotWindowId(), org.getOrgId(), question, true, 0, 0);
			results = result.getRows();
		}

		if (org.getOrgTypeName().equals("School") || org.getOrgTypeName().equals("District")) {
			for (Map<String, String> map : results) {
				if (StringUtils.isBlank(map.get("levelOfConcern"))) {
					map.put("levelOfConcernCount0to3", "(missing)");
					map.put("levelOfConcernCount4to5", "(missing)");
					map.put("levelOfConcernCount6to7", "(missing)");
					map.put("levelOfConcernCount8to10", "(missing)");
					map.put("levelOfConcernOther", "(missing)");
				} else if ("0".equals(map.get("levelOfConcernCount0to3")) && "0".equals(map.get("levelOfConcernCount4to5"))
						&& "0".equals(map.get("levelOfConcernCount6to7")) && "0".equals(map.get("levelOfConcernCount8to10"))) {
					map.put("levelOfConcernOther", map.get("levelOfConcern"));
				}
				if (StringUtils.isBlank(map.get("dataEntryComplete"))) {
					map.put("dataEntryComplete", "(missing)");
				} else {
					map.put("dataEntryComplete", map.get("dataEntryComplete"));
				}
				if (exportType.equals(ExportType.pdf)) {
					formatTBDandMissingPdf("levelOfConcernCount0to3", "levelOfConcernCount0to3", map);
					formatTBDandMissingPdf("levelOfConcernCount4to5", "levelOfConcernCount4to5", map);
					formatTBDandMissingPdf("levelOfConcernCount6to7", "levelOfConcernCount6to7", map);
					formatTBDandMissingPdf("levelOfConcernCount8to10", "levelOfConcernCount8to10", map);
					formatTBDandMissingPdf("levelOfConcernAverageResponse", "levelOfConcernAverageResponse", map);
					formatTBDandMissingPdf("levelOfConcernOther", "levelOfConcernOther", map);
				}
				if (exportType.equals(ExportType.csv) && org.getOrgTypeName().equals("District")) {
					if (map.get("schoolType") != null) {
						map.put("schoolType", SCHOOL_TYPE.get(map.get("schoolType")) == null ? map.get("schoolType")
								: SCHOOL_TYPE.get(map.get("schoolType")));
					}
				}
			}
		}

		return results;
	}
}
