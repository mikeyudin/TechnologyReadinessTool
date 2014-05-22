package net.techreadiness.text;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import net.techreadiness.service.CustomTextService;
import net.techreadiness.service.ServiceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class DatabaseResourceBundleImpl extends ResourceBundle {
	@Inject
	private CustomTextService customTextService;
	@Inject
	private ServiceContext serviceContext;

	public DatabaseResourceBundleImpl() {
	}

	public DatabaseResourceBundleImpl(ResourceBundle parent) {
		setParent(parent);
	}

	@Override
	protected Object handleGetObject(String key) {
		return customTextService.getCustomTextByCode(serviceContext, key);
	}

	@Override
	public Enumeration<String> getKeys() {
		Map<String, String> allCustomText = customTextService.findAllCustomText(serviceContext);

		return Collections.enumeration(allCustomText.keySet());
	}

}
