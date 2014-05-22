package net.techreadiness.service.exception;

import javax.xml.ws.WebFault;

import net.techreadiness.service.ServiceContext;

/**
 * Exception thrown when the {@link ServiceContext} provided to the Service APIs is populated with invalid values.
 *
 */
@WebFault(name = "ServiceFault")
public class InvalidServiceContextException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public InvalidServiceContextException() {
	}

	public InvalidServiceContextException(String message) {
		super(message);
	}

	public InvalidServiceContextException(Throwable cause) {
		super(cause);
	}

	public InvalidServiceContextException(String message, Throwable cause) {
		super(message, cause);
	}
}
