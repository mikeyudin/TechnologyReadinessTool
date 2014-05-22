package net.techreadiness.plugin.action.reports;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.plugin.action.reports.ReportItemProvider.ExportType;
import net.techreadiness.plugin.service.reports.MinimumRecommendedFlag;
import net.techreadiness.plugin.service.reports.ReportsService;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ScopeService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Results({ @Result(name = "success", location = "/net/techreadiness/plugin/action/reports/currentInfo.jsp") })
public class CurrentInfoAction extends ReportAction {
	private static final long serialVersionUID = 1L;

	private Long orgId;
	private String orgCode;
	private Map<String, String> org;
	private Org currentOrg;
	Map<String, String> orgSummary;

	private boolean showTaskLinks;
	private boolean reset;

	@Inject
	protected ScopeService scopeService;

	@Inject
	private OrganizationService orgService;

	@Inject
	private ReportsService reportsService;

	@Override
	@CoreSecured({ CorePermissionCodes.CORE_CUSTOMER_ORGANIZATION_ACCESS })
	public String execute() throws Exception {

		ServiceContext context = getServiceContext();

		if (orgId == null || orgId.equals("")) {
			Org org = context.getOrg();
			orgId = org.getId();
		}

		currentOrg = orgService.getById(context, orgId);

		if (currentOrg == null || !userService.hasAccessToOrg(context, context.getUserId(), currentOrg.getOrgId())) {
			if (currentOrg == null) {
				throw new Exception("Org could not be found: " + orgCode);
			}
			throw new Exception("User(" + context.getUserId() + ") doesn't have access to org (" + currentOrg.getOrgId()
					+ ")");
		}

		if (currentOrg.getSurveyAdminCount() == null) {
			currentOrg.setSurveyAdminCount("(missing)");
		}
		if (currentOrg.getSurveyAdminTraining() == null) {
			currentOrg.setSurveyAdminTraining("(missing)");
		}
		if (currentOrg.getSurveyAdminUnderstanding() == null) {
			currentOrg.setSurveyAdminUnderstanding("(missing)");
		}
		if (currentOrg.getSurveyTechstaffCount() == null) {
			currentOrg.setSurveyTechstaffCount("(missing)");
		}
		if (currentOrg.getSurveyTechstaffTraining() == null) {
			currentOrg.setSurveyTechstaffTraining("(missing)");
		}
		if (currentOrg.getSurveyTechstaffUnderstanding() == null) {
			currentOrg.setSurveyTechstaffUnderstanding("(missing)");
		}

		Collection<Map<String, String>> results;

		QueryResult<Map<String, String>> result = reportsService.retrieveSummaryForOrg(getSnapshotWindow()
				.getSnapshotWindowId(), currentOrg.getOrgId(), MinimumRecommendedFlag.MINIMUM);

		results = result.getRows();

		orgSummary = Iterables.getFirst(results, new HashMap<String, String>());

		Collection<Map<String, String>> recommendedResults = Lists.newArrayList();

		QueryResult<Map<String, String>> recommendedResult = reportsService.retrieveSummaryForOrg(getSnapshotWindow()
				.getSnapshotWindowId(), currentOrg.getOrgId(), MinimumRecommendedFlag.RECOMMENDED);

		recommendedResults = recommendedResult.getRows();

		for (Map<String, String> map : recommendedResults) {

			String recDevicePassingCount = map.get("devicePassingPercent") == null ? "" : map.get("devicePassingCount")
					.toUpperCase();
			orgSummary.put("recDevicePassingCount", recDevicePassingCount);
			String recDevicePassingPercent = map.get("devicePassingPercent") == null ? "" : map.get("devicePassingPercent")
					.toUpperCase();
			orgSummary.put("recDevicePassingPercent", recDevicePassingPercent);
			int totalEnrollmentCount = answeredEnrollmentCount(currentOrg);
			orgSummary.put("totalEnrollmentCount", Integer.toString(totalEnrollmentCount));
		}

		return SUCCESS;
	}

	private static int answeredEnrollmentCount(Org o) {
		int count = (StringUtils.isBlank(o.getEnrollmentCountK()) ? 0 : Integer.parseInt(o.getEnrollmentCountK()))
				+ (StringUtils.isBlank(o.getEnrollmentCount1()) ? 0 : Integer.parseInt(o.getEnrollmentCount1()))
				+ (StringUtils.isBlank(o.getEnrollmentCount2()) ? 0 : Integer.parseInt(o.getEnrollmentCount2()))
				+ (StringUtils.isBlank(o.getEnrollmentCount3()) ? 0 : Integer.parseInt(o.getEnrollmentCount3()))
				+ (StringUtils.isBlank(o.getEnrollmentCount4()) ? 0 : Integer.parseInt(o.getEnrollmentCount4()))
				+ (StringUtils.isBlank(o.getEnrollmentCount5()) ? 0 : Integer.parseInt(o.getEnrollmentCount5()))
				+ (StringUtils.isBlank(o.getEnrollmentCount6()) ? 0 : Integer.parseInt(o.getEnrollmentCount6()))
				+ (StringUtils.isBlank(o.getEnrollmentCount7()) ? 0 : Integer.parseInt(o.getEnrollmentCount7()))
				+ (StringUtils.isBlank(o.getEnrollmentCount8()) ? 0 : Integer.parseInt(o.getEnrollmentCount8()))
				+ (StringUtils.isBlank(o.getEnrollmentCount9()) ? 0 : Integer.parseInt(o.getEnrollmentCount9()))
				+ (StringUtils.isBlank(o.getEnrollmentCount10()) ? 0 : Integer.parseInt(o.getEnrollmentCount10()))
				+ (StringUtils.isBlank(o.getEnrollmentCount11()) ? 0 : Integer.parseInt(o.getEnrollmentCount11()))
				+ (StringUtils.isBlank(o.getEnrollmentCount12()) ? 0 : Integer.parseInt(o.getEnrollmentCount12()));
		return count;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public boolean isShowTaskLinks() {
		return showTaskLinks;
	}

	public void setShowTaskLinks(boolean showTaskLinks) {
		this.showTaskLinks = showTaskLinks;
	}

	public Map<String, String> getOrg() {
		return org;
	}

	public void setOrg(Map<String, String> org) {
		this.org = org;
	}

	public Org getCurrentOrg() {
		return currentOrg;
	}

	public void setCurrentOrg(Org currentOrg) {
		this.currentOrg = currentOrg;
	}

	public Map<String, String> getOrgSummary() {
		return orgSummary;
	}

	public void setOrgSummary(Map<String, String> orgSummary) {
		this.orgSummary = orgSummary;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	@Override
	protected ReportExport getReportExport(ExportType type) throws Exception {
		return null;
	}

	@Override
	protected ReportExport getAllSchoolsReportExport(ExportType type) throws Exception {
		return null;
	}

}