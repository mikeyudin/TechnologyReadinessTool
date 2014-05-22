package net.techreadiness.rules;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.lang3.StringUtils;
import org.drools.base.BaseEvaluator;
import org.drools.base.ValueType;
import org.drools.base.evaluators.EvaluatorCache;
import org.drools.base.evaluators.EvaluatorDefinition;
import org.drools.base.evaluators.Operator;
import org.drools.common.InternalWorkingMemory;
import org.drools.rule.VariableRestriction.ObjectVariableContextEntry;
import org.drools.rule.VariableRestriction.VariableContextEntry;
import org.drools.spi.Evaluator;
import org.drools.spi.FieldValue;
import org.drools.spi.InternalReadAccessor;

public class EndsWithEvaluatorDefinition implements EvaluatorDefinition {
	public static final Operator ENDS_WITH = Operator.addOperatorToRegistry("endsWith", false);
	public static final Operator NOT_ENDS_WITH = Operator.addOperatorToRegistry("endsWith", true);
	private static final String[] SUPPORTED_IDS = { ENDS_WITH.getOperatorString() };

	private EvaluatorCache evaluators = new EvaluatorCache() {
		{
			addEvaluator(ValueType.STRING_TYPE, ENDS_WITH, EndsWithEvaluator.INSTANCE);
			addEvaluator(ValueType.STRING_TYPE, NOT_ENDS_WITH, NotEndsWithEvaluator.INSTANCE);
		}
	};

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(evaluators);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		evaluators = (EvaluatorCache) in.readObject();
	}

	@Override
	public String[] getEvaluatorIds() {
		return SUPPORTED_IDS;
	}

	@Override
	public boolean isNegatable() {
		return true;
	}

	@Override
	public Evaluator getEvaluator(ValueType type, String operatorId, boolean isNegated, String parameterText,
			Target leftTarget, Target rightTarget) {
		return evaluators.getEvaluator(type, Operator.determineOperator(operatorId, isNegated));
	}

	@Override
	public Evaluator getEvaluator(ValueType type, String operatorId, boolean isNegated, String parameterText) {
		return getEvaluator(type, operatorId, isNegated, parameterText, Target.FACT, Target.FACT);
	}

	@Override
	public Evaluator getEvaluator(ValueType type, Operator operator, String parameterText) {
		return evaluators.getEvaluator(type, operator);
	}

	@Override
	public Evaluator getEvaluator(ValueType type, Operator operator) {
		return evaluators.getEvaluator(type, operator);
	}

	@Override
	public boolean supportsType(ValueType type) {
		return type.equals(ValueType.STRING_TYPE);
	}

	@Override
	public Target getTarget() {
		return Target.FACT;
	}

	public static class EndsWithEvaluator extends BaseEvaluator {
		public final static Evaluator INSTANCE = new EndsWithEvaluator();

		public EndsWithEvaluator() {
			super(ValueType.STRING_TYPE, ENDS_WITH);
		}

		@Override
		public boolean evaluate(InternalWorkingMemory workingMemory, InternalReadAccessor extractor, Object object,
				FieldValue value) {
			final String value1 = (String) extractor.getValue(workingMemory, object);
			final String value2 = (String) value.getValue();
			return StringUtils.endsWith(value1, value2);
		}

		@Override
		public boolean evaluate(InternalWorkingMemory workingMemory, InternalReadAccessor leftExtractor, Object left,
				InternalReadAccessor rightExtractor, Object right) {
			final Object value1 = leftExtractor.getValue(workingMemory, left);
			final Object value2 = rightExtractor.getValue(workingMemory, right);
			return StringUtils.endsWith((String) value1, (String) value2);
		}

		@Override
		public boolean evaluateCachedLeft(InternalWorkingMemory workingMemory, VariableContextEntry context, Object right) {
			final String value = (String) context.extractor.getValue(workingMemory, right);
			return StringUtils.endsWith(value, (String) ((ObjectVariableContextEntry) context).left);
		}

		@Override
		public boolean evaluateCachedRight(InternalWorkingMemory workingMemory, VariableContextEntry context, Object left) {
			final String value = (String) ((ObjectVariableContextEntry) context).right;
			return StringUtils.endsWith(value, (String) context.declaration.getExtractor().getValue(workingMemory, left));
		}

		@Override
		public String toString() {
			return "Starts with evaluator";
		}
	}

	public static class NotEndsWithEvaluator extends BaseEvaluator {
		public final static Evaluator INSTANCE = new NotEndsWithEvaluator();

		public NotEndsWithEvaluator() {
			super(ValueType.STRING_TYPE, NOT_ENDS_WITH);
		}

		@Override
		public boolean evaluate(InternalWorkingMemory workingMemory, InternalReadAccessor extractor, Object object,
				FieldValue value) {
			final String value1 = (String) extractor.getValue(workingMemory, object);
			final String value2 = (String) value.getValue();
			return !StringUtils.endsWith(value1, value2);
		}

		@Override
		public boolean evaluate(InternalWorkingMemory workingMemory, InternalReadAccessor leftExtractor, Object left,
				InternalReadAccessor rightExtractor, Object right) {
			final Object value1 = leftExtractor.getValue(workingMemory, left);
			final Object value2 = rightExtractor.getValue(workingMemory, right);
			return !StringUtils.endsWith((String) value1, (String) value2);
		}

		@Override
		public boolean evaluateCachedLeft(InternalWorkingMemory workingMemory, VariableContextEntry context, Object right) {
			final String value = (String) context.extractor.getValue(workingMemory, right);
			return !StringUtils.endsWith(value, (String) ((ObjectVariableContextEntry) context).left);
		}

		@Override
		public boolean evaluateCachedRight(InternalWorkingMemory workingMemory, VariableContextEntry context, Object left) {
			final String value = (String) ((ObjectVariableContextEntry) context).right;
			return !StringUtils.endsWith(value, (String) context.declaration.getExtractor().getValue(workingMemory, left));
		}

		@Override
		public String toString() {
			return "Starts with evaluator";
		}
	}
}
