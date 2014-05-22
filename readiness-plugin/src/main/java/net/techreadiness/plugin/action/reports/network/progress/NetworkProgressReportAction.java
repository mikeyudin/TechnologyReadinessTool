package net.techreadiness.plugin.action.reports.network.progress;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.action.reports.GeoChartBean;
import net.techreadiness.plugin.action.reports.ReportAction;
import net.techreadiness.plugin.action.reports.ReportExport;
import net.techreadiness.plugin.action.reports.ReportExportCsv;
import net.techreadiness.plugin.action.reports.ReportExportPdf;
import net.techreadiness.plugin.action.reports.ReportItemProvider;
import net.techreadiness.plugin.action.reports.ReportItemProvider.ExportType;
import net.techreadiness.plugin.action.task.snapshot.SnapshotComparator;
import net.techreadiness.plugin.service.SnapshotWindowService;
import net.techreadiness.plugin.service.object.SnapshotWindow;
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

import com.google.common.collect.Lists;

public class NetworkProgressReportAction extends ReportAction {
	private static final long serialVersionUID = 1L;

	private String orgCode;
	private String stateCode;
	private String stateName;

	private GeoChartBean geoChart;
	private List<SnapshotWindow> snapshots;
	private Org currentOrg;
	private boolean dualConsortium;

	@Inject
	private SnapshotWindowService snapshotService;
	@Inject
	private ReportsService reportService;
	@Inject
	@Qualifier("ReportProgressItemProvider")
	private ReportItemProvider reportItemProvider;

	@ConversationScoped(value = "networkProgressDataGridState")
	private DataGridState<?> reportGrid;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_PROGESS_NETWORK_RPT })
	@Action(value = "networkProgress", results = { @Result(name = "report", location = "/net/techreadiness/plugin/action/reports/network/progress/report.jsp") })
	public String networkProgress() throws Exception {
		getData();
		buildBreadcrumbs(currentOrg, consortium, "networkProgress");
		return "report";
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
		if (session.get("prevOrgDevice") == null || !currentOrg.getId().equals(session.get("prevOrgDevice"))) {
			reportGrid.setPage(1);
			reportGrid.setPageSize(10);
		}
		session.put("prevOrgDevice", currentOrg.getId());

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

		snapshots = snapshotService.findActiveSnapshots(context, consortiumScope.getScopeId());
		List<Long> snapshotWindowIds = Lists.newArrayList();

		for (SnapshotWindow snapshot : snapshots) {
			snapshotWindowIds.add(snapshot.getSnapshotWindowId());
		}
		Collections.sort(snapshots, new SnapshotComparator());
		reportItemProvider.setSnapshotWindows(snapshots);
		reportItemProvider.setSnapshotWindow(getCurrentSnapshotWindow());
		reportItemProvider.setOrg(currentOrg);
		reportItemProvider.setMinimumRecommendedFlag(getMinimumRecommendedFlag());

		if (currentOrg.getOrgTypeName().equals("Readiness")) {
			// query the map data
			QueryResult<Map<String, String>> result = reportService.retrieveProgressDataForChildOrgs(snapshotWindowIds,
					currentOrg.getOrgId(), getMinimumRecommendedFlag(), true, null, null);
			// put state data into geochart
			geoChart = new GeoChartBean();
			geoChart.addColor("#463de4");
			geoChart.setHoverLabel("Consortium");
			geoChart.setWidth(285);
			geoChart.setHeight(175);

			for (Map<String, String> data : result.getRows()) {
				geoChart.addBooleanData(data.get("orgCode"),
						StringUtils.remove(data.get("orgName"), "(" + data.get("localOrgCode") + ")"),
						data.get("localOrgCode"), consortium.toString());
			}

		} else if (currentOrg.getOrgTypeName().equals("State")) {
			// set state code and name (needed for image on page)
			stateCode = currentOrg.getLocalCode().toUpperCase();
			stateName = currentOrg.getName();
		} else if (currentOrg.getOrgTypeName().equals("District")) {
			Org state = orgService.getById(context, currentOrg.getParentOrgId());
			// set state code and name (needed for image on page)
			stateCode = state.getLocalCode().toUpperCase();
			stateName = state.getName();
		} else if (currentOrg.getOrgTypeName().equals("School")) {
			Org district = orgService.getById(context, currentOrg.getParentOrgId());
			Org state = orgService.getById(context, district.getParentOrgId());
			// set state code and name (needed for image on page)
			stateCode = state.getLocalCode().toUpperCase();
			stateName = state.getName();
		}
	}

	@Override
	protected ReportExport getReportExport(ExportType type) throws Exception {
		// populate reportItemProvider
		setFileName("NetworkProgress");
		getData();
		List<String> columnLabels = null, columnKeys = null;
		List<Boolean> useConditionalStyles = null;
		columnLabels = Lists.newArrayList("Organization", "Org Code");
		columnKeys = Lists.newArrayList("orgName", "orgCode");
		useConditionalStyles = Lists.newArrayList(false, false);

		for (SnapshotWindow snapshot : snapshots) {
			columnLabels.add(snapshot.getName().equals("default") ? "Current Window" : snapshot.getName());
			columnKeys.add("networkPercentStudentsTestable_" + snapshot.getId().toString());
			useConditionalStyles.add(true);
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
				subtitle.append("<b>")
						.append(currentOrg.getOrgTypeName().equals("Readiness") ? consortium.toString() : currentOrg
								.getName()).append(" - ");
				subtitle.append(
						"% Schools having Sufficient Infrastructure to Carry the Data Traffic for this Assessment, based on ")
						.append(requirements.toString()).append(" Requirements");

			} else {
				subtitle.append("<b>")
						.append(currentOrg.getOrgTypeName().equals("Readiness") ? consortium.toString() : currentOrg
								.getName()).append(" - ");
				subtitle.append("% of Students can be Tested in the Window, based on ").append(requirements.toString())
						.append(" Requirements");
			}

			ReportExportPdf pdf = new ReportExportPdf.Builder(columnLabels, columnKeys, useConditionalStyles,
					reportItemProvider.export(type)).title("Progress Report - Network Indicators")
					.subtitle(subtitle.toString()).consortium(consortium.toString())
					.aggregationMessage(getAsOfDate() + " -  Reports are updated hourly").build();
			pdf.setLegendTitle(getLegendTitle());
			if (currentOrg.getOrgTypeName().equals("Readiness")) {
				pdf.setTemplate("reports/wrapper-progress-map.jrxml");
				pdf.setMapUrl(geoChart.getUrl());
			} else {
				pdf.setTemplate("reports/wrapper-progress.jrxml");
			}
			return pdf;
		}
		}

		return null;
	}

	@Override
	protected ReportExport getAllSchoolsReportExport(ExportType type) throws Exception {
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

	public String getLegendTitle() {
		return "Percent Coverage";
	}

	public List<SnapshotWindow> getSnapshots() {
		return snapshots;
	}

	public Org getCurrentOrg() {
		return currentOrg;
	}

	public String getCurrentOrgName() {
		StringBuilder sb = new StringBuilder();
		if (currentOrg == null) {
			return "";
		}
		if (!"".equals(currentOrg.getName())) {
			sb.append(currentOrg.getName());
		}
		if (!"".equals(currentOrg.getLocalCode())) {
			if (sb.length() > 0) {
				sb.append(" (").append(currentOrg.getLocalCode()).append(")");
			} else {
				sb.append(currentOrg.getLocalCode());
			}
		}
		return sb.toString();
	}

}
