package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.techreadiness.persistence.AuditedBaseEntity;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the entity_field database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "entity_field")
public class EntityFieldDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "entity_field_id", unique = true, nullable = false)
	private Long entityFieldId;

	@Column(nullable = false, length = 100)
	private String code;

	@Column(length = 1000)
	private String description;

	@Column(nullable = false)
	private boolean disabled;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(name = "max_length")
	private Integer maxLength;

	@Column(name = "min_length")
	private Integer minLength;

	@Column(nullable = false, length = 200)
	private String name;

	@Column(length = 2000)
	private String regex;

	@Column(name = "regex_display", length = 2000)
	private String regexDisplay;

	@Column(nullable = false)
	private boolean required;

	// bi-directional many-to-one association to EntityDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private EntityDO entity;

	// bi-directional many-to-one association to EntityDataTypeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_data_type_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private EntityDataTypeDO entityDataType;

	// bi-directional many-to-one association to OptionListDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_list_id")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private OptionListDO optionList;

	// bi-directional many-to-one association to OrgExtDO
	@OneToMany(mappedBy = "entityField")
	private List<OrgExtDO> orgExts;

	// bi-directional many-to-one association to OrgPartExtDO
	@OneToMany(mappedBy = "entityField")
	private List<OrgPartExtDO> orgPartExts;

	// bi-directional many-to-one association to ScopeExtDO
	@OneToMany(mappedBy = "entityField")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ScopeExtDO> scopeExts;

	// bi-directional many-to-one association to ViewDefFieldDO
	@OneToMany(mappedBy = "entityField")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<ViewDefFieldDO> viewDefFields;

	public EntityFieldDO() {
	}

	public Long getEntityFieldId() {
		return entityFieldId;
	}

	public void setEntityFieldId(Long entityFieldId) {
		this.entityFieldId = entityFieldId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRegexDisplay() {
		return regexDisplay;
	}

	public void setRegexDisplay(String regexDisplay) {
		this.regexDisplay = regexDisplay;
	}

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public EntityDO getEntity() {
		return entity;
	}

	public void setEntity(EntityDO entity) {
		this.entity = entity;
	}

	public EntityDataTypeDO getEntityDataType() {
		return entityDataType;
	}

	public void setEntityDataType(EntityDataTypeDO entityDataType) {
		this.entityDataType = entityDataType;
	}

	public OptionListDO getOptionList() {
		return optionList;
	}

	public void setOptionList(OptionListDO optionList) {
		this.optionList = optionList;
	}

	public List<OrgExtDO> getOrgExts() {
		return orgExts;
	}

	public void setOrgExts(List<OrgExtDO> orgExts) {
		this.orgExts = orgExts;
	}

	public List<OrgPartExtDO> getOrgPartExts() {
		return orgPartExts;
	}

	public void setOrgPartExts(List<OrgPartExtDO> orgPartExts) {
		this.orgPartExts = orgPartExts;
	}

	public List<ScopeExtDO> getScopeExts() {
		return scopeExts;
	}

	public void setScopeExts(List<ScopeExtDO> scopeExts) {
		this.scopeExts = scopeExts;
	}

	public List<ViewDefFieldDO> getViewDefFields() {
		return viewDefFields;
	}

	public void setViewDefFields(List<ViewDefFieldDO> viewDefFields) {
		this.viewDefFields = viewDefFields;
	}

	public boolean isCore() {
		try {
			String className = getEntity().getEntityType().getJavaClass();
			Class<?> entityClass = Class.forName(className);
			Object instance = entityClass.newInstance();
			return PropertyUtils.isReadable(instance, getCode());
		} catch (Exception e) {
			return false;
		}
	}
}
