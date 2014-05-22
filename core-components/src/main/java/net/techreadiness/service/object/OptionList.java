package net.techreadiness.service.object;

import java.util.List;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.OptionListDO;

public class OptionList extends BaseObject<OptionListDO> {
	private static final long serialVersionUID = 1L;
	@CoreField
	Long optionListId;
	@CoreField
	String name;
	@CoreField
	String code;

	List<OptionListValue> optionListValues;
	Scope scope;

	public Long getOptionListId() {
		return optionListId;
	}

	public void setOptionListId(Long optionListId) {
		this.optionListId = optionListId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<OptionListValue> getOptionListValues() {
		return optionListValues;
	}

	public void setOptionListValues(List<OptionListValue> optionListValues) {
		this.optionListValues = optionListValues;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public Class<OptionListDO> getBaseEntityType() {
		return OptionListDO.class;
	}

	@Override
	public Long getId() {
		return optionListId;
	}
}
