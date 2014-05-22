package net.techreadiness.plugin.action.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

import net.techreadiness.plugin.action.reports.ReportBreadcrumb.ProgressReportLink;
import net.techreadiness.plugin.action.reports.ReportItemProvider.ExportType;
import net.techreadiness.plugin.service.SnapshotWindowService;
import net.techreadiness.plugin.service.object.SnapshotWindow;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.service.OrgPartService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Scope;
import net.techreadiness.ui.BaseAction;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Lists;

public abstract class ReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	public enum Consortium {
		SBAC("smart", "/readiness/smart"), PARCC("parcc", "/readiness/parcc");

		public final String code, path;

		Consortium(final String code, final String path) {
			this.code = code;
			this.path = path;
		}

		public static Consortium getConsortium(String code) {
			for (Consortium corsortium : Consortium.values()) {
				if (corsortium.code.equals(code)) {
					return corsortium;
				}
			}
			return null;
		}
	}

	protected enum ViewBy {
		table, map;
	}

	public enum Requirements {
		Minimum, Recommended;
	}

	@Inject
	protected OrganizationService orgService;

	@Inject
	protected OrgPartService orgPartService;

	@Inject
	protected ScopeService scopeService;

	@Inject
	SnapshotWindowService snapshotWindowService;

	@Inject
	private ReportsService reportService;

	// List of minimum and recommended requirements set by the consortium
	private List<Map<String, String>> minRecReq;

	protected List<ReportBreadcrumb> breadcrumbs;

	protected ViewBy viewBy;
	protected Consortium consortium;
	protected Requirements requirements;
	protected String snapshotName;
	protected long snapshotScopeId;

	protected String fileName;
	protected String fileType;
	protected String contentType;
	private InputStream inputStream;
	private String reportAggDate;
	private String year;
	private String monthZ;
	private String date;
	private int hourInt;
	private String hour;
	private String ampm;
	private String minutes;
	private String mm;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a ");

	protected abstract ReportExport getReportExport(ExportType type) throws Exception;

	protected abstract ReportExport getAllSchoolsReportExport(ExportType type) throws Exception;

	@Action(value = "download", results = { @Result(name = "success", type = "stream", params = { "inputName",
			"inputStream", "contentType", "${contentType}", "contentDisposition",
			"attachment;filename=\"${fileName}.${fileType}\"", "bufferSize", "1024" }) })
	public String download() throws Exception {
		// check input types for validity
		try {
			switch (ExportType.valueOf(fileType)) {
			case csv:
				contentType = "text/csv";
				break;
			case pdf:
				contentType = "application/pdf";
				break;
			}
		} catch (Exception e) {
			throw new Exception("File Type not implemented.");
		}

		ReportExport export = getReportExport(ExportType.valueOf(fileType));
		inputStream = new ByteArrayInputStream(export.getReport());
		return SUCCESS;
	}

	@Action(value = "downloadAllSchools", results = { @Result(name = "success", type = "stream", params = { "inputName",
			"inputStream", "contentType", "${contentType}", "contentDisposition",
			"attachment;filename=\"${fileName}.${fileType}\"", "bufferSize", "1024" }) })
	public String downloadAllSchools() throws Exception {
		// check input types for validity
		try {
			switch (ExportType.valueOf(fileType)) {
			case csv:
				contentType = "text/csv";
				break;
			default:
				break;
			}
		} catch (Exception e) {
			throw new Exception("File Type not implemented.");
		}

		ReportExport export = getAllSchoolsReportExport(ExportType.valueOf(fileType));
		inputStream = new ByteArrayInputStream(export.getReport());
		return SUCCESS;
	}

	@Action(value = "minRequirements", results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/reports/minRequirements.jsp") })
	public String minRequirements() throws ServiceException {
		// select consortium (based on past selection)
		if (consortium == null) {
			consortium = Consortium.SBAC;
		}
		// get the consortiums scope object
		queryRequirements(getSnapshotWindow(snapshotName, snapshotScopeId));
		return SUCCESS;
	}

	@Action(value = "exceptionTypes", results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/reports/exceptionTypes.jsp") })
	public String exceptionTypes() throws ServiceException {
		if (consortium == null) {
			consortium = Consortium.SBAC;
		}
		return SUCCESS;
	}

	private SnapshotWindow currentSnapshotWindow = null;

	private Scope consortiumScope;

	protected SnapshotWindow getCurrentSnapshotWindow() {
		if (currentSnapshotWindow == null) {
			consortiumScope = scopeService.getByScopePath(consortium.path);
			currentSnapshotWindow = snapshotWindowService.getByScopeIdAndName(getServiceContext(),
					consortiumScope.getScopeId(), ReportsService.DEFAULT_SNAPSHOT_WINDOW);
		}
		return currentSnapshotWindow;
	}

	public SnapshotWindow getSnapshotWindow() {
		if (snapshotName == null || snapshotName.equals("")) {
			return getCurrentSnapshotWindow();
		}
		return getSnapshotWindow(snapshotName, snapshotScopeId);
	}

	protected SnapshotWindow getSnapshotWindow(String snapshotName, long snapshotScopeId) {
		if (snapshotName == null || snapshotName.equals("")) {
			return getCurrentSnapshotWindow();
		}
		return snapshotWindowService.getByScopeIdAndName(getServiceContext(), snapshotScopeId, snapshotName);
	}

	protected void queryRequirements() {
		minRecReq = reportService.retrieveMinimumRecommendedValues(getCurrentSnapshotWindow().getSnapshotWindowId());
	}

	protected void queryRequirements(SnapshotWindow snapshotWindow) {
		minRecReq = reportService.retrieveMinimumRecommendedValues(snapshotWindow.getSnapshotWindowId());
	}

	@Action(value = "TBD", results = { @Result(name = "success", location = "/net/techreadiness/plugin/action/reports/tbd.jsp") })
	public String tbd() throws ServiceException {
		return SUCCESS;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getViewBy() {
		return viewBy == null ? ViewBy.map.toString() : viewBy.toString();
	}

	public void setViewBy(String viewBy) {
		this.viewBy = ViewBy.valueOf(viewBy);
	}

	public String getRequirements() {
		return requirements == null ? Requirements.Minimum.toString() : requirements.toString();
	}

	public void setRequirements(String requirements) {
		this.requirements = Requirements.valueOf(requirements);
	}

	public String getConsortium() {
		return consortium == null ? Consortium.SBAC.toString() : consortium.toString();
	}

	public void setConsortium(String consortium) {
		this.consortium = Consortium.valueOf(consortium);
	}

	public List<ReportBreadcrumb> getBreadcrumbs() {
		return breadcrumbs;
	}

	public void setBreadcrumbs(List<ReportBreadcrumb> breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
	}

	public List<Map<String, String>> getMinRecReq() {
		return minRecReq;
	}

	public String getSnapshotName() {
		return snapshotName;
	}

	public void setSnapshotName(String snapshotName) {
		this.snapshotName = snapshotName;
	}

	public long getSnapshotScopeId() {
		return snapshotScopeId;
	}

	public void setSnapshotScopeId(long snapshotScopeId) {
		this.snapshotScopeId = snapshotScopeId;
	}

	protected MinimumRecommendedFlag getMinimumRecommendedFlag() {
		if (requirements == null) {
			return MinimumRecommendedFlag.MINIMUM;
		}
		switch (requirements) {
		case Minimum:
			return MinimumRecommendedFlag.MINIMUM;
		case Recommended:
			return MinimumRecommendedFlag.RECOMMENDED;
		default:
			return MinimumRecommendedFlag.MINIMUM;
		}
	}

	protected void buildBreadcrumbs(Org org, Consortium consortium, String action) {
		List<ReportBreadcrumb> crumbs = Lists.newArrayList();
		ServiceContext context = getServiceContext();
		if (org.getOrgTypeName().equals("School")) {
			ReportBreadcrumb bc = new ReportBreadcrumb();
			bc.setAction(action);
			bc.setLabel(org.getName());
			bc.setLink(userService.hasAccessToOrg(context, context.getUserId(), org.getOrgId()));
			bc.setOrgCode(org.getCode());
			crumbs.add(bc);
			org = orgService.getByCode(getServiceContext(), org.getParentOrgCode());
		}

		if (org.getOrgTypeName().equals("District")) {
			ReportBreadcrumb bc = new ReportBreadcrumb();
			bc.setAction(action);
			bc.setLabel(org.getName());
			bc.setLink(userService.hasAccessToOrg(context, context.getUserId(), org.getOrgId()));
			bc.setOrgCode(org.getCode());
			crumbs.add(bc);
			org = orgService.getByCode(getServiceContext(), org.getParentOrgCode());
		}

		String stateName = null, stateCode = null;
		if (org.getOrgTypeName().equals("State")) {
			ReportBreadcrumb bc = new ReportBreadcrumb();
			bc.setAction(action);
			bc.setLabel(org.getName());
			bc.setLink(userService.hasAccessToOrg(context, context.getUserId(), org.getOrgId()));
			bc.setOrgCode(org.getCode());
			stateName = org.getLocalCode();
			stateName = org.getName();
			crumbs.add(bc);
			org = orgService.getByCode(getServiceContext(), org.getParentOrgCode());
		}

		// add the state code to the available crumbs
		for (ReportBreadcrumb crumb : crumbs) {
			crumb.setStateCode(stateCode);
			crumb.setStateName(stateName);
		}

		ReportBreadcrumb consortiumBC = new ReportBreadcrumb();
		consortiumBC.setAction(action);
		consortiumBC.setLabel(consortium.toString());
		consortiumBC.setLink(userService.hasAccessToOrg(context, context.getUserId(), org.getOrgId()));
		consortiumBC.setOrgCode(org.getCode());

		crumbs.add(consortiumBC);

		Collections.reverse(crumbs);
		breadcrumbs = crumbs;
	}

	protected void addProgressReportLink(ProgressReportLink link) {
		if (breadcrumbs == null || breadcrumbs.isEmpty()) {
			breadcrumbs = Lists.newArrayList();
		}

		ReportBreadcrumb progress = new ReportBreadcrumb();
		progress.setProgressReportLink(link);
		breadcrumbs.add(0, progress);

	}

	public String getColor(double percentage) {
		if (percentage < .26) {
			return "report-level1";
		}
		if (percentage < .51) {
			return "report-level2";
		}
		if (percentage < .76) {
			return "report-level3";
		}
		return "report-level4";
	}

	public String getColor(Long percentage) {
		if (percentage < 26) {
			return "report-level1";
		}
		if (percentage < 51) {
			return "report-level2";
		}
		if (percentage < 76) {
			return "report-level3";
		}
		return "report-level4";
	}

	public String getColor(String value) {
		if ("TBD".equals(value) || "--".equals(value)) {
			return "report-level-null";
		}
		return getColor(Double.valueOf(value));
	}

	public String getColorStaff(double percentage) {
		if (percentage <= 3) {
			return "report-level4";
		}
		if (percentage <= 5) {
			return "report-level3";
		}
		if (percentage <= 7) {
			return "report-level2";
		}
		return "report-level1";
	}

	public String getColorStaff(String value) {
		return getColorStaff(Double.valueOf(value));
	}

	public String getPercent(String value) {
		if ("TBD".equalsIgnoreCase(value)) {
			return value;
		}
		return value + "%";
	}

	public String getPercentColor(String value) {
		String percent = StringUtils.defaultString(value).replaceAll("[>%]", "");
		try {
			return getColor(Long.valueOf(percent));
		} catch (Exception e) {
			return "report-level-null";
		}
	}

	public String getColorBoolean(String value) {
		if (BooleanUtils.toBoolean(value)) {
			return getColor(1);
		}
		return getColor(0);
	}

	public String getAsOfDate() {
		SnapshotWindow snapshot = getCurrentSnapshotWindow();
		if (snapshot.getExecuteDate() == null) {
			return "";
		}

		dateFormat.setTimeZone(TimeZone.getTimeZone("CST"));
		timeFormat.setTimeZone(TimeZone.getTimeZone("CST"));
		StringBuilder sb = new StringBuilder();
		sb.append("Data as of ");
		sb.append(dateFormat.format(snapshot.getExecuteDate()));
		sb.append(" at ");
		sb.append(timeFormat.format(snapshot.getExecuteDate()));
		sb.append(" CT");
		return sb.toString();
	}

	public String getFormattedDate() {

		reportAggDate = getSnapshotWindow().getExecuteDate().toString();

		if (!reportAggDate.isEmpty()) {
			if (reportAggDate.contains("CST") || reportAggDate.contains("-0600")) {
				reportAggDate.replace("CST", "CT");
				reportAggDate.replace("-0600", "CT");
			}

			mm = reportAggDate.substring(5, 7);

			if (mm.equals("01")) {
				monthZ = "January";
			} else if (mm.equals("02")) {
				monthZ = "February";
			}

			else if (mm.equals("03")) {
				monthZ = "March";
			} else if (mm.equals("04")) {
				monthZ = "April";
			} else if (mm.equals("05")) {
				monthZ = "May";
			} else if (mm.equals("06")) {
				monthZ = "June";
			} else if (mm.equals("07")) {
				monthZ = "July";
			} else if (mm.equals("08")) {
				monthZ = "August";
			} else if (mm.equals("09")) {
				monthZ = "September";
			} else if (mm.equals("10")) {
				monthZ = "October";
			} else if (mm.equals("11")) {
				monthZ = "November";
			} else if (mm.equals("12")) {
				monthZ = "December";
			}

			hourInt = Integer.parseInt(reportAggDate.substring(11, 13)); // hour integer value

			if (hourInt < 12) {
				ampm = "AM";
			} else {
				ampm = "PM";
			}
			year = reportAggDate.substring(0, 4); // year
			hour = reportAggDate.substring(11, 13); // hour
			minutes = reportAggDate.substring(14, 16); // minutes
			date = reportAggDate.substring(8, 10); // date
		}
		return monthZ + " " + date + ", " + year + " at " + hour + ":" + minutes + " " + ampm + " CT";

	}

	public Scope getConsortiumScope() {
		return consortiumScope;
	}
}
