package net.techreadiness.service.exception;

import javax.xml.ws.WebFault;

/**
 * Exception thrown when a DB integrity violation occurs. This is simply a wrapper for the spring
 * DataIntegrityViolationException thrown in that circumstance.
 */
@WebFault(name = "ServiceFault")
public class DataIntegrityServiceException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public DataIntegrityServiceException() {
	}

	public DataIntegrityServiceException(String message) {
		super(message);
	}

	public DataIntegrityServiceException(Throwable cause) {
		super(cause);
	}

	public DataIntegrityServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
