package net.techreadiness.batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.core.io.Resource;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.google.common.collect.Lists;

public class CoreFieldExtractor<T> implements FieldExtractor<T> {
	private Resource resource;

	@Override
	public Object[] extract(T item) {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext elContext = new CoreMappingEvaluationContext(item);
		elContext.getPropertyAccessors().add(new ExtendedAttributePropertyAccessor());

		try (InputStream iStream = resource.getInputStream()) {
			List<String> expressions = IOUtils.readLines(iStream);
			List<Object> fields = Lists.newArrayListWithCapacity(expressions.size());
			for (String expression : expressions) {
				String value = parser.parseExpression(expression).getValue(elContext, String.class);
				if (value != null && value.contains("\"") && !"\"\"".equals(value)) {
					value = value.replaceAll("\"", "\"\"");
				}
				if (value != null && value.contains(",")) {
					StringBuilder sb = new StringBuilder(value.length() + 2);
					sb.append("\"");
					sb.append(value);
					sb.append("\"");
					value = sb.toString();
				}
				fields.add(value);
			}
			return fields.toArray(new Object[fields.size()]);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
