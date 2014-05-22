package net.techreadiness.ui.text;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * allows a ResourceBundle to be used as a TextProvider
 * 
 */
public class ResourceBundleTextProviderAdapter extends SimpleTextProvider {

	private final ResourceBundle resourceBundle;

	public ResourceBundleTextProviderAdapter(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	@Override
	public boolean hasKey(String key) {
		return resourceBundle.containsKey(key);
	}

	@Override
	public ResourceBundle getTexts(String bundleName) {
		return resourceBundle;
	}

	@Override
	public ResourceBundle getTexts() {
		return resourceBundle;
	}

	@Override
	public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
		Object[] argsArray = args != null ? args.toArray() : null;
		Locale locale = (Locale) stack.getContext().get(ActionContext.LOCALE);
		return LocalizedTextUtil.findText(resourceBundle, key, locale, defaultValue, argsArray, stack);
	}
}
