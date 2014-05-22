package net.techreadiness.batch;

import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;

public class CoreMappingEvaluationContext extends StandardEvaluationContext {

	public CoreMappingEvaluationContext(Object root) {
		super(root);
		try {
			// Add functions for use in the mapping expressions.
			registerFunction("substring", StringUtils.class.getMethod("substring", String.class, int.class, int.class));
			registerFunction("upperCase", StringUtils.class.getMethod("upperCase", String.class));
			registerFunction("leftPad", StringUtils.class.getMethod("leftPad", String.class, int.class, String.class));
			registerFunction("rightPad", StringUtils.class.getMethod("rightPad", String.class, int.class, String.class));
			registerFunction("trim", StringUtils.class.getMethod("trim", String.class));
			registerFunction("capitalize", WordUtils.class.getMethod("capitalizeFully", String.class));
			registerFunction("isBlank", StringUtils.class.getMethod("isBlank", CharSequence.class));
			registerFunction("split", StringUtils.class.getMethod("split", String.class, char.class));
			registerFunction("join", StringUtils.class.getMethod("join", Object[].class, char.class));
			registerFunction("toBoolean", BooleanUtils.class.getMethod("toBoolean", String.class));

			DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
			conversionService.addFormatterForFieldType(Date.class, new DateFormatter("MM/dd/yyyy"));
			setTypeConverter(new StandardTypeConverter(conversionService));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public CoreMappingEvaluationContext() {
		this(null);
	}
}
