package net.techreadiness.batch.org;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import net.techreadiness.batch.BaseItemProcessor;
import net.techreadiness.batch.Binder;
import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.Org;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;

public class OrgMatchingItemProcessor extends BaseItemProcessor implements ItemProcessor<OrgData, OrgData> {
	@Inject
	private OrganizationService organizationService;
	@Inject
	@Named("orgBinder")
	private Binder<OrgData> binder;
	@Inject
	private MessageSource messageSource;

	@Override
	public OrgData process(OrgData input) throws Exception {
		ServiceContext context = getServiceContext();
		List<ValidationError> errors = Lists.newArrayList();
		if (StringUtils.isBlank(input.getOrg().getOrgTypeCode())) {
			ValidationError error = new ValidationError("orgTypeCode", "Organization Type",
					"The organization type is required.");
			errors.add(error);
		}
		if (StringUtils.isBlank(input.getOrg().getParentOrgCode())) {
			ValidationError error = new ValidationError("parentOrgCode", "Parent Organization",
					"The parent organization is required.");
			errors.add(error);
		}

		if (!errors.isEmpty()) {
			ValidationServiceException e = new ValidationServiceException(new FaultInfo());
			e.getFaultInfo().getAttributeErrors().addAll(errors);
			throw e;
		}

		Org match = organizationService.getMatch(context, input.getOrg());

		if (match != null) {
			if (!match.getParentOrgLocalCode().equals(input.getOrg().getParentOrgLocalCode())) {
				String msg = messageSource.getMessage("validation.org.parent.not.same", new Object[] {
						input.getOrg().getParentOrgLocalCode(), match.getParentOrgLocalCode() }, Locale.getDefault());
				errors.add(new ValidationError("parentOrgCode", "Parent Organization", msg));
				ValidationServiceException e = new ValidationServiceException(new FaultInfo());
				e.getFaultInfo().getAttributeErrors().addAll(errors);
				throw e;
			}
			input.setOrg(match);
			return binder.bind(input, input.getFieldSet());
		}
		return input;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public void setBinder(Binder<OrgData> binder) {
		this.binder = binder;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
