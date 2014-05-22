package net.techreadiness.service.object;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.ContactDO;

import com.google.common.base.Objects;

public class Contact extends BaseObject<ContactDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	Long contactId;
	@CoreField
	String addressLine1;
	@CoreField
	String addressLine2;
	@CoreField
	String city;
	@CoreField
	String country;
	@CoreField
	String email;
	@CoreField
	String fax;
	@CoreField
	String name;
	@CoreField
	String phone;
	@CoreField
	String phoneExtension;
	@CoreField
	String state;
	@CoreField
	String title;
	@CoreField
	String zip;

	Long contactTypeId;
	String contactTypeCode;
	String contactTypeName;

	Org org;

	public Contact() { // required by JAXB
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("contactId", contactId).add("name", name).toString();
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

	public Long getContactTypeId() {
		return contactTypeId;
	}

	public void setContactTypeId(Long contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

	public String getContactTypeCode() {
		return contactTypeCode;
	}

	public void setContactTypeCode(String contactTypeCode) {
		this.contactTypeCode = contactTypeCode;
	}

	public String getContactTypeName() {
		return contactTypeName;
	}

	public void setContactTypeName(String contactTypeName) {
		this.contactTypeName = contactTypeName;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	@Override
	public Class<ContactDO> getBaseEntityType() {
		return ContactDO.class;
	}

	@Override
	public Long getId() {
		return contactId;
	}
}
