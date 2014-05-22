package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.base.Objects;

/**
 * The persistent class for the option_list database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "option_list")
public class OptionListDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "option_list_id", unique = true, nullable = false)
	private Long optionListId;

	@Column(nullable = false, length = 100)
	private String code;

	@Column(nullable = false, length = 200)
	private String name;

	@Column(nullable = false)
	private boolean shared;

	@Column(name = "sql_text", length = 60000)
	private String sqlText;

	// bi-directional many-to-one association to EntityFieldDO
	@OneToMany(mappedBy = "optionList")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<EntityFieldDO> entityFields;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private ScopeDO scope;

	// bi-directional many-to-one association to OptionListValueDO
	@OneToMany(mappedBy = "optionList")
	@OrderBy("displayOrder")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<OptionListValueDO> optionListValues;

	public OptionListDO() {
	}

	public Long getOptionListId() {
		return optionListId;
	}

	public void setOptionListId(Long optionListId) {
		this.optionListId = optionListId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public String getSqlText() {
		return sqlText;
	}

	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}

	public List<EntityFieldDO> getEntityFields() {
		return entityFields;
	}

	public void setEntityFields(List<EntityFieldDO> entityFields) {
		this.entityFields = entityFields;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	public List<OptionListValueDO> getOptionListValues() {
		return optionListValues;
	}

	public void setOptionListValues(List<OptionListValueDO> optionListValues) {
		this.optionListValues = optionListValues;
	}

	public Map<String, String> getOptionListAsMap() {
		Map<String, String> optionMap = new LinkedHashMap<>();
		if (getOptionListValues() != null) {
			for (OptionListValueDO optionDo : getOptionListValues()) {
				optionMap.put(optionDo.getValue(), optionDo.getName());
			}
		}
		return optionMap;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("optionListId", optionListId).add("name", name).add("code", code).toString();
	}
}
