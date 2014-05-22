package net.techreadiness.text;

import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.el.CompositeELResolver;
import javax.el.ExpressionFactory;
import javax.el.ResourceBundleELResolver;
import javax.el.ValueExpression;

import net.techreadiness.el.IndirectResourceBundleELResolver;
import de.odysseus.el.util.SimpleContext;

public class ELAwareResourceBundle extends ResourceBundle {
	private ExpressionFactory factory;
	private ResourceBundle wrapped;

	public ELAwareResourceBundle(ResourceBundle wrapped) {
		factory = new de.odysseus.el.ExpressionFactoryImpl();// ExpressionFactory.newInstance();
		this.wrapped = wrapped;
	}

	@Override
	protected Object handleGetObject(String key) {
		String expression = wrapped.getString(key);
		SimpleContext context = new SimpleContext();
		CompositeELResolver resolver = new CompositeELResolver();
		resolver.add(new IndirectResourceBundleELResolver(wrapped));
		resolver.add(new ResourceBundleELResolver());
		context.setELResolver(resolver);

		ValueExpression ve = factory.createValueExpression(context, expression, String.class);
		String value = (String) ve.getValue(context);
		return value;
	}

	@Override
	public Enumeration<String> getKeys() {
		return wrapped.getKeys();
	}

}
