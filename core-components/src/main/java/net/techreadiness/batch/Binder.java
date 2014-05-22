package net.techreadiness.batch;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.google.common.collect.Maps;

public class Binder<T> {
	private Map<String, String> mappings;

	public Binder(Map<String, String> mappings) {
		this.mappings = mappings;
	}

	public Binder(Properties properties) {
		mappings = Maps.fromProperties(properties);
	}

	public T bind(T target, FieldSet source) {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext targetContext = new CoreMappingEvaluationContext(target);
		EvaluationContext valueContext = new CoreMappingEvaluationContext(source);
		valueContext.getPropertyAccessors().add(new FieldSetPropertyAccessor(source));
		targetContext.getPropertyAccessors().add(new ExtendedAttributePropertyAccessor());

		for (Entry<String, String> entry : mappings.entrySet()) {
			Expression valueExpression = parser.parseExpression(entry.getValue());
			Expression targetExpression = parser.parseExpression(entry.getKey());
			Object value = valueExpression.getValue(valueContext);
			if (value instanceof String && StringUtils.isBlank((String) value)) {
				value = null;
			} else if (value instanceof Iterable && !((Iterable<?>) value).iterator().hasNext()) {
				value = null;
			}

			targetExpression.setValue(targetContext, value);
		}
		return target;
	}
}
