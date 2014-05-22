package net.techreadiness.service.object;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.OptionListValueDO;

public class OptionListValue extends BaseObject<OptionListValueDO> {
	private static final long serialVersionUID = 1L;
	@CoreField
	Long optionListValueId;
	@CoreField
	String name;
	@CoreField
	String value;
	@CoreField
	Integer displayOrder;

	public Long getOptionListValueId() {
		return optionListValueId;
	}

	public void setOptionListValueId(Long optionListValueId) {
		this.optionListValueId = optionListValueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public Class<OptionListValueDO> getBaseEntityType() {
		return OptionListValueDO.class;
	}

	@Override
	public Long getId() {
		return null; // To change body of implemented methods use File | Settings | File Templates.
	}
}
