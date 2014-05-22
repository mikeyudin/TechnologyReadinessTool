package net.techreadiness.service.exception;

import java.util.List;
import java.util.Map;

import javax.xml.ws.WebFault;

import net.techreadiness.service.common.ValidationError;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@WebFault(name = "ServiceFault")
public class ValidationServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public ValidationServiceException() {

	}

	public ValidationServiceException(String message) {
		super(message);
	}

	public ValidationServiceException(String message, FaultInfo faultInfo) {
		super(message, faultInfo);
	}

	public ValidationServiceException(FaultInfo faultInfo) {
		super(faultInfo);
	}

	public Map<String, List<String>> getErrorMap() {
		return getErrorMap(null);
	}

	public Map<String, List<String>> getErrorMap(String baseFieldName) {
		Map<String, List<String>> errors = Maps.newHashMap();
		for (ValidationError error : getFaultInfo().getAttributeErrors()) {
			String fieldName = (baseFieldName == null ? "" : baseFieldName + ".") + error.getFieldName();
			if (!errors.containsKey(fieldName)) {
				errors.put(fieldName, Lists.<String> newArrayList());
			}
			errors.get(fieldName).add(error.getOnlineMessage());
		}
		return errors;
	}

	public Map<String, List<String>> getBatchErrorMap() {
		Map<String, List<String>> errors = Maps.newHashMap();
		for (ValidationError error : getFaultInfo().getAttributeErrors()) {
			String batchErrorCode = error.getBatchCode();

			if (!errors.containsKey(batchErrorCode)) {
				errors.put(batchErrorCode, Lists.<String> newArrayList());
			}
			errors.get(batchErrorCode).add(error.getBatchMessage());
		}
		return errors;
	}
}
