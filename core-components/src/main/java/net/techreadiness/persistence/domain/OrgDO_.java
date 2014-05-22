package net.techreadiness.persistence.domain;

import java.util.Date;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OrgDO.class)
public abstract class OrgDO_ {

	public static volatile ListAttribute<OrgDO, OrgDO> orgs;
	public static volatile SingularAttribute<OrgDO, OrgTypeDO> orgType;
	public static volatile ListAttribute<OrgDO, OrgTreeDO> orgTrees;
	public static volatile SingularAttribute<OrgDO, String> phoneExtension;
	public static volatile SingularAttribute<OrgDO, ScopeDO> scope;
	public static volatile SingularAttribute<OrgDO, OrgDO> parentOrg;
	public static volatile ListAttribute<OrgDO, OrgTreeDO> ancestorOrgTrees;
	public static volatile SingularAttribute<OrgDO, String> state;
	public static volatile SingularAttribute<OrgDO, String> code;
	public static volatile SingularAttribute<OrgDO, String> addressLine2;
	public static volatile SingularAttribute<OrgDO, String> addressLine1;
	public static volatile SingularAttribute<OrgDO, String> localCode;
	public static volatile SingularAttribute<OrgDO, String> city;
	public static volatile SingularAttribute<OrgDO, Long> orgId;
	public static volatile SingularAttribute<OrgDO, Boolean> inactive;
	public static volatile ListAttribute<OrgDO, OrgPartDO> orgParts;
	public static volatile SingularAttribute<OrgDO, String> zip;
	public static volatile SingularAttribute<OrgDO, String> fax;
	public static volatile SingularAttribute<OrgDO, String> phone;
	public static volatile SingularAttribute<OrgDO, String> name;
	public static volatile SingularAttribute<OrgDO, String> dataEntryComplete;
	public static volatile SingularAttribute<OrgDO, Date> dataEntryCompleteDate;
	public static volatile SingularAttribute<OrgDO, String> dataEntryCompleteUser;
	public static volatile SingularAttribute<OrgDO, String> schoolType;
	public static volatile SingularAttribute<OrgDO, String> ncesCode;
	public static volatile SingularAttribute<OrgDO, Integer> surveyTechstaffCount;
	public static volatile SingularAttribute<OrgDO, Integer> surveyTechstaffUnderstanding;
	public static volatile SingularAttribute<OrgDO, Integer> surveyTechstaffTraining;
	public static volatile SingularAttribute<OrgDO, Integer> surveyAdminCount;
	public static volatile SingularAttribute<OrgDO, Integer> surveyAdminUnderstanding;
	public static volatile SingularAttribute<OrgDO, Integer> surveyAdminTraining;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCountK;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount1;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount2;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount3;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount4;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount5;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount6;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount7;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount8;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount9;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount10;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount11;
	public static volatile SingularAttribute<OrgDO, Integer> enrollmentCount12;
	public static volatile SingularAttribute<OrgDO, Integer> studentCount;
	public static volatile SingularAttribute<OrgDO, Integer> wirelessAccessPoints;
	public static volatile SingularAttribute<OrgDO, Integer> simultaneousTesters;
	public static volatile SingularAttribute<OrgDO, Integer> sessionsPerDay;
	public static volatile SingularAttribute<OrgDO, Integer> testingWindowLength;
	public static volatile SingularAttribute<OrgDO, Integer> internetSpeed;
	public static volatile SingularAttribute<OrgDO, Integer> internetUtilization;
	public static volatile SingularAttribute<OrgDO, Integer> networkSpeed;
	public static volatile SingularAttribute<OrgDO, Integer> networkUtilization;
	public static volatile ListAttribute<OrgDO, OrgExtDO> orgExts;
	public static volatile SetAttribute<OrgDO, ContactDO> contacts;
	public static volatile SetAttribute<OrgDO, UserOrgDO> userOrgs;
	public static volatile SetAttribute<OrgDO, DeviceDO> devices;
	public static volatile SetAttribute<OrgDO, FileDO> files;

}
