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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.techreadiness.persistence.AuditedBaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the custom_text database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "custom_text")
public class CustomTextDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "custom_text_id", nullable = false)
	private Long customTextId;

	@Size(min = 1, max = 100, message = "{net.techreadiness.persistence.domain.CustomTextDO.code.Size}")
	@NotNull(message = "{net.techreadiness.persistence.domain.CustomTextDO.code.NotNull}")
	@Column(nullable = false)
	private String code;

	@Size(min = 1, max = 20000, message = "{net.techreadiness.persistence.domain.CustomTextDO.text.Size}")
	@NotNull(message = "{net.techreadiness.persistence.domain.CustomTextDO.text.NotNull}")
	@Column(nullable = false)
	private String text;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@NotNull
	private ScopeDO scope;

	public CustomTextDO() {
	}

	public Long getCustomTextId() {
		return customTextId;
	}

	public void setCustomTextId(Long customTextId) {
		this.customTextId = customTextId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

}