package net.techreadiness.text;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;

public class CoreResourceBundleMessageSource extends ResourceBundleMessageSource {
	@Inject
	private ApplicationContext applicationContext;

	@Override
	protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
		ResourceBundle bundleToWrap = super.doGetBundle(basename, locale);
		String[] names = applicationContext.getBeanNamesForType(DatabaseResourceBundleImpl.class);
		if (names != null && names.length > 0) {
			bundleToWrap = (DatabaseResourceBundleImpl) applicationContext.getBean(names[0], bundleToWrap);
		}

		return new ELAwareResourceBundle(bundleToWrap);
	}
}
