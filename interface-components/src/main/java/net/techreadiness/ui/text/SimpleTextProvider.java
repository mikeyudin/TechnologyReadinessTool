package net.techreadiness.ui.text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Abstract class to make implementing a TextProvider a little more sane
 */
public abstract class SimpleTextProvider implements TextProvider {

	@Override
	public abstract boolean hasKey(String key);

	@Override
	public abstract ResourceBundle getTexts(String bundleName);

	@Override
	public abstract ResourceBundle getTexts();

	@Override
	public abstract String getText(String key, String defaultValue, List<?> args, ValueStack stack);

	@Override
	public String getText(String key) {
		return getText(key, key, Collections.emptyList());
	}

	@Override
	public String getText(String key, String defaultValue) {
		return getText(key, defaultValue, Collections.emptyList());
	}

	@Override
	public String getText(String key, String defaultValue, String obj) {
		return getText(key, defaultValue, Arrays.asList(obj));
	}

	@Override
	public String getText(String key, List<?> args) {
		return getText(key, key, args);
	}

	@Override
	public String getText(String key, String[] args) {
		return getText(key, key, args);
	}

	@Override
	public String getText(String key, String defaultValue, String[] args) {
		return getText(key, defaultValue, Arrays.asList(args));
	}

	@Override
	public String getText(String key, String defaultValue, List<?> args) {
		return getText(key, defaultValue, args, ActionContext.getContext().getValueStack());
	}

	@Override
	public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
		return getText(key, defaultValue, Arrays.asList(args), stack);
	}

}
