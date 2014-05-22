package net.techreadiness.service.object;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.OrgDO;

import com.google.common.base.Objects;

@XmlRootElement(name = "org")
public class Org extends BaseObjectWithExts<OrgDO> {
	private static final long serialVersionUID = 1L;

	@CoreField
	private Long orgId;
	@CoreField
	private String addressLine1;
	@CoreField
	private String addressLine2;
	@CoreField
	private String city;
	@CoreField
	private String code;
	@CoreField
	private String fax;
	@CoreField
	private Boolean inactive;
	@CoreField
	private String localCode;
	@CoreField
	private String name;
	@CoreField
	private String phoneExtension;
	@CoreField
	private String phone;
	@CoreField
	private String zip;
	@CoreField
	private String state;
	@CoreField
	private String dataEntryComplete;
	@CoreField
	private Date dataEntryCompleteDate;
	@CoreField
	private String dataEntryCompleteUser;
	@CoreField
	private String schoolType;
	@CoreField
	private String ncesCode;
	@CoreField
	private String surveyTechstaffCount;
	@CoreField
	private String surveyTechstaffUnderstanding;
	@CoreField
	private String surveyTechstaffTraining;
	@CoreField
	private String surveyAdminCount;
	@CoreField
	private String surveyAdminUnderstanding;
	@CoreField
	private String surveyAdminTraining;
	@CoreField
	private String enrollmentCountK;
	@CoreField
	private String enrollmentCount1;
	@CoreField
	private String enrollmentCount2;
	@CoreField
	private String enrollmentCount3;
	@CoreField
	private String enrollmentCount4;
	@CoreField
	private String enrollmentCount5;
	@CoreField
	private String enrollmentCount6;
	@CoreField
	private String enrollmentCount7;
	@CoreField
	private String enrollmentCount8;
	@CoreField
	private String enrollmentCount9;
	@CoreField
	private String enrollmentCount10;
	@CoreField
	private String enrollmentCount11;
	@CoreField
	private String enrollmentCount12;
	@CoreField
	private String studentCount;
	@CoreField
	private String wirelessAccessPoints;
	@CoreField
	private String simultaneousTesters;
	@CoreField
	private String sessionsPerDay;
	@CoreField
	private String testingWindowLength;
	@CoreField
	private String internetSpeed;
	@CoreField
	private String internetUtilization;
	@CoreField
	private String networkSpeed;
	@CoreField
	private String networkUtilization;

	// extra mapped information
	private Scope scope;

	private Long parentOrgId;
	private String parentOrgCode;
	private String parentOrgName;
	private String parentOrgLocalCode;

	private Long orgTypeId;
	private String orgTypeCode;
	private String orgTypeName;
	private boolean orgTypeAllowDevice = false;

	public Org() { // required by JAXB
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("orgId", orgId).add("name", name).toString();
	}

	@Override
	public Class<OrgDO> getBaseEntityType() {
		return OrgDO.class;
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

	public Boolean getInactive() {
		return inactive;
	}

	public void setInactive(Boolean inactive) {
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

	public Long getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(Long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public String getParentOrgCode() {
		return parentOrgCode;
	}

	public void setParentOrgCode(String parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}

	public Long getOrgTypeId() {
		return orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId) {
		this.orgTypeId = orgTypeId;
	}

	public String getOrgTypeName() {
		return orgTypeName;
	}

	public void setOrgTypeName(String orgTypeName) {
		this.orgTypeName = orgTypeName;
	}

	public String getOrgTypeCode() {
		return orgTypeCode;
	}

	public void setOrgTypeCode(String orgTypeCode) {
		this.orgTypeCode = orgTypeCode;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Org)) {
			return false;
		}
		Org other = (Org) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		if (scope == null) {
			if (other.scope != null) {
				return false;
			}
		} else if (!scope.equals(other.scope)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (code == null ? 0 : code.hashCode());
		result = prime * result + (scope == null ? 0 : scope.hashCode());
		return result;
	}

	@Override
	public Long getId() {
		return orgId;
	}

	public String getParentOrgLocalCode() {
		return parentOrgLocalCode;
	}

	public void setParentOrgLocalCode(String parentOrgLocalCode) {
		this.parentOrgLocalCode = parentOrgLocalCode;
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

	public boolean isOrgTypeAllowDevice() {
		return orgTypeAllowDevice;
	}

	public void setOrgTypeAllowDevice(boolean orgTypeAllowDevice) {
		this.orgTypeAllowDevice = orgTypeAllowDevice;
	}
}
