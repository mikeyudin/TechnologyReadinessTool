package net.techreadiness.ui.action;

import net.techreadiness.service.exception.FaultInfo;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.ui.BaseAction;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;

@Results({ @Result(name = Action.SUCCESS, location = "/error/error.jsp") })
public class ErrorAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(ErrorAction.class);
	private Throwable throwable;
	private FaultInfo faultInfo;

	@Override
	public String execute() {
		Object e = getValueStack().findValue("exception");

		if (e instanceof ServiceException) {
			ServiceException serviceException = (ServiceException) e;
			faultInfo = serviceException.getFaultInfo();
			throwable = serviceException;
		} else if (e instanceof Exception) {
			throwable = (Exception) e;
			faultInfo = new FaultInfo();
		}
		if (throwable != null) {
			log.error("Unhandled Exception", e);
		}
		return SUCCESS;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public FaultInfo getFaultInfo() {
		return faultInfo;
	}
}
