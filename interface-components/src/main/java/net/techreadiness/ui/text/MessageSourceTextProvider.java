package net.techreadiness.ui.text;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import net.techreadiness.ui.util.ContextUtils;

import org.springframework.context.MessageSource;

import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;

public class MessageSourceTextProvider extends SimpleTextProvider {

	private final TextProvider delegate;

	@Inject
	public MessageSourceTextProvider(@Inject ServletContext ctx) {
		MessageSource messageSource = ContextUtils.getRequiredBeanOfType(MessageSource.class, ctx);
		delegate = new MessageSourceTextProviderAdapter(messageSource);
	}

	@Override
	public boolean hasKey(String key) {
		return delegate.hasKey(key);
	}

	@Override
	public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
		return delegate.getText(key, defaultValue, args, stack);
	}

	@Override
	public ResourceBundle getTexts(String bundleName) {
		return delegate.getTexts(bundleName);
	}

	@Override
	public ResourceBundle getTexts() {
		return delegate.getTexts();
	}

}
