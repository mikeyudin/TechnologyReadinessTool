package net.techreadiness.plugin.action.task.dataentry;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.task.org.dataentry.DataEntryTaskFlowAction;
import net.techreadiness.ui.tags.datagrid.DataGridState;
import net.techreadiness.ui.util.ConversationScoped;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.util.Key;

@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "data-entry" }),
		@Result(name = "invalid", type = "lastAction", params = { "actionName", "data-entry" }) })
public class SaveAction extends DataEntryTaskFlowAction implements Preparable {

	private static final long serialVersionUID = 1L;

	@Key(Long.class)
	@Element(Boolean.class)
	private Map<Long, Boolean> dataEntryComplete;

	@Inject
	private OrganizationService organizationService;

	@ConversationScoped
	private DataGridState<?> orgSearchGrid;

	@Inject
	DataModificationStatus dataModificationStatus;

	@CoreSecured({ CorePermissionCodes.READY_CUSTOMER_NETWORK_INFRASTRUCTURE })
	@Override
	public String execute() {
		for (Org org : new HashSet<>(getTaskFlowData().getOrgs())) {
			try {
				int totalEnrollmentCount = getAnsweredEnrollmentCount(org);
				if (org.getInternetSpeed() != null && org.getNetworkSpeed() != null && org.getInternetUtilization() != null
						&& org.getNetworkUtilization() != null && org.getTestingWindowLength() != null
						&& org.getSessionsPerDay() != null && totalEnrollmentCount > 0) {
					boolean entryComplete = dataEntryComplete.get(org.getOrgId()) != null
							&& dataEntryComplete.get(org.getOrgId()).booleanValue();
					org.setDataEntryComplete(Boolean.toString(entryComplete));
					org.setDataEntryCompleteUser(getServiceContext().getUserName());
					org.setDataEntryCompleteDate(new Date());
					Org updated = organizationService.addOrUpdate(getServiceContext(), org);
					getTaskFlowData().getOrgs().remove(updated);
					getTaskFlowData().getOrgs().add(updated);
				}
			} catch (ValidationServiceException e) {
				List<ValidationError> errors = e.getFaultInfo().getAttributeErrors();
				for (ValidationError validationError : errors) {
					addFieldError("dataEntryComplete[" + org.getOrgId() + "]." + validationError.getFieldName(),
							validationError.getOnlineMessage());
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
		dataEntryComplete = new HashMap<>();
	}

	public Map<Long, Boolean> getDataEntryComplete() {
		return dataEntryComplete;
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
