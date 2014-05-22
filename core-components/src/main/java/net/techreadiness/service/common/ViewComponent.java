package net.techreadiness.service.common;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class ViewComponent implements Comparable<ViewComponent>, Serializable {
	private static final long serialVersionUID = 1L;
	protected int displayOrder;
	protected String name;
	protected long id;
	protected Long displayRuleId;
	protected Long editRuleId;

	public String getType() {
		String type = StringUtils.substringAfterLast(this.getClass().toString(), ".");
		return type;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getDisplayRuleId() {
		return displayRuleId;
	}

	public void setDisplayRuleId(Long displayRuleId) {
		this.displayRuleId = displayRuleId;
	}

	public Long getEditRuleId() {
		return editRuleId;
	}

	public void setEditRuleId(Long editRuleId) {
		this.editRuleId = editRuleId;
	}

	@Override
	public int compareTo(ViewComponent o) {
		return displayOrder - o.displayOrder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ id >>> 32);
		result = prime * result + (getClass().getName() == null ? 0 : getClass().getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		ViewComponent other = (ViewComponent) obj;
		if (id != other.id) {
			return false;
		}

		return true;
	}
}
