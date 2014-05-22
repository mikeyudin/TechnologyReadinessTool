package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.Set;

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

/**
 * The persistent class for the contact_type database table.
 * 
 */
@Entity
@Table(name = "contact_type")
public class ContactTypeDO extends AuditedBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "contact_type_id")
	private Long contactTypeId;

	private String code;

	@Column(name = "display_order")
	private Integer displayOrder;

	private String name;

	// bi-directional many-to-one association to Contact
	@OneToMany(mappedBy = "contactType")
	private Set<ContactDO> contacts;

	// bi-directional many-to-one association to Scope
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id")
	private ScopeDO scope;

	public ContactTypeDO() {
	}

	public Long getContactTypeId() {
		return contactTypeId;
	}

	public void setContactTypeId(Long contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ContactDO> getContacts() {
		return contacts;
	}

	public void setContacts(Set<ContactDO> contacts) {
		this.contacts = contacts;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contactTypeId == null ? 0 : contactTypeId.hashCode());
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
		if (!(obj instanceof ContactTypeDO)) {
			return false;
		}
		ContactTypeDO other = (ContactTypeDO) obj;
		if (contactTypeId == null) {
			if (other.contactTypeId != null) {
				return false;
			}
		} else if (!contactTypeId.equals(other.contactTypeId)) {
			return false;
		}
		return true;
	}

}