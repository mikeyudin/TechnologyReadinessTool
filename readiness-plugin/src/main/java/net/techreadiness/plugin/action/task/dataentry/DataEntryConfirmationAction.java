package net.techreadiness.plugin.action.task.dataentry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.plugin.service.AlertBoxService;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.BaseAction;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({ @Result(name = "success", type = "json", params = { "root", "actionMessages" }) })
public class DataEntryConfirmationAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Inject
	AlertBoxService alertBoxService;

	@Inject
	OrganizationService orgService;

	private Long orgId;
	Org org;

	public static final String COMPLETED = "All the readiness Survey Questions have been answered";
	public static final String PARTIAL = "Some Readiness Survey Questions have not been answered";
	public static final String NONE = "No readiness Survey Questions have been answered";
	public static final String MISSING_READINESS_DETERMINANTS = "Some devices have been entered, but there are missing data for readiness determinant fields";
	public static final String NO_DEVICE = "No devices have been entered";
	private static final int TOTAL_SURVEY_QUESTION_COUNT = 16;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_NETWORK_INFRASTRUCTURE })
	@Override
	public String execute() {
		Long deviceCount = alertBoxService.getDeviceCount(getServiceContext(), orgId);
		if (deviceCount == null || deviceCount.longValue() < 1) {
			addActionMessage(getText("no.devices.for.organization"));
		}

		Long devicesMissingFields = alertBoxService.getDeviceCountMissingReadinessDeterminants(getServiceContext(), orgId);
		if (devicesMissingFields == null || devicesMissingFields.longValue() > 0) {
			addActionMessage(getText("devices.missing.readiness.determinants"));
		}

		Org org = orgService.getById(getServiceContext(), orgId);
		int answeredCount = answeredQuestionsCount(org);

		if (answeredCount < TOTAL_SURVEY_QUESTION_COUNT) {
			addActionMessage(getText("unanswered.survey.questions"));
		}
		return SUCCESS;
	}

	private static int answeredQuestionsCount(Org o) {
		int count = (answeredEnrollmentCount(o) == 0 ? 0 : 1) + (StringUtils.isBlank(o.getSchoolType()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getInternetSpeed()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getInternetUtilization()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getNetworkSpeed()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getNetworkUtilization()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getWirelessAccessPoints()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getSimultaneousTesters()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getTestingWindowLength()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getSessionsPerDay()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getSurveyAdminCount()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getSurveyAdminUnderstanding()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getSurveyAdminTraining()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getSurveyTechstaffCount()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getSurveyTechstaffUnderstanding()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getSurveyTechstaffTraining()) ? 0 : 1);
		return count;
	}

	private static int answeredEnrollmentCount(Org o) {
		int count = (StringUtils.isBlank(o.getEnrollmentCountK()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount1()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount2()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount3()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount4()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount5()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount6()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount7()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount8()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount9()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount10()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount11()) ? 0 : 1)
				+ (StringUtils.isBlank(o.getEnrollmentCount12()) ? 0 : 1);
		return count;
	}

	public AlertBoxService getAlertBoxService() {
		return alertBoxService;
	}

	public void setAlertBoxService(AlertBoxService alertBoxService) {
		this.alertBoxService = alertBoxService;
	}

	public OrganizationService getOrgService() {
		return orgService;
	}

	public void setOrgService(OrganizationService orgService) {
		this.orgService = orgService;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String assessSurveyQuestionsAnsweredStatus(ServiceContext context, Long orgId) {
		int answeredCount = answeredQuestionsCount(org);

		orgId = org.getOrgId();
		if (answeredCount == 0) {
			return NONE;
		}
		if (answeredCount < TOTAL_SURVEY_QUESTION_COUNT) {
			return PARTIAL;
		}
		return COMPLETED;
	}
}
