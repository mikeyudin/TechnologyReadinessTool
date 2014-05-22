package net.techreadiness.batch.device;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import net.techreadiness.batch.AbstractServiceContextProvider;
import net.techreadiness.service.DeviceService;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.UserService;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;

import org.springframework.context.MessageSource;

public abstract class AbstractDeviceStep extends AbstractServiceContextProvider {
	@Inject
	protected DeviceService deviceService;
	@Inject
	protected OrganizationService orgService;
	@Inject
	protected UserService userService;
	@Inject
	protected MessageSource messageSource;

	protected void throwIfErrors(List<ValidationError> errors) {
		if (errors == null || !errors.isEmpty()) {
			FaultInfo faultInfo = new FaultInfo();
			faultInfo.setMessage(messageSource.getMessage("validation.device.failed", null, Locale.getDefault()));
			faultInfo.setAttributeErrors(errors);
			throw new ValidationServiceException(faultInfo);
		}
	}

	protected void addGeneralError(List<ValidationError> errors, Exception e) {
		String all = messageSource.getMessage("validation.device.all", null, Locale.getDefault());
		errors.add(new ValidationError(all, all, e.toString() + ". " + e.getMessage(), all, e.toString() + ". "
				+ e.getMessage()));
	}

	protected void addNotAllowedError(List<ValidationError> errors, String lookupCode, String mode) {
		String noOrgAccess = messageSource.getMessage("validation.device.orgCode.notAllowed", new Object[] { lookupCode,
				mode }, Locale.getDefault());
		String orgFieldCode = messageSource.getMessage("validation.device.orgCode", null, Locale.getDefault());
		errors.add(new ValidationError(orgFieldCode, orgFieldCode, noOrgAccess, orgFieldCode, noOrgAccess));
	}
}
