package net.techreadiness.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.base.Objects;

/**
 * The persistent class for the option_list_value database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "option_list_value")
public class OptionListValueDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "option_list_value_id", unique = true, nullable = false)
	private Long optionListValueId;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(nullable = false, length = 200)
	private String name;

	@Column(nullable = false, length = 200)
	private String value;

	// bi-directional many-to-one association to OptionListDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_list_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private OptionListDO optionList;

	public OptionListValueDO() {
	}

	public Long getOptionListValueId() {
		return optionListValueId;
	}

	public void setOptionListValueId(Long optionListValueId) {
		this.optionListValueId = optionListValueId;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public OptionListDO getOptionList() {
		return optionList;
	}

	public void setOptionList(OptionListDO optionList) {
		this.optionList = optionList;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("optionListValueId", optionListValueId).add("name", name)
				.add("value", value).toString();
	}
}