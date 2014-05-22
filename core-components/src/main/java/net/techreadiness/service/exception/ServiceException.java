package net.techreadiness.service.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.ws.WebFault;

@WebFault(name = "ServiceFault")
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private FaultInfo faultInfo;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);

		createFaultInfo();
	}

	public ServiceException(Throwable cause) {
		super(cause);

		createFaultInfo();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);

		createFaultInfo();
	}

	public ServiceException(String message, FaultInfo faultInfo) {
		super(message);

		this.faultInfo = faultInfo;
		populateFaultInfo();
	}

	public ServiceException(FaultInfo faultInfo) {
		super(faultInfo.getMessage());

		this.faultInfo = faultInfo;
		populateFaultInfo();
	}

	private void createFaultInfo() {
		faultInfo = new FaultInfo();
		faultInfo.setMessage(this.getMessage());

		populateFaultInfo();
	}

	private void populateFaultInfo() {
		final Writer writer = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(writer);

		this.printStackTrace(printWriter);

		faultInfo.setStackTrace(writer.toString());
	}

	/**
	 * Gets the fault information about this exception. The message, stack trace, and any validation errors are contained in
	 * here.
	 * 
	 * @return FaultInfo
	 */
	public FaultInfo getFaultInfo() {
		return faultInfo;
	}
}
