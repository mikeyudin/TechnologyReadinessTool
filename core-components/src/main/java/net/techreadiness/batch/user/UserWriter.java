package net.techreadiness.batch.user;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import net.techreadiness.batch.BaseItemWriter;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.UserOrgService;
import net.techreadiness.service.UserRoleService;
import net.techreadiness.service.UserService;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ValidationServiceException;
import net.techreadiness.service.object.User;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.MessageSource;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class UserWriter extends BaseItemWriter implements ItemWriter<UserData> {
	@Inject
	private UserService userService;
	@Inject
	private UserOrgService userOrgService;
	@Inject
	private UserRoleService userRoleService;
	@Inject
	private MessageSource messageSource;

	@Override
	public void write(List<? extends UserData> items) throws Exception {
		for (final UserData userData : items) {
			List<ValidationError> errors = Lists.newLinkedList();
			ServiceContext serviceContext = getServiceContext();
			User user = null;
			try {
				if ("c".equalsIgnoreCase(userData.getAction())) {
					userData.getUser().setEmail(userData.getUser().getUsername());
					user = userService.create(serviceContext, userData.getUser());

				} else if ("u".equalsIgnoreCase(userData.getAction())) {
					User existingUser = userService.getByUsername(serviceContext, userData.getUser().getUsername());
					userData.getUser().setEmail(existingUser.getEmail());
					user = userService.update(serviceContext, userData.getUser());

				} else if ("d".equalsIgnoreCase(userData.getAction())) {
					User existingUser = userService.getByUsername(serviceContext, userData.getUser().getUsername());
					userData.getUser().setEmail(existingUser.getEmail());
					Date userDeleteDate = userData.getUser().getDeleteDate();
					if (userDeleteDate == null) {
						Date deleteDate = new Date();
						userData.getUser().setDeleteDate(deleteDate);
					}
					user = userService.update(serviceContext, userData.getUser());

				} else {
					ValidationError invalidOperation = new ValidationError("action", "Action", messageSource.getMessage(
							"validation.user.batch.actionIsRequired", null, Locale.getDefault()));
					errors.add(invalidOperation);
				}

				if (StringUtils.isBlank(userData.getStateCode())) {
					ValidationError stateCode = new ValidationError("stateCode", "State Code", messageSource.getMessage(
							"validation.user.readinessStateCodeRequired", null, Locale.getDefault()));
					errors.add(stateCode);
				}
			} catch (ValidationServiceException e) {
				handleValidationException(errors, e);
			}

			List<String> orgCodes = Lists.transform(userData.getOrgCodes(), new Function<String, String>() {

				@Override
				public String apply(String input) {
					return userData.getStateCode() + "-" + input;
				}
			});

			if (user != null) {
				try {
					userOrgService.mergeUserOrgs(serviceContext, user.getUserId(), orgCodes);
				} catch (ValidationServiceException e) {
					handleValidationException(errors, e);
				}
				try {
					userRoleService.mergeUserRoles(serviceContext, user.getUserId(), userData.getRoleCodes());
				} catch (ValidationServiceException e) {
					handleValidationException(errors, e);
				}
			}

			if (errors.isEmpty()) {
				if ("c".equalsIgnoreCase(userData.getAction())) {
					// Wait to send an email in case the transaction rolls back
					//userService.newAccountPassword(serviceContext, user);
				}
			} else {
				ValidationServiceException e = new ValidationServiceException(new FaultInfo());
				e.getFaultInfo().getAttributeErrors().addAll(errors);
				throw e;
			}
		}
	}

}
