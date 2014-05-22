package net.techreadiness.service.exception;

import javax.xml.ws.WebFault;

/**
 * Exception thrown when an empty result occurs from the DB. This is simply a wrapper for the spring
 * EmptyResultDataAccessException thrown in that circumstance.
 */
@WebFault(name = "ServiceFault")
public class EmptyResultDataAccessServiceException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public EmptyResultDataAccessServiceException() {
	}

	public EmptyResultDataAccessServiceException(String message) {
		super(message);
	}

	public EmptyResultDataAccessServiceException(Throwable cause) {
		super(cause);
	}

	public EmptyResultDataAccessServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
