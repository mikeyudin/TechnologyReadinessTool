package net.techreadiness.plugin.action.reports.staff;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.reports.GeoChartBean;
import net.techreadiness.plugin.action.reports.ReportAction;
import net.techreadiness.plugin.action.reports.ReportExport;
import net.techreadiness.plugin.action.reports.ReportExportCsv;
import net.techreadiness.plugin.action.reports.ReportExportPdf;
import net.techreadiness.plugin.action.reports.ReportItemProvider;
import net.techreadiness.plugin.action.reports.ReportItemProvider.ExportType;
import net.techreadiness.plugin.action.reports.ReportPdfHighlightCondition;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.OrgPart;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Qualifier;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionalStyle;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class StaffReportAction extends ReportAction {

	private static final long serialVersionUID = 1L;

	private String orgCode;
	private String stateCode;
	private String stateName;

	private GeoChartBean geoChart;
	private String question;
	private Map<String, String> questions;
	private Org currentOrg;
	private boolean dualConsortium;
	private String reportAggDate;

	@Inject
	private ReportsService reportService;
	@Inject
	@Qualifier("StaffReportItemProvider")
	private ReportItemProvider reportItemProvider;

	@ConversationScoped(value = "staffDataGridState")
	private DataGridState<?> reportGrid;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_STAFF_PERSONNEL_RPT })
	@Action(value = "staffReport", results = {
			@Result(name = "map", location = "/net/techreadiness/plugin/action/reports/staff/map.jsp"),
			@Result(name = "table", location = "/net/techreadiness/plugin/action/reports/staff/table.jsp"),
			@Result(name = "org", location = "/net/techreadiness/plugin/action/reports/staff/org.jsp") })
	public String staffReport() throws Exception {
		getData();
		buildBreadcrumbs(currentOrg, consortium, "staffReport");
		return getReturn();
	}

	private void getData() throws Exception {
		ServiceContext context = getServiceContext();
		// query list of questions;
		if (questions == null) {
			questions = new LinkedHashMap<>();
			questions.put("admin_count", "Having a sufficient number of test administrators to support online testing.");
			questions.put("admin_understanding",
					"Test administrators having sufficient technical understanding to support online testing.");
			questions.put("admin_training", "Providing all appropriate training needed for test administrators.");
			questions.put("techstaff_count",
					"Having a sufficient number of technology support staff to support online testing.");
			questions.put("techstaff_understanding",
					"Technology support staff having sufficient technical understanding to support online testing.");
			questions.put("techstaff_training", "Providing all appropriate training needed for technology support staff.");
		}

		if (question == null && questions != null && !questions.isEmpty()) {
			question = questions.keySet().iterator().next();
		}

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
		if (session.get("prevOrgStaff") == null || !currentOrg.getId().equals(session.get("prevOrgStaff"))) {
			reportGrid.setPage(1);
		}
		session.put("prevOrgStaff", currentOrg.getId());

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
		reportItemProvider.setSnapshotWindow(getCurrentSnapshotWindow());
		reportItemProvider.setOrg(currentOrg);
		reportItemProvider.setMinimumRecommendedFlag(getMinimumRecommendedFlag());
		reportItemProvider.setQuestion(question);

		if (currentOrg.getOrgTypeName().equals("Readiness")) {

			// query the map data
			Collection<Map<String, String>> states = reportService.retrieveSurveySummaryForChildOrgs(
					getCurrentSnapshotWindow().getSnapshotWindowId(), currentOrg.getOrgId(), question, true, 0, 0).getRows();
			// put state data into geochart
			geoChart = new GeoChartBean();
			geoChart.setHoverLabel("Aggregated Average Level of Concern (Scale 0 -10, zero is no concern)");
			if (viewBy != null && viewBy.equals(ViewBy.table)) {
				geoChart.setWidth(285);
				geoChart.setHeight(175);
			}

			if (states != null) {
				for (Map<String, String> data : states) {
					reportAggDate = data.get("createDate");
					geoChart.addSurveyData(data.get("orgCode"),
							StringUtils.remove(data.get("orgName"), "(" + data.get("localOrgCode") + ")"),
							data.get("localOrgCode"), data.get("levelOfConcernAverageResponse"));
				}
			}
		} else if (currentOrg.getOrgTypeName().equals("State")) {

			// set state code and name (needed for image on page)
			stateCode = currentOrg.getLocalCode().toUpperCase();
			stateName = currentOrg.getName();

			// query for aggDate
			Collection<Map<String, String>> districts = reportService.retrieveSurveySummaryForChildOrgs(
					getCurrentSnapshotWindow().getSnapshotWindowId(), currentOrg.getOrgId(), question, true, 0, 0).getRows();
			if (districts != null && !districts.isEmpty()) {
				reportAggDate = Iterables.getFirst(districts, null).get("createDate");
			}

		} else if (currentOrg.getOrgTypeName().equals("District")) {
			Org state = orgService.getById(context, currentOrg.getParentOrgId());
			// set state code and name (needed for image on page)
			stateCode = state.getLocalCode().toUpperCase();
			stateName = state.getName();

			// query for aggDate
			Collection<Map<String, String>> schools = reportService.retrieveSurveySummaryForChildOrgs(
					getCurrentSnapshotWindow().getSnapshotWindowId(), currentOrg.getOrgId(), question, true, 0, 0).getRows();
			if (schools != null && !schools.isEmpty()) {
				reportAggDate = Iterables.getFirst(schools, null).get("createDate");
			}
		} else if (currentOrg.getOrgTypeName().equals("School")) {
			Org state = orgService.getParentOrgOfType(getServiceContext(), currentOrg.getOrgId(), "state");
			// set state code and name (needed for image on page)
			stateCode = state.getLocalCode().toUpperCase();
			stateName = state.getName();

			// query for aggDate
			Collection<Map<String, String>> result = reportService.retrieveSurveySummaryForOrg(
					getCurrentSnapshotWindow().getSnapshotWindowId(), currentOrg.getOrgId(), question).getRows();

			Map<String, String> school = Iterables.getFirst(result, new HashMap<String, String>());

			if (school != null && !school.isEmpty()) {
				reportAggDate = school.get("createDate");
			}
		}
	}

	@Override
	protected ReportExport getReportExport(ExportType type) throws Exception {
		// populate reportItemProvider
		setFileName("StaffReport");
		getData();
		List<String> columnLabels, columnKeys;
		columnLabels = Lists.newArrayList("Parent Organization", "Organization", "Org Code", "Level of Concern 0-3",
				"Level of Concern 4-5", "Level of Concern 6-7", "Level of Concern 8-10", "Aggregated Average Response");
		columnKeys = Lists.newArrayList("parentOrgName", "orgName", "orgCode", "levelOfConcernCount0to3",
				"levelOfConcernCount4to5", "levelOfConcernCount6to7", "levelOfConcernCount8to10",
				"levelOfConcernAverageResponse");
		// remove last column if its a school and replace with Other
		if (currentOrg.getOrgTypeName().equals("School")) {
			columnLabels.set(columnLabels.size() - 1, "Other");
			columnKeys.set(columnKeys.size() - 1, "levelOfConcernOther");
		}

		if (currentOrg.getOrgTypeName().equals("District") || currentOrg.getOrgTypeName().equals("School")) {
			columnLabels = Lists
					.newArrayList("Parent Organization", "Organization", "Org Code", "Completion Status",
							"Level of Concern 0-3", "Level of Concern 4-5", "Level of Concern 6-7", "Level of Concern 8-10",
							"Other");
			columnKeys = Lists.newArrayList("parentOrgName", "orgName", "orgCode", "dataEntryComplete",
					"levelOfConcernCount0to3", "levelOfConcernCount4to5", "levelOfConcernCount6to7",
					"levelOfConcernCount8to10", "levelOfConcernOther");
		}

		if (ExportType.csv.equals(type) && currentOrg.getOrgTypeName().equals("District")) {
			columnLabels.add(1, "School Classification");
			columnKeys.add(1, "schoolType");
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
			ReportExportPdf pdf = new ReportExportPdf.Builder(columnLabels, columnKeys, reportItemProvider.export(type))
					.title("Staff & Personnel Indicator Report")
					.subtitle(questions.get(question))
					.consortium(consortium.toString())
					.aggregationMessage(
							reportAggDate == null ? "" : "Data as of " + reportAggDate + " - Reports are updated hourly")
					.build();
			pdf.setConditionalStyles(createConditionalStyles(pdf.getDetailStyle()));
			pdf.setSuppressConditionalStyles(currentOrg.getOrgTypeName().equals("District")
					|| currentOrg.getOrgTypeName().equals("School"));
			pdf.setTemplate("reports/wrapper-survey.jrxml");
			if (!getReturn().equals("org")) {
				pdf.setMapUrl(geoChart.getUrl());

				pdf.setTemplate("reports/wrapper-survey-map.jrxml");
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

	private static List<ConditionalStyle> createConditionalStyles(Style baseStyle) throws IllegalAccessException,
			InstantiationException, InvocationTargetException, NoSuchMethodException {
		Style reportLevel1 = (Style) BeanUtils.cloneBean(baseStyle);
		reportLevel1.setBackgroundColor(new Color(184, 31, 75));
		reportLevel1.setTextColor(Color.white);
		reportLevel1.setTransparent(false);
		Style reportLevel2 = (Style) BeanUtils.cloneBean(baseStyle);
		reportLevel2.setBackgroundColor(new Color(246, 133, 31));
		reportLevel2.setTransparent(false);
		Style reportLevel3 = (Style) BeanUtils.cloneBean(baseStyle);
		reportLevel3.setBackgroundColor(new Color(252, 185, 19));
		reportLevel3.setTransparent(false);
		Style reportLevel4 = (Style) BeanUtils.cloneBean(baseStyle);
		reportLevel4.setBackgroundColor(new Color(0, 161, 94));
		reportLevel4.setTransparent(false);

		ReportPdfHighlightCondition status1 = new ReportPdfHighlightCondition(0, 3.00);
		ReportPdfHighlightCondition status2 = new ReportPdfHighlightCondition(3.01, 5.00);
		ReportPdfHighlightCondition status3 = new ReportPdfHighlightCondition(5.01, 7.00);
		ReportPdfHighlightCondition status4 = new ReportPdfHighlightCondition(7.01, 10);

		List<ConditionalStyle> conditionalStyles = Lists.newArrayList();
		conditionalStyles.add(new ConditionalStyle(status1, reportLevel4));
		conditionalStyles.add(new ConditionalStyle(status2, reportLevel3));
		conditionalStyles.add(new ConditionalStyle(status3, reportLevel2));
		conditionalStyles.add(new ConditionalStyle(status4, reportLevel1));

		return conditionalStyles;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Map<String, String> getQuestions() {
		return questions;
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
		return currentOrg != null && currentOrg.getOrgTypeName().equals("Readiness") ? getViewBy() : "org";
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

	public Org getCurrentOrg() {
		return currentOrg;
	}

	public String getReportAggDate() {
		return reportAggDate;
	}

	public String getLegendTitle() {
		return "Level of Concern";
	}

}
