package net.techreadiness.service;

import java.util.Map;
import java.util.ResourceBundle;

import javax.jws.WebService;

@WebService
public interface CustomTextService {
	Map<String, String> findAllCustomText(ServiceContext context);

	String getCustomTextByCode(ServiceContext context, String code);

	ResourceBundle getCombinedResourceBundle();
}
