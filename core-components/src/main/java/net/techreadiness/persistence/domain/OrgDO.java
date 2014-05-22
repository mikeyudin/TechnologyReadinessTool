package net.techreadiness.persistence.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.techreadiness.persistence.AuditedBaseEntityWithExt;
import net.techreadiness.persistence.BaseEntity;
import net.techreadiness.persistence.ServiceObjectMapped;
import net.techreadiness.service.object.BaseObject;
import net.techreadiness.service.object.Org;

import org.hibernate.annotations.BatchSize;

import com.google.common.base.Objects;

/**
 * The persistent class for the org database table.
 * 
 */
@Entity
@Table(name = "org")
@BatchSize(size = 50)
public class OrgDO extends AuditedBaseEntityWithExt implements Serializable, ServiceObjectMapped {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "org_id", unique = true, nullable = false)
	private Long orgId;

	@Column(name = "address_line1", length = 100)
	private String addressLine1;

	@Column(name = "address_line2", length = 100)
	private String addressLine2;

	@Column(length = 100)
	private String city;

	@Column(nullable = false, length = 50)
	private String code;

	@Column(name = "fax", length = 20)
	private String fax;

	@Column(name = "inactive")
	private boolean inactive;

	@Column(name = "local_code", length = 50)
	private String localCode;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(name = "phone_extension", length = 20)
	private String phoneExtension;

	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "zip", length = 10)
	private String zip;

	@Column(length = 2)
	private String state;

	@Column(name = "data_entry_complete")
	private String dataEntryComplete;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_entry_complete_date")
	private Date dataEntryCompleteDate;

	@Column(name = "data_entry_complete_user")
	private String dataEntryCompleteUser;

	@Column(name = "school_type")
	private String schoolType;

	@Column(name = "nces_code")
	private String ncesCode;

	@Column(name = "survey_techstaff_count")
	private String surveyTechstaffCount;

	@Column(name = "survey_techstaff_understanding")
	private String surveyTechstaffUnderstanding;

	@Column(name = "survey_techstaff_training")
	private String surveyTechstaffTraining;

	@Column(name = "survey_admin_count")
	private String surveyAdminCount;

	@Column(name = "survey_admin_understanding")
	private String surveyAdminUnderstanding;

	@Column(name = "survey_admin_training")
	private String surveyAdminTraining;

	@Column(name = "enrollment_countK")
	private String enrollmentCountK;

	@Column(name = "enrollment_count1")
	private String enrollmentCount1;

	@Column(name = "enrollment_count2")
	private String enrollmentCount2;

	@Column(name = "enrollment_count3")
	private String enrollmentCount3;

	@Column(name = "enrollment_count4")
	private String enrollmentCount4;

	@Column(name = "enrollment_count5")
	private String enrollmentCount5;

	@Column(name = "enrollment_count6")
	private String enrollmentCount6;

	@Column(name = "enrollment_count7")
	private String enrollmentCount7;

	@Column(name = "enrollment_count8")
	private String enrollmentCount8;

	@Column(name = "enrollment_count9")
	private String enrollmentCount9;

	@Column(name = "enrollment_count10")
	private String enrollmentCount10;

	@Column(name = "enrollment_count11")
	private String enrollmentCount11;

	@Column(name = "enrollment_count12")
	private String enrollmentCount12;

	@Column(name = "student_count")
	private String studentCount;

	@Column(name = "wireless_access_points")
	private String wirelessAccessPoints;

	@Column(name = "simultaneous_testers")
	private String simultaneousTesters;

	@Column(name = "sessions_per_day")
	private String sessionsPerDay;

	@Column(name = "testing_window_length")
	private String testingWindowLength;

	@Column(name = "internet_speed")
	private String internetSpeed;

	@Column(name = "internet_utilization")
	private String internetUtilization;

	@Column(name = "network_speed")
	private String networkSpeed;

	@Column(name = "network_utilization")
	private String networkUtilization;

	// bi-directional many-to-one association to OrgDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_org_id")
	private OrgDO parentOrg;

	// bi-directional many-to-one association to OrgDO
	@OneToMany(mappedBy = "parentOrg")
	private List<OrgDO> orgs;

	// bi-directional many-to-one association to OrgTypeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_type_id", nullable = false)
	private OrgTypeDO orgType;

	// bi-directional many-to-one association to ScopeDO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scope_id", nullable = false)
	private ScopeDO scope;

	// bi-directional many-to-one association to OrgExtDO
	@OneToMany(mappedBy = "org", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@BatchSize(size = 50)
	private List<OrgExtDO> orgExts;

	// bi-directional many-to-one association to OrgPartDO
	@OneToMany(mappedBy = "org", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<OrgPartDO> orgParts;

	// bi-directional many-to-one association to OrgTreeDO
	@OneToMany(mappedBy = "org")
	private List<OrgTreeDO> orgTrees;

	// bi-directional many-to-one association to OrgTreeDO
	@OneToMany(mappedBy = "ancestorOrg")
	private List<OrgTreeDO> ancestorOrgTrees;

	@OneToMany(mappedBy = "org")
	private Set<UserOrgDO> userOrgs;

	// bi-directional many-to-one association to Contact
	@OneToMany(mappedBy = "org", cascade = { CascadeType.REMOVE })
	private Set<ContactDO> contacts;

	@OneToMany(mappedBy = "org", cascade = { CascadeType.REMOVE })
	private Set<DeviceDO> devices;

	@OneToMany(mappedBy = "org", cascade = { CascadeType.REMOVE })
	private Set<FileDO> files;

	public OrgDO() {
	}

	@Override
	protected void populateExtAttributes() {
		Map<String, String> map = new HashMap<>();

		if (orgExts != null && orgExts.size() > 0) {
			for (OrgExtDO orgExtDO : orgExts) {
				map.put(orgExtDO.getEntityField().getCode(), orgExtDO.getValue());
			}
		}

		this.setExtAttributes(map);
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public boolean isInactive() {
		return inactive;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneExtension() {
		return phoneExtension;
	}

	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDataEntryComplete() {
		return dataEntryComplete;
	}

	public void setDataEntryComplete(String dataEntryComplete) {
		this.dataEntryComplete = dataEntryComplete;
	}

	public Date getDataEntryCompleteDate() {
		return dataEntryCompleteDate;
	}

	public void setDataEntryCompleteDate(Date dataEntryCompleteDate) {
		this.dataEntryCompleteDate = dataEntryCompleteDate;
	}

	public String getDataEntryCompleteUser() {
		return dataEntryCompleteUser;
	}

	public void setDataEntryCompleteUser(String dataEntryCompleteUser) {
		this.dataEntryCompleteUser = dataEntryCompleteUser;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public String getNcesCode() {
		return ncesCode;
	}

	public void setNcesCode(String ncesCode) {
		this.ncesCode = ncesCode;
	}

	public String getSurveyTechstaffCount() {
		return surveyTechstaffCount;
	}

	public void setSurveyTechstaffCount(String surveyTechstaffCount) {
		this.surveyTechstaffCount = surveyTechstaffCount;
	}

	public String getSurveyTechstaffUnderstanding() {
		return surveyTechstaffUnderstanding;
	}

	public void setSurveyTechstaffUnderstanding(String surveyTechstaffUnderstanding) {
		this.surveyTechstaffUnderstanding = surveyTechstaffUnderstanding;
	}

	public String getSurveyTechstaffTraining() {
		return surveyTechstaffTraining;
	}

	public void setSurveyTechstaffTraining(String surveyTechstaffTraining) {
		this.surveyTechstaffTraining = surveyTechstaffTraining;
	}

	public String getSurveyAdminCount() {
		return surveyAdminCount;
	}

	public void setSurveyAdminCount(String surveyAdminCount) {
		this.surveyAdminCount = surveyAdminCount;
	}

	public String getSurveyAdminUnderstanding() {
		return surveyAdminUnderstanding;
	}

	public void setSurveyAdminUnderstanding(String surveyAdminUnderstanding) {
		this.surveyAdminUnderstanding = surveyAdminUnderstanding;
	}

	public String getSurveyAdminTraining() {
		return surveyAdminTraining;
	}

	public void setSurveyAdminTraining(String surveyAdminTraining) {
		this.surveyAdminTraining = surveyAdminTraining;
	}

	public String getEnrollmentCountK() {
		return enrollmentCountK;
	}

	public void setEnrollmentCountK(String enrollmentCountK) {
		this.enrollmentCountK = enrollmentCountK;
	}

	public String getEnrollmentCount1() {
		return enrollmentCount1;
	}

	public void setEnrollmentCount1(String enrollmentCount1) {
		this.enrollmentCount1 = enrollmentCount1;
	}

	public String getEnrollmentCount2() {
		return enrollmentCount2;
	}

	public void setEnrollmentCount2(String enrollmentCount2) {
		this.enrollmentCount2 = enrollmentCount2;
	}

	public String getEnrollmentCount3() {
		return enrollmentCount3;
	}

	public void setEnrollmentCount3(String enrollmentCount3) {
		this.enrollmentCount3 = enrollmentCount3;
	}

	public String getEnrollmentCount4() {
		return enrollmentCount4;
	}

	public void setEnrollmentCount4(String enrollmentCount4) {
		this.enrollmentCount4 = enrollmentCount4;
	}

	public String getEnrollmentCount5() {
		return enrollmentCount5;
	}

	public void setEnrollmentCount5(String enrollmentCount5) {
		this.enrollmentCount5 = enrollmentCount5;
	}

	public String getEnrollmentCount6() {
		return enrollmentCount6;
	}

	public void setEnrollmentCount6(String enrollmentCount6) {
		this.enrollmentCount6 = enrollmentCount6;
	}

	public String getEnrollmentCount7() {
		return enrollmentCount7;
	}

	public void setEnrollmentCount7(String enrollmentCount7) {
		this.enrollmentCount7 = enrollmentCount7;
	}

	public String getEnrollmentCount8() {
		return enrollmentCount8;
	}

	public void setEnrollmentCount8(String enrollmentCount8) {
		this.enrollmentCount8 = enrollmentCount8;
	}

	public String getEnrollmentCount9() {
		return enrollmentCount9;
	}

	public void setEnrollmentCount9(String enrollmentCount9) {
		this.enrollmentCount9 = enrollmentCount9;
	}

	public String getEnrollmentCount10() {
		return enrollmentCount10;
	}

	public void setEnrollmentCount10(String enrollmentCount10) {
		this.enrollmentCount10 = enrollmentCount10;
	}

	public String getEnrollmentCount11() {
		return enrollmentCount11;
	}

	public void setEnrollmentCount11(String enrollmentCount11) {
		this.enrollmentCount11 = enrollmentCount11;
	}

	public String getEnrollmentCount12() {
		return enrollmentCount12;
	}

	public void setEnrollmentCount12(String enrollmentCount12) {
		this.enrollmentCount12 = enrollmentCount12;
	}

	public String getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(String studentCount) {
		this.studentCount = studentCount;
	}

	public String getWirelessAccessPoints() {
		return wirelessAccessPoints;
	}

	public void setWirelessAccessPoints(String wirelessAccessPoints) {
		this.wirelessAccessPoints = wirelessAccessPoints;
	}

	public String getSimultaneousTesters() {
		return simultaneousTesters;
	}

	public void setSimultaneousTesters(String simultaneousTesters) {
		this.simultaneousTesters = simultaneousTesters;
	}

	public String getSessionsPerDay() {
		return sessionsPerDay;
	}

	public void setSessionsPerDay(String sessionsPerDay) {
		this.sessionsPerDay = sessionsPerDay;
	}

	public String getTestingWindowLength() {
		return testingWindowLength;
	}

	public void setTestingWindowLength(String testingWindowLength) {
		this.testingWindowLength = testingWindowLength;
	}

	public String getInternetSpeed() {
		return internetSpeed;
	}

	public void setInternetSpeed(String internetSpeed) {
		this.internetSpeed = internetSpeed;
	}

	public String getInternetUtilization() {
		return internetUtilization;
	}

	public void setInternetUtilization(String internetUtilization) {
		this.internetUtilization = internetUtilization;
	}

	public String getNetworkSpeed() {
		return networkSpeed;
	}

	public void setNetworkSpeed(String networkSpeed) {
		this.networkSpeed = networkSpeed;
	}

	public String getNetworkUtilization() {
		return networkUtilization;
	}

	public void setNetworkUtilization(String networkUtilization) {
		this.networkUtilization = networkUtilization;

	}

	public OrgDO getParentOrg() {
		return parentOrg;
	}

	public void setParentOrg(OrgDO parentOrg) {
		this.parentOrg = parentOrg;
	}

	public List<OrgDO> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<OrgDO> orgs) {
		this.orgs = orgs;
	}

	public OrgTypeDO getOrgType() {
		return orgType;
	}

	public void setOrgType(OrgTypeDO orgType) {
		this.orgType = orgType;
	}

	public ScopeDO getScope() {
		return scope;
	}

	public void setScope(ScopeDO scope) {
		this.scope = scope;
	}

	public List<OrgExtDO> getOrgExts() {
		return orgExts;
	}

	public void setOrgExts(List<OrgExtDO> orgExts) {
		this.orgExts = orgExts;
	}

	public List<OrgPartDO> getOrgParts() {
		return orgParts;
	}

	public void setOrgParts(List<OrgPartDO> orgParts) {
		this.orgParts = orgParts;
	}

	public List<OrgTreeDO> getOrgTrees() {
		return orgTrees;
	}

	public void setOrgTrees(List<OrgTreeDO> orgTrees) {
		this.orgTrees = orgTrees;
	}

	public List<OrgTreeDO> getAncestorOrgTrees() {
		return ancestorOrgTrees;
	}

	public void setAncestorOrgTrees(List<OrgTreeDO> ancestorOrgTrees) {
		this.ancestorOrgTrees = ancestorOrgTrees;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getOrgId() == null ? 0 : getOrgId().hashCode());
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
		if (obj instanceof Org) {
			return getOrgId().equals(((Org) obj).getOrgId());
		}
		if (!(obj instanceof OrgDO)) {
			return false;
		}
		OrgDO other = (OrgDO) obj;
		if (getOrgId() == null) {
			if (other.getOrgId() != null) {
				return false;
			}
		} else if (!getOrgId().equals(other.getOrgId())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("orgId", getOrgId()).add("name", getName()).toString();
	}

	@Override
	public Class<? extends BaseObject<? extends BaseEntity>> getServiceObjectType() {
		return Org.class;
	}

	public Set<ContactDO> getContacts() {
		return contacts;
	}

	public void setContacts(Set<ContactDO> contacts) {
		this.contacts = contacts;
	}

	public Set<DeviceDO> getDevices() {
		return devices;
	}

	public void setDevices(Set<DeviceDO> devices) {
		this.devices = devices;
	}

	public Set<FileDO> getFiles() {
		return files;
	}

	public void setFiles(Set<FileDO> files) {
		this.files = files;
	}

	public Set<UserOrgDO> getUserOrgs() {
		return userOrgs;
	}

	public void setUserOrgs(Set<UserOrgDO> userOrgs) {
		this.userOrgs = userOrgs;
	}
}