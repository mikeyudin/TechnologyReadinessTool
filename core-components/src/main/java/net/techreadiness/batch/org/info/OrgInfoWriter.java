package net.techreadiness.batch.org.info;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import net.techreadiness.batch.BaseItemWriter;
import net.techreadiness.batch.Binder;
import net.techreadiness.batch.org.OrgData;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.MessageSource;

public class OrgInfoWriter extends BaseItemWriter implements ItemWriter<OrgData> {
	@Inject
	private OrganizationService orgService;
	@Inject
	@Named("orgInfoBinder")
	private Binder<OrgData> binder;
	@Inject
	private MessageSource messageSource;
	private Boolean dataIsIncomplete = false;

	@Override
	public void write(List<? extends OrgData> items) throws Exception {
		ServiceContext serviceContext = getServiceContext();
		for (OrgData orgData : items) {
			ValidationError typeError = null;
			Org org = orgService.getMatch(serviceContext, orgData.getOrg());
			if (org == null) {
				String msg = messageSource.getMessage("validation.org.noMatch", new Object[] { orgData.getOrg().getCode() },
						Locale.getDefault());
				ValidationError error = new ValidationError("code", "Code", msg);
				ValidationServiceException e = new ValidationServiceException(new FaultInfo());
				e.getFaultInfo().getAttributeErrors().add(error);
				throw e;
			}
			if (!org.getOrgTypeCode().equals("school")) {
				String errorMsg = messageSource.getMessage("validation.org.mustBeSchool", new Object[] { org.getLocalCode(),
						org.getOrgTypeCode() }, Locale.getDefault());
				typeError = new ValidationError("code", "Code", errorMsg);
			}
			OrgData merged = new OrgData();
			merged.setOrg(org);
			merged = binder.bind(merged, orgData.getFieldSet());
			try {
				orgService.addOrUpdate(serviceContext, merged.getOrg());
				org = orgService.getById(getServiceContext(), org.getOrgId());
				dataIsIncomplete = checkForMissingData(org);
				if (dataIsIncomplete && org.getDataEntryComplete() != null && org.getDataEntryComplete().equals("true")) {
					org.setDataEntryComplete("false");
					org.setDataEntryCompleteUser(getServiceContext().getUserName());
					org.setDataEntryCompleteDate(new Date());
				}
				orgService.addOrUpdate(serviceContext, org);
			} catch (ValidationServiceException e) {
				if (typeError != null) {
					e.getFaultInfo().getAttributeErrors().add(typeError);
				}
				throw e;
			}
			if (typeError != null) {
				ValidationServiceException e = new ValidationServiceException(new FaultInfo());
				e.getFaultInfo().getAttributeErrors().add(typeError);
				throw e;
			}
		}
	}

	public Boolean checkForMissingData(Org org) {

		int errorCount = 0;

		if (org.getInternetUtilization() == null || StringUtils.isBlank(org.getInternetUtilization())) {
			errorCount++;
		}
		if (org.getInternetSpeed() == null || StringUtils.isBlank(org.getInternetSpeed())) {
			errorCount++;
		}

		if (org.getNetworkSpeed() == null || StringUtils.isBlank(org.getNetworkSpeed())) {
			errorCount++;
		}

		if (org.getNetworkUtilization() == null || StringUtils.isBlank(org.getNetworkUtilization())) {
			errorCount++;
		}

		if (org.getTestingWindowLength() == null || StringUtils.isBlank(org.getTestingWindowLength())) {
			errorCount++;
		}

		if (org.getSessionsPerDay() == null || StringUtils.isBlank(org.getSessionsPerDay())) {
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
