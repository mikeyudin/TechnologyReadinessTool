package net.techreadiness.service.exception;

import javax.xml.ws.WebFault;

/**
 * Exception thrown when an action (eg. update, delete) is requested on a specific entity but that entity doesn't exist in
 * the DB.
 * 
 * This is not used for a simple find method (eg. getById). A null is expected to returned in that instance.
 */
@WebFault(name = "ServiceFault")
public class RecordNotFoundServiceException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public RecordNotFoundServiceException() {
	}

	public RecordNotFoundServiceException(String message) {
		super(message);
	}

	public RecordNotFoundServiceException(Throwable cause) {
		super(cause);
	}

	public RecordNotFoundServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
