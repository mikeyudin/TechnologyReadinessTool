package net.techreadiness.service.exception;

import javax.xml.ws.WebFault;

/**
 * Exception thrown when attempting to create a DB entity that has been determined (exact means is specific to the entity
 * type) to already exist.
 */
@WebFault(name = "ServiceFault")
public class RecordAlreadyExistsServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public RecordAlreadyExistsServiceException() {
	}

	public RecordAlreadyExistsServiceException(String message) {
		super(message);
	}

	public RecordAlreadyExistsServiceException(Throwable cause) {
		super(cause);
	}

	public RecordAlreadyExistsServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
