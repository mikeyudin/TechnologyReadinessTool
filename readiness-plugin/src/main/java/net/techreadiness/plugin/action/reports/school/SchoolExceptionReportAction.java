package net.techreadiness.plugin.action.reports.school;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.action.reports.ReportAction;
import net.techreadiness.plugin.action.reports.ReportExport;
import net.techreadiness.plugin.action.reports.ReportExportCsv;
import net.techreadiness.plugin.action.reports.ReportExportPdf;
import net.techreadiness.plugin.action.reports.ReportItemProvider;
import net.techreadiness.plugin.action.reports.ReportItemProvider.ExportType;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.collect.Lists;

public class SchoolExceptionReportAction extends ReportAction {
	private static List<String> standardColumnLabels, standardColumnKeys, fullDetailColumnLabels, fullDetailColumnKeys;
	{
		standardColumnLabels = Lists.newArrayList("Organization", "Org Code", "Parent Organization", "Completion Status",
				"Total Device Count", "Total # of Test Starts Needed Per School", "Device to Test-Taker Ratio",
				"Number of Unanswered Survey Questions");
		standardColumnKeys = Lists.newArrayList("orgName", "orgCode", "parentOrgName", "dataEntryComplete", "deviceCount",
				"testingTestStartCount", "deviceTestTakerRatio", "unansweredSurveyCount");

		fullDetailColumnLabels = Lists.newArrayList("Organization", "Org Code", "Parent Organization", "Completion Status",
				"Total Device Count", "Total # of Test Starts Needed Per School", "Device to Test-Taker Ratio",
				"Number of Unanswered Survey Questions", "Internal Network Bandwidth Utilization",
				"Max # of Simultaneous Test-Takers", "Wireless Access Points Count", "Number of Administrators",
				"Number of Technology Staff", "AdministratorTechnical Understanding", "Training of  Administrators",
				"Training of Technology Staff", "Testing Window", "Sessions per Day", "Internal Network Bandwidth",
				"Internet Bandwidth", "Technical Staff Technical Understanding", "Internet Bandwidth Utilization",
				"Classification of School", "Grade K Enrollment Count", "Grade 1 Enrollment Count",
				"Grade 2 Enrollment Count", "Grade 3 Enrollment Count", "Grade 4 Enrollment Count",
				"Grade 5 Enrollment Count", "Grade 6 Enrollment Count", "Grade 7 Enrollment Count",
				"Grade 8 Enrollment Count", "Grade 9 Enrollment Count", "Grade 10 Enrollment Count",
				"Grade 11 Enrollment Count", "Grade 12 Enrollment Count");
		fullDetailColumnKeys = Lists.newArrayList("orgName", "orgCode", "parentOrgName", "dataEntryComplete", "deviceCount",
				"testingTestStartCount", "deviceTestTakerRatio", "unansweredSurveyCount", "networkUtilization",
				"simultaneousTesters", "wirelessAccessPoints", "surveyAdminCount", "surveyTechstaffCount",
				"surveyAdminUnderstanding", "surveyAdminTraining", "surveyTechstaffTraining", "testingWindowLength",
				"sessionsPerDay", "networkSpeed", "internetSpeed", "surveyTechstaffUnderstanding", "internetUtilization",
				"schoolType", "enrollmentCountK", "enrollmentCount1", "enrollmentCount2", "enrollmentCount3",
				"enrollmentCount4", "enrollmentCount5", "enrollmentCount6", "enrollmentCount7", "enrollmentCount8",
				"enrollmentCount9", "enrollmentCount10", "enrollmentCount11", "enrollmentCount12");
	}

	private static final long serialVersionUID = 1L;

	private String orgCode;
	private String stateCode;
	private String stateName;

	// Not implementing geoChartBean for School Exception Report
	// private GeoChartBean geoChartBean;
	private boolean csvFullDetail = false;

	private String question;
	private Map<String, String> questions;
	private Org currentOrg;
	private boolean dualConsortium;
	private boolean importDeviceDataAllowed;
	private String asOfDate;

	@javax.inject.Inject
	@Qualifier("SchoolReportItemProvider")
	private ReportItemProvider reportItemProvider;

	@ConversationScoped(value = "schoolDataGridState")
	private DataGridState<?> reportGrid;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_SCHOOL_EXCEPTION_RPT })
	@Action(value = "schoolExceptionReport", results = { @Result(name = "map", location = "/net/techreadiness/plugin/action/reports/school/table.jsp") })
	public String schoolExceptionReport() throws Exception {
		getData();
		buildBreadcrumbs(currentOrg, consortium, "schoolExceptionReport");

		return "map";

	}

	public void getData() throws Exception {
		ServiceContext context = getServiceContext();
		if (questions == null) {
			questions = new LinkedHashMap<>();
			questions.put("notComplete", "Not Complete");
			questions.put("noDeviceEntry", "No Device Entry");
			questions.put("noActivity", "No Activity");
			questions.put("potentiallyMissed", "Potentially Missed Survey Questions");
			questions.put("potentiallyMissedDevice", "Potentially Missed Device Entry");
			questions.put("potentiallyComplete", "Potentially Complete");
			questions.put("potentiallyUnderReported", "Potentially Not Complete");
		}

		if (question == null && questions != null && !questions.isEmpty()) {
			question = questions.keySet().iterator().next();
		}

		// check if we are passed an org code
		// we should get this passed to us on every request outside the initial request(clicking on tab)

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
		if (session.get("prevOrgSchool") == null || !currentOrg.getId().equals(session.get("prevOrgSchool"))) {
			reportGrid.setPage(1);
			reportGrid.setPageSize(10);
		}
		session.put("prevOrgSchool", currentOrg.getId());

		// check if user has access to dual consortium
		dualConsortium = userService.hasPermission(context, CorePermissionCodes.CORE_CUSTOMER_CHANGE_GLOBAL_SCOPE);

		// check if user has access to import device data
		setImportDeviceDataAllowed(userService.hasPermission(context, CorePermissionCodes.READY_CUSTOMER_DEVICE_CREATE));

		// if true, check to make sure the current org has access to both consortiums
		List<net.techreadiness.service.object.OrgPart> orgParts = orgPartService.findOrgPartsForOrg(context,
				currentOrg.getOrgId());
		if (dualConsortium) {
			dualConsortium = orgParts != null && orgParts.size() > 1;
		}
		// if consortium is null, set consortium to the consortium in the service context
		if (consortium == null) {
			consortium = Consortium.getConsortium(context.getScope().getCode());
		}

		// get the consortiums scope object
		Scope consortiumScope = scopeService.getByScopePath(consortium.path);

		// set appropriate data for the report item provider
		reportItemProvider.setConsortium(consortiumScope);
		reportItemProvider.setSnapshotWindow(getCurrentSnapshotWindow());
		reportItemProvider.setOrg(currentOrg);
		reportItemProvider.setMinimumRecommendedFlag(getMinimumRecommendedFlag());
		reportItemProvider.setQuestion(question);

	}

	@Override
	protected ReportExport getReportExport(ExportType type) throws Exception {
		setFileName("SchoolExceptionReport");
		getData();

		switch (ExportType.valueOf(fileType)) {

		case csv: {
			ReportExportCsv csv = new ReportExportCsv();
			if (csvFullDetail) {
				csv.setColumnLabels(fullDetailColumnLabels);
				csv.setColumnKeys(fullDetailColumnKeys);
			} else {
				csv.setColumnLabels(standardColumnLabels);
				csv.setColumnKeys(standardColumnKeys);
			}
			csv.setData(reportItemProvider.export(type));
			return csv;
		}

		case pdf: {
			ReportExportPdf pdf = new ReportExportPdf.Builder(standardColumnLabels, standardColumnKeys,
					reportItemProvider.export(type))
					.title("School Exception Report")
					.subtitle(questions.get(question))
					.consortium(consortium.toString())
					.aggregationMessage(
							asOfDate == null ? "" : "Data as of " + asOfDate + "  -   Reports are updated hourly").build();

			return pdf;
			// pdf.setConditionalStyles(createConditionalStyles(pdf.getDetailStyle()));

			// UNFINISHED -
			// ASK TRACY ABOUT THE REPORT LAYOUT BEFORE FINISHING THIS
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

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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

	public Org getCurrentOrg() {
		return currentOrg;
	}

	public boolean isDualConsortium() {
		return dualConsortium;
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

	public boolean isCsvFullDetail() {
		return csvFullDetail;
	}

	public void setCsvFullDetail(boolean csvFullDetail) {
		this.csvFullDetail = csvFullDetail;
	}

	public boolean isImportDeviceDataAllowed() {
		return importDeviceDataAllowed;
	}

	public void setImportDeviceDataAllowed(boolean importDeviceDataAllowed) {
		this.importDeviceDataAllowed = importDeviceDataAllowed;
	}

}
