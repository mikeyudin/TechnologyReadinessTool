package net.techreadiness.ui.text;

import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.ValueStack;

public class MessageSourceTextProviderAdapter extends SimpleTextProvider {
	private MessageSource messageSource;
	private static final Logger log = LoggerFactory.getLogger(MessageSourceTextProviderAdapter.class);

	public MessageSourceTextProviderAdapter(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public boolean hasKey(String key) {
		try {
			messageSource.getMessage(key, null, null);
		} catch (NoSuchMessageException e) {
			return false;
		}
		return true;
	}

	@Override
	public ResourceBundle getTexts(String bundleName) {
		return LocalizedTextUtil.findResourceBundle(bundleName, null);
	}

	@Override
	public ResourceBundle getTexts() {
		return new ResourceBundle() {

			@Override
			protected Object handleGetObject(String key) {
				try {
					return messageSource.getMessage(key, null, null);
				} catch (NoSuchMessageException e) {
					log.warn("No text found for key: {}", key);
					return key;
				}
			}

			@Override
			public Enumeration<String> getKeys() {
				return null;
			}
		};
	}

	@Override
	public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
		Object[] argsArray = args != null ? args.toArray() : null;
		Locale locale = (Locale) stack.getContext().get(ActionContext.LOCALE);
		return LocalizedTextUtil.findText(getTexts(), key, locale, defaultValue, argsArray, stack);
	}

}
