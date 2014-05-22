package net.techreadiness.plugin.action.task.survey.network;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.task.org.survey.SurveyQuestionsTaskFlowAction;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "network" }),
		@Result(name = "invalid", type = "lastAction", params = { "actionName", "network" }) })
public class SaveAction extends SurveyQuestionsTaskFlowAction implements Preparable {
	private static final long serialVersionUID = 1L;

	@Key(Long.class)
	@Element(Org.class)
	private Map<Long, Org> orgs;

	@Inject
	private OrganizationService organizationService;
	@Inject
	DataModificationStatus dataModificationStatus;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_NETWORK_INFRASTRUCTURE })
	@Override
	public String execute() {
		for (Entry<Long, Org> entry : orgs.entrySet()) {
			if ("school".equalsIgnoreCase(entry.getValue().getOrgTypeCode())) {
				boolean isDataComplete = checkForMissingData(entry.getValue());
				boolean dataEntryComplete = BooleanUtils.toBoolean(entry.getValue().getDataEntryComplete());
				try {
					if (isDataComplete && dataEntryComplete) {
						entry.getValue().setDataEntryComplete("false");
						entry.getValue().setDataEntryCompleteUser(getServiceContext().getUserName());
						entry.getValue().setDataEntryCompleteDate(new Date());
						dataModificationStatus.setMessage(getText("task.mark.dataentry.complete.warning"));
					}
					Org updated = organizationService.addOrUpdate(getServiceContext(), entry.getValue());
					getTaskFlowData().getOrgs().remove(updated);
					getTaskFlowData().getOrgs().add(updated);
				} catch (ValidationServiceException e) {
					List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
					for (ValidationError validationError : errors) {
						addFieldError("orgs[" + entry.getKey() + "]." + validationError.getFieldName(),
								validationError.getOnlineMessage());
					}
				}
			}
		}
		if (hasErrors()) {
			return "invalid";
		}

		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		orgs = Maps.newHashMap();

	}

	public Map<Long, Org> getOrgs() {
		return orgs;
	}

	public void setOrgs(Map<Long, Org> orgs) {
		this.orgs = orgs;
	}

	public boolean checkForMissingData(Org org) {

		int errorCount = 0;

		if (StringUtils.isBlank(org.getInternetUtilization())) {
			errorCount++;
		}
		if (StringUtils.isBlank(org.getInternetSpeed())) {
			errorCount++;
		}

		if (StringUtils.isBlank(org.getNetworkUtilization())) {
			errorCount++;
		}

		if (StringUtils.isBlank(org.getNetworkSpeed())) {
			errorCount++;
		}

		if (StringUtils.isBlank(org.getTestingWindowLength())) {
			errorCount++;
		}

		if (StringUtils.isBlank(org.getSessionsPerDay())) {
			errorCount++;
		}

		int enrollmentCountTotal = getAnsweredEnrollmentCount(org);
		if (enrollmentCountTotal == 0) {
			errorCount++;
		}

		if (errorCount == 0) {
			return false;
		}
		return true;
	}

	private static int getAnsweredEnrollmentCount(Org org) {
		int count = (StringUtils.isBlank(org.getEnrollmentCountK()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount1()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount2()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount3()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount4()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount5()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount6()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount7()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount8()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount9()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount10()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount11()) ? 0 : 1)
				+ (StringUtils.isBlank(org.getEnrollmentCount12()) ? 0 : 1);
		return count;
	}
}
