package net.techreadiness.plugin.action.reports.tester;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.reports.GeoChartBean;
import net.techreadiness.plugin.action.reports.ReportAction;
import net.techreadiness.plugin.action.reports.ReportBreadcrumb.ProgressReportLink;
import net.techreadiness.plugin.action.reports.ReportExport;
import net.techreadiness.plugin.action.reports.ReportExportCsv;
import net.techreadiness.plugin.action.reports.ReportExportPdf;
import net.techreadiness.plugin.action.reports.ReportItemProvider;
import net.techreadiness.plugin.action.reports.ReportItemProvider.ExportType;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.OrgPart;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class TesterAssessmentReportAction extends ReportAction {
	private static final long serialVersionUID = 1L;

	private String orgCode;
	private String stateCode;
	private String stateName;

	private GeoChartBean geoChart;
	private Map<String, String> orgSummary;
	private Org currentOrg;
	private boolean dualConsortium;

	@Inject
	private ReportsService reportsService;
	@Inject
	@Qualifier("TesterAssessmentItemProvider")
	private ReportItemProvider reportItemProvider;

	@ConversationScoped(value = "testerDataGridState")
	private DataGridState<?> reportGrid;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_DEVICE_TO_TEST_RPT })
	@Action(value = "testerAssessment", results = {
			@Result(name = "map", location = "/net/techreadiness/plugin/action/reports/tester/map.jsp"),
			@Result(name = "table", location = "/net/techreadiness/plugin/action/reports/tester/table.jsp"),
			@Result(name = "org", location = "/net/techreadiness/plugin/action/reports/tester/org.jsp"),
			@Result(name = "school", location = "/net/techreadiness/plugin/action/reports/tester/school.jsp") })
	public String testerAssessment() throws Exception {
		getData();
		if (!currentOrg.getOrgTypeName().equals("Readiness")) {
			buildBreadcrumbs(currentOrg, consortium, "testerAssessment");
		}
		if (snapshotName != null && !"".equals(snapshotName)) {
			addProgressReportLink(ProgressReportLink.testTakerProgress);
		}
		return getReturn();
	}

	private void getData() throws Exception {
		ServiceContext context = getServiceContext();

		// check if we are passed an org code
		// we should get this passed to us on every request outside the initial request (clicking on tab)
		if (orgCode == null || orgCode.equals("")) {
			Org org = context.getOrg();
			orgCode = org.getCode();
		}

		currentOrg = orgService.getByCode(context, orgCode);
		if (currentOrg == null || !userService.hasAccessToOrg(context, context.getUserId(), currentOrg.getOrgId())) {
			if (currentOrg == null) {
				throw new Exception("Org could not be found: " + orgCode);
			}
			throw new Exception("User(" + context.getUserId() + ") doesn't have access to org (" + currentOrg.getOrgId()
					+ ")");
		}

		// get previous org off the session, if different, reset paging
		Map<String, Object> session = getSession();
		if (session.get("prevOrgTester") == null || !currentOrg.getId().equals(session.get("prevOrgTester"))) {
			reportGrid.setPage(1);
			reportGrid.setPageSize(10);
		}
		session.put("prevOrgTester", currentOrg.getId());

		// check if user has access to dual consortium
		dualConsortium = userService.hasPermission(context, CorePermissionCodes.CORE_CUSTOMER_CHANGE_GLOBAL_SCOPE);
		// if true, check to make sure the current org has access to both consortiums
		List<OrgPart> orgParts = orgPartService.findOrgPartsForOrg(context, currentOrg.getOrgId());
		if (dualConsortium) {
			dualConsortium = orgParts != null && orgParts.size() > 1;
		}
		// if consortium is null, set consortium to the consortium in the service context
		if (consortium == null) {
			consortium = Consortium.getConsortium(context.getScope().getCode());
		}

		// get the consortiums scope object
		Scope consortiumScope = scopeService.getByScopePath(consortium.path);

		// set the appropriate data for the report item provider
		reportItemProvider.setConsortium(consortiumScope);
		reportItemProvider.setSnapshotWindow(getSnapshotWindow());
		reportItemProvider.setOrg(currentOrg);
		reportItemProvider.setMinimumRecommendedFlag(getMinimumRecommendedFlag());

		Collection<Map<String, String>> result = reportsService.retrieveSummaryForOrg(
				getSnapshotWindow().getSnapshotWindowId(), currentOrg.getOrgId(), getMinimumRecommendedFlag()).getRows();
		orgSummary = Iterables.getFirst(result, new HashMap<String, String>());

		if (currentOrg.getOrgTypeName().equals("Readiness")) {
			// query the map data
			Collection<Map<String, String>> states = reportsService.retrieveSummaryForChildOrgs(
					getSnapshotWindow().getSnapshotWindowId(), currentOrg.getOrgId(), getMinimumRecommendedFlag(), true, 0,
					0).getRows();
			// put state data into geochart
			geoChart = new GeoChartBean();
			geoChart.setHoverLabel("Students That Can Be Tested");
			if (viewBy != null && viewBy.equals(ViewBy.table)) {
				geoChart.setWidth(285);
				geoChart.setHeight(175);
			}

			for (Map<String, String> data : states) {
				geoChart.addPercentData(data.get("orgCode"),
						StringUtils.remove(data.get("orgName"), "(" + data.get("localOrgCode") + ")"),
						data.get("localOrgCode"), data.get("testTakerPercentStudentsTestable"));
			}
		} else if (currentOrg.getOrgTypeName().equals("State")) {
			// set state code and name (needed for image on page)
			stateCode = currentOrg.getLocalCode().toUpperCase();
			stateName = currentOrg.getName();
		} else if (currentOrg.getOrgTypeName().equals("District")) {
			Org state = orgService.getParentOrgOfType(getServiceContext(), currentOrg.getOrgId(), "state");
			// set state code and name (needed for image on page)
			stateCode = state.getLocalCode().toUpperCase();
			stateName = state.getName();
		} else if (currentOrg.getOrgTypeName().equals("School")) {
			Org state = orgService.getParentOrgOfType(getServiceContext(), currentOrg.getOrgId(), "state");
			// set state code and name (needed for image on page)
			stateCode = state.getLocalCode().toUpperCase();
			stateName = state.getName();
		}
	}

	@Override
	protected ReportExport getReportExport(ExportType type) throws Exception {
		// populate reportItemProvider
		setFileName("TestTakerIndicators");
		getData();
		List<String> columnLabels, columnKeys;
		if (currentOrg.getOrgTypeName().equals("District") || currentOrg.getOrgTypeName().equals("School")) {
			columnLabels = Lists.newArrayList("Organization", "Org Code", "Completion Status", "# Devices Meeting "
					+ requirements.toString() + " Requirements", "Enrollment Count of Tested Grades",
					"Total # Test Starts Needed per School", "Testing Window (Days)", "# Sessions per Day",
					"# Possible Test Starts With Existing Devices", "% of Students that can be Tested");
			columnKeys = Lists.newArrayList("orgName", "orgCode", "dataEntryComplete", "devicePassingCount",
					"testingStudentCount", "testingTestStartCount", "testingWindowLengthCalc", "sessionsPerDay",
					"testTakerPossibleTestCount", "testTakerPercentStudentsTestable");
		} else {

			columnLabels = Lists.newArrayList("Organization Name", "Organization Code", "# of Schools 0%-25%",
					"# of Schools 26%-50%", "# of Schools 51%-75%", "# of Schools 76%-100%", "Total # Test Starts Needed",
					"# Possible Test Starts With Existing Devices", "% of Students that can be Tested in the Window");
			columnKeys = Lists.newArrayList("orgName", "orgCode", "testTaker0To25", "testTaker26To50", "testTaker51To75",
					"testTaker76To100", "testingTestStartCount", "testTakerPossibleTestCount",
					"testTakerPercentStudentsTestable");

		}
		if (ExportType.csv.equals(type)) {
			columnLabels.add(0, "Parent Organization");
			columnKeys.add(0, "parentOrgName");
			if (currentOrg.getOrgTypeName().equals("District")) {
				columnLabels.add(1, "School Classification");
				columnKeys.add(1, "schoolType");
			}
		}

		switch (ExportType.valueOf(fileType)) {
		case csv: {
			ReportExportCsv csv = new ReportExportCsv();
			csv.setColumnLabels(columnLabels);
			csv.setColumnKeys(columnKeys);
			csv.setData(reportItemProvider.export(type));
			return csv;
		}
		case pdf: {
			StringBuilder subtitle = new StringBuilder();
			if (currentOrg.getOrgTypeName().equals("School")) {
				if (orgSummary.get("testTakerPercentStudentsTestable") == null
						|| orgSummary.get("testTakerPercentStudentsTestable").equals("TBD")) {
					subtitle.append("<b><span style=\"color: #9a9a9a\">TBD</span>&nbsp;&nbsp;&nbsp;");
					subtitle.append(
							currentOrg.getOrgTypeName().equals("Readiness") ? consortium.toString() : orgSummary
									.get("orgName")).append("</b> - ");
					subtitle.append(
							"It <u>cannot</u> be determined what % of Eligible Test-Takers can be Tested on Existing Devices, based on ")
							.append(getRequirements()).append(" Requirements");
				} else {
					if (orgSummary.get("testTakerPercentStudentsTestable").equals("(missing)")) {
						subtitle.append(
								currentOrg.getOrgTypeName().equals("Readiness") ? consortium.toString() : orgSummary
										.get("orgName")).append("</b> - ");
						subtitle.append(
								"It <u>cannot</u> be determined what % of Eligible Test-Takers can be Tested on Existing Devices, based on ")
								.append(getRequirements()).append(" Requirements");
					} else {
						if (orgSummary.get("testTakerPercentStudentsTestable").equalsIgnoreCase("(Not Applicable)")) {
							subtitle.append("<b>").append(orgSummary.get("orgName")).append("</b> - ");
							subtitle.append("There are no Eligible Test Takers");
						} else {
							subtitle.append("<b>")
									.append(currentOrg.getOrgTypeName().equals("Readiness") ? consortium.toString()
											: orgSummary.get("orgName")).append(" - ");
							subtitle.append(orgSummary.get("testTakerPercentStudentsTestable"))
									.append("</b> of Eligible Test-Takers can be Tested on Existing Devices, based on ")
									.append(requirements.toString()).append(" Requirements");
						}
					}
				}
			} else {
				if (orgSummary.get("testTakerPercentStudentsTestable") == null
						|| orgSummary.get("testTakerPercentStudentsTestable").equals("TBD")
						|| orgSummary.get("testTakerPercentStudentsTestable").equals("(missing)")) {
					// subtitle.append("<b><span style=\"color: #9a9a9a\">TBD</span>&nbsp;&nbsp;&nbsp;");
					subtitle.append(
							currentOrg.getOrgTypeName().equals("Readiness") ? consortium.toString() : orgSummary
									.get("orgName")).append("</b> - ");
					subtitle.append(
							"It <u>cannot</u> be determined what % of Eligible Test-Takers can be Tested on Existing Devices, based on ")
							.append(getRequirements()).append(" Requirements");
				} else {
					subtitle.append("<b>")
							.append(currentOrg.getOrgTypeName().equals("Readiness") ? consortium.toString() : orgSummary
									.get("orgName")).append(" - ");
					subtitle.append(orgSummary.get("testTakerPercentStudentsTestable"))
							.append("</b> of Eligible Test-Takers can be Tested on Existing Devices, based on ")
							.append(requirements.toString()).append(" Requirements");
				}
			}

			ReportExportPdf pdf = new ReportExportPdf.Builder(columnLabels, columnKeys, reportItemProvider.export(type))
					.title("Device to Test-Taker Indicators").subtitle(subtitle.toString())
					.consortium(consortium.toString())
					.aggregationMessage("Data as of " + orgSummary.get("createDate") + " - Reports are updated hourly")
					.build();
			pdf.setLegendTitle(getLegendTitle());
			if (!getReturn().equals("org") && !getReturn().equals("school")) {
				pdf.setMapUrl(geoChart.getUrl());
			}
			return pdf;
		}
		}

		return null;
	}

	@Override
	protected ReportExport getAllSchoolsReportExport(ExportType type) throws Exception {
		// populate reportItemProvider
		setFileName("AllSchoolsTestTakerIndicators");
		getData();
		List<String> columnLabels = null, columnKeys = null;

		columnLabels = Lists.newArrayList("Organization", "Org Code", "Completion Status", "# Devices Meeting "
				+ requirements.toString() + " Requirements", "Enrollment Count of Tested Grades",
				"Total # Test Starts Needed per School", "Testing Window (Days)", "# Sessions per Day",
				"# Possible Test Starts With Existing Devices", "% of Students that can be Tested");
		columnKeys = Lists.newArrayList("orgName", "orgCode", "dataEntryComplete", "devicePassingCount",
				"testingStudentCount", "testingTestStartCount", "testingWindowLengthCalc", "sessionsPerDay",
				"testTakerPossibleTestCount", "testTakerPercentStudentsTestable");

		if (ExportType.csv.equals(type)) {
			columnLabels.add(0, "Parent Organization");
			columnKeys.add(0, "parentOrgName");
			if (currentOrg.getOrgTypeName().equals("District") || currentOrg.getOrgTypeName().equals("State")) {
				columnLabels.add(1, "School Classification");
				columnKeys.add(1, "schoolType");
			}
		}

		switch (ExportType.valueOf(fileType)) {
		case csv: {
			ReportExportCsv csv = new ReportExportCsv();
			csv.setColumnLabels(columnLabels);
			csv.setColumnKeys(columnKeys);
			csv.setData(reportItemProvider.exportAllSchoolsDetail(type));
			return csv;
		}
		default:
			break;

		}

		return null;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String selectedOrgCode) {
		orgCode = selectedOrgCode;
	}

	public GeoChartBean getGeoChart() {
		return geoChart;
	}

	public void setGeoChart(GeoChartBean geoChart) {
		this.geoChart = geoChart;
	}

	private String getReturn() {
		return currentOrg != null && currentOrg.getOrgTypeName().equals("Readiness") ? getViewBy() : currentOrg
				.getOrgTypeName().equals("District") || currentOrg.getOrgTypeName().equals("School") ? "school" : "org";
	}

	public ReportItemProvider getReportItemProvider() {
		return reportItemProvider;
	}

	public DataGridState<?> getReportGrid() {
		return reportGrid;
	}

	public void setReportGrid(DataGridState<?> reportGrid) {
		this.reportGrid = reportGrid;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public boolean isDualConsortium() {
		return dualConsortium;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Map<String, String> getOrgSummary() {
		return orgSummary;
	}

	public String getLegendTitle() {
		return "Percent Coverage";
	}

	public Org getCurrentOrg() {
		return currentOrg;
	}

	public void setCurrentOrg(Org currentOrg) {
		this.currentOrg = currentOrg;
	}

}
