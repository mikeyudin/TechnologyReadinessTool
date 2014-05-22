package net.techreadiness.plugin.action.reports;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProviderImpl;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public abstract class ReportItemProviderImpl extends DataGridItemProviderImpl<Map<String, String>> implements
		ReportItemProvider {

	protected Org org;
	protected Scope consortium;
	protected String question;
	protected SnapshotWindow snapshotWindow;
	protected Collection<SnapshotWindow> snapshotWindows;
	protected MinimumRecommendedFlag minimumRecommendedFlag;
	protected final ImmutableMap<String, String> SCHOOL_TYPE = new ImmutableMap.Builder<String, String>()
			.put("public", "Public School").put("private", "Private School").put("charter", "Charter School")
			.put("other", "Other Testing Location").build();

	@Inject
	private ReportsService reportsService;

	public SnapshotWindow getSnapshotWindow() {
		return snapshotWindow;
	}

	@Override
	public Collection<Map<String, String>> getPage(DataGrid<Map<String, String>> grid) {
		if (org == null) {
			setTotalNumberOfItems(0);
			return Lists.newArrayList();
		}

		if (consortium == null) {
			throw new IllegalStateException("No Data to Display.  Please ensure the appropriate consortium is selected.");
		}

		if (org.getOrgTypeName().equals("School")) {
			QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForOrg(
					snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag);

			setTotalNumberOfItems(result.getTotalRowCount());

			if (result.getTotalRowCount() == 0) {
				return Lists.newArrayList();
			}
			return result.getRows();
		}

		QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForChildOrgs(
				snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag, false, grid.getFirstResult(),
				grid.getPageSize());
		setTotalNumberOfItems(result.getTotalRowCount());

		return result.getRows();
	}

	@Override
	public Collection<Map<String, String>> export(ExportType exportType) {
		if (org == null) {
			return Lists.newArrayList();
		}

		if (org.getOrgTypeName().equals("School")) {
			QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForOrg(
					snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag);

			if (result.getTotalRowCount() == 0) {
				return Lists.newArrayList();
			}
			return result.getRows();
		}

		QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForChildOrgs(
				snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag, true, 0, 0);
		Collection<Map<String, String>> results = result.getRows();
		if (exportType.equals(ExportType.csv) && org.getOrgTypeName().equals("District")) {
			for (Map<String, String> map : results) {
				if (map.get("schoolType") != null) {
					map.put("schoolType", SCHOOL_TYPE.get(map.get("schoolType")) == null ? map.get("schoolType")
							: SCHOOL_TYPE.get(map.get("schoolType")));
				}
			}
		}

		return results;
	}

	@Override
	public Collection<Map<String, String>> exportAllSchoolsDetail(ExportType exportType) {
		if (org == null) {
			return Lists.newArrayList();
		}

		QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForDescendantOrgs(
				snapshotWindow.getSnapshotWindowId(), org.getOrgId(), minimumRecommendedFlag, true, 0, 0, 2);
		Collection<Map<String, String>> results = result.getRows();
		for (Map<String, String> map : results) {
			if (exportType.equals(ExportType.csv) && org.getOrgTypeName().equals("State")) {
				if (map.get("schoolType") != null) {
					map.put("schoolType", SCHOOL_TYPE.get(map.get("schoolType")) == null ? map.get("schoolType")
							: SCHOOL_TYPE.get(map.get("schoolType")));
				}
			}

			String percent = map.get("testTakerPercentTestable");
			if (NumberUtils.toInt(map.get("deviceTbdCount")) > 0) {
				percent += " TBD";
			}
			map.put("testTakerPercentTestable", percent);

			String percent2 = map.get("devicePassingPercent") == null ? "" : map.get("devicePassingPercent").toUpperCase();
			if (NumberUtils.toInt(map.get("deviceTbdCount")) > 0) {
				percent2 += " TBD";
			}
			map.put("devicePassingPercent", percent2);
		}

		return results;
	}

	@Override
	public void setSnapshotWindow(SnapshotWindow snapshotWindow) {
		this.snapshotWindow = snapshotWindow;
	}

	@Override
	public void setSnapshotWindows(Collection<SnapshotWindow> snapshotWindows) {
		this.snapshotWindows = snapshotWindows;
	}

	@Override
	public void setOrg(Org org) {
		this.org = org;
	}

	@Override
	public void setConsortium(Scope consortiumScope) {
		consortium = consortiumScope;
	}

	@Override
	public void setMinimumRecommendedFlag(MinimumRecommendedFlag flag) {
		minimumRecommendedFlag = flag;
	}

	@Override
	public void setQuestion(String questionKey) {
		question = questionKey;
	}

	public String getFullName(String name, String code) {
		StringBuilder sb = new StringBuilder();
		if (!"".equals(name)) {
			sb.append(name);
		}
		if (!"".equals(code)) {
			if (sb.length() > 0) {
				sb.append(" (").append(code).append(")");
			} else {
				sb.append(code);
			}
		}
		return sb.toString();
	}

	protected void formatMissing(String complianceKey, String dataKey, Map<String, String> data) {
		if (data.get(complianceKey) != null && data.get(complianceKey).equals("missing")) {
			data.put(dataKey, "(missing)");
		}
	}

	protected void formatTBD(String complianceKey, String dataKey, Map<String, String> data) {
		if (data.get(complianceKey) != null && data.get(complianceKey).equals("TBD")) {
			data.put(dataKey, "<span style=\"color: #9a9a9a\">TBD</span>");
		}
	}

	protected void formatTBDandMissing(String complianceKey, String dataKey, Map<String, String> data) {
		StringBuilder sb = new StringBuilder();
		boolean showSpace;
		sb.append(data.get(dataKey) == null ? "" : data.get(dataKey).equals("missing") ? "(missing)" : data.get(dataKey));

		if (data.get(complianceKey) != null && data.get(complianceKey).equals("TBD")) {
			showSpace = sb.length() != 0;
			sb.append(showSpace ? " (" : "").append("TBD").append(showSpace ? ")" : "");
		}

		data.put(dataKey, sb.toString());
	}

	protected void formatTBDandMissingPdf(String complianceKey, String dataKey, Map<String, String> data) {
		StringBuilder sb = new StringBuilder();
		boolean showSpace;
		if (data.get(dataKey) != null && data.get(complianceKey) != null
				&& (data.get(dataKey).equals("missing") || data.get(complianceKey).equals("no"))) {
			sb.append("<span style=\"color: red\">");
			sb.append(data.get(dataKey).equals("missing") ? "(missing)" : data.get(dataKey));
			sb.append("</span>");
		} else if (data.get(dataKey) != null) {
			sb.append(data.get(dataKey));
		}

		if (data.get(complianceKey) != null && data.get(complianceKey).equals("TBD")) {
			showSpace = sb.length() != 0;
			sb.append("<span style=\"color: #9a9a9a\">").append(showSpace ? "&nbsp;" : "").append("TBD</span>");
		}

		data.put(dataKey, sb.toString());
	}
}
