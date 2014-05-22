package net.techreadiness.service.common;

public class Expression {

	private Long entityFieldId;
	private Operator operator;
	private String text;

	public Long getEntityFieldId() {
		return entityFieldId;
	}

	public void setEntityFieldId(Long entityFieldId) {
		this.entityFieldId = entityFieldId;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("entityFieldId=");
		sb.append(entityFieldId);
		sb.append(", operator=");
		sb.append(operator);
		sb.append(", text=");
		sb.append(text);
		return sb.toString();
	}
}
