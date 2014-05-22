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
import net.techreadiness.persistence.BaseEntity;
import net.techreadiness.persistence.ServiceObjectMapped;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.Contact;

/**
 * The persistent class for the contact database table.
 * 
 */
@Entity
@Table(name = "contact")
public class ContactDO extends AuditedBaseEntity implements Serializable, ServiceObjectMapped {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "contact_id")
	private Long contactId;

	@Column(name = "address_line_1")
	private String addressLine1;

	@Column(name = "address_line_2")
	private String addressLine2;

	private String city;

	private String country;

	private String email;

	private String fax;

	private String name;

	private String phone;

	@Column(name = "phone_extension")
	private String phoneExtension;

	private String state;

	private String title;

	private String zip;

	// bi-directional many-to-one association to ContactType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contact_type_id")
	private ContactTypeDO contactType;

	// bi-directional many-to-one association to OrgPart
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
	private OrgDO org;

	public ContactDO() {
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoneExtension() {
		return phoneExtension;
	}

	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public ContactTypeDO getContactType() {
		return contactType;
	}

	public void setContactType(ContactTypeDO contactType) {
		this.contactType = contactType;
	}

	public OrgDO getOrg() {
		return org;
	}

	public void setOrg(OrgDO org) {
		this.org = org;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contactId == null ? 0 : contactId.hashCode());
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
		if (!(obj instanceof ContactDO)) {
			return false;
		}
		ContactDO other = (ContactDO) obj;
		if (contactId == null) {
			if (other.contactId != null) {
				return false;
			}
		} else if (!contactId.equals(other.contactId)) {
			return false;
		}
		return true;
	}

	@Override
	public Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType() {
		return Contact.class;
	}
}