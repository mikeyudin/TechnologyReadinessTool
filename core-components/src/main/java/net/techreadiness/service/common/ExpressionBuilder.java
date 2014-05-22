package net.techreadiness.service.common;

import java.util.List;
import java.util.Map;

import net.techreadiness.persistence.domain.EntityFieldDO;
import net.techreadiness.rules.EndsWithEvaluatorDefinition;
import net.techreadiness.rules.StartsWithEvaluatorDefinition;
import net.techreadiness.service.exception.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.drools.compiler.DrlParser;
import org.drools.compiler.DroolsError;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.lang.descr.AndDescr;
import org.drools.lang.descr.BaseDescr;
import org.drools.lang.descr.ExprConstraintDescr;
import org.drools.lang.descr.PackageDescr;
import org.drools.lang.descr.PatternDescr;
import org.drools.lang.descr.RuleDescr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class ExpressionBuilder {
	private final Logger log = LoggerFactory.getLogger(ExpressionBuilder.class);
	private String rule;
	private Map<String, EntityFieldDO> fields;

	public ExpressionBuilder(String rule, Map<String, EntityFieldDO> fields) {
		this.rule = rule;
		this.fields = fields;
	}

	public List<Expression> buildExpressions() throws ServiceException {
		List<Expression> expressions = Lists.newArrayList();
		PackageBuilderConfiguration config = new PackageBuilderConfiguration();
		config.addEvaluatorDefinition(new StartsWithEvaluatorDefinition());
		config.addEvaluatorDefinition(new EndsWithEvaluatorDefinition());
		PackageBuilder builder = new PackageBuilder();
		builder.getPackageBuilderConfiguration();
		DrlParser parser = new DrlParser();
		try {
			PackageDescr packageDescr = parser.parse(true, rule);
			List<RuleDescr> rules = packageDescr.getRules();
			for (RuleDescr rule : rules) {
				AndDescr lhs = rule.getLhs();
				List<?> clauses = lhs.getDescrs();
				for (Object object : clauses) {
					if (object instanceof PatternDescr) {
						PatternDescr pattern = (PatternDescr) object;
						log.debug("Pattern: {}", pattern);
						List<? extends BaseDescr> descrs = pattern.getConstraint().getDescrs();
						for (BaseDescr baseDescr : descrs) {
							if (baseDescr instanceof ExprConstraintDescr) {
								ExprConstraintDescr constraint = (ExprConstraintDescr) baseDescr;
								String[] parts = StringUtils.split(constraint.getExpression(), "\"");
								// 1 is name, 2 is operator (with extra chars), 3 (if exists) is text

								if (parts != null && parts.length > 1) {
									Expression expression = new Expression();
									String name = parts[1];

									expression.setEntityFieldId(fields.get(name).getEntityFieldId());

									String operator = parts[2];

									operator = StringUtils.remove(operator, "]");
									operator = StringUtils.trimToEmpty(operator);

									expression.setOperator(Operator.getOperator(operator));

									String text = "";

									if (parts.length > 3) {
										text = parts[3];
									} else if (constraint.getExpression().contains("true")) {
										text = "true";
									} else if (constraint.getExpression().contains("false")) {
										text = "false";
									}

									expression.setText(text);
									expressions.add(expression);
								}
							}
						}
					}
				}
			}
			if (parser.hasErrors()) {
				StringBuilder sb = new StringBuilder();
				for (DroolsError droolsError : parser.getErrors()) {
					sb.append("Error on lines: ");
					// sb.append(droolsError.getErrorLines());
					sb.append(", ");
					sb.append(droolsError.getMessage());
					sb.append("\n");
				}
				throw new ServiceException(sb.toString());
			}
			return expressions;
		} catch (DroolsParserException e) {
			throw new ServiceException(e);
		}
	}
}
