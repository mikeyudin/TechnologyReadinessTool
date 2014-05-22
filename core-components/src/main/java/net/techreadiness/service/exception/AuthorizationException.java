package net.techreadiness.service.exception;

import javax.xml.ws.WebFault;

/**
 * Exception thrown when a secured method is attempted to be executed by a principal that lacks access.
 */
@WebFault(name = "ServiceFault")
public class AuthorizationException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public AuthorizationException() {
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
	}

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
}