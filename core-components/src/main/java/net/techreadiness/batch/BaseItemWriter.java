package net.techreadiness.batch;

import java.util.Collection;

import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;

public abstract class BaseItemWriter extends AbstractServiceContextProvider {

	protected void handleValidationException(Collection<ValidationError> errors, ValidationServiceException e) {
		if (e.getFaultInfo().getAttributeErrors().isEmpty()) {
			ValidationError error = new ValidationError();
			error.setBatchMessage(e.getMessage());
			error.setOnlineMessage(e.getMessage());
			errors.add(error);
		} else {
			errors.addAll(e.getFaultInfo().getAttributeErrors());
		}
	}
}
