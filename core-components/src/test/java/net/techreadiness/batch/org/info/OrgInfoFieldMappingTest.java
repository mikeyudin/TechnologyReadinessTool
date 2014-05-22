package net.techreadiness.batch.org.info;

import java.io.IOException;
import java.util.Properties;

import net.techreadiness.batch.Binder;
import net.techreadiness.batch.CoreFieldSetMapper;
import net.techreadiness.batch.org.OrgData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class OrgInfoFieldMappingTest {
	private CoreFieldSetMapper<OrgData> mapper;
	private OrgData orgData;
	private OrgInfoLineTokenizer tokenizer;
	private FieldSet fieldSet;

	@Before
	public void setup() throws IOException, BindException {
		Properties mappings = new Properties();
		mappings.load(getClass().getResourceAsStream("/net/techreadiness/batch/org-info/org-info-import.properties"));
		mapper = new CoreFieldSetMapper<>(OrgData.class);
		mapper.setBinder(new Binder<OrgData>(mappings));
		tokenizer = new OrgInfoLineTokenizer();
		fieldSet = tokenizer
				.tokenize("code,state,100,101,102,103,10,50,40,5,4,complete,1,2,3,4,5,6,1,11,21,31,41,51,61,71,81,91,101,111,121,schoolType");
		orgData = mapper.mapFieldSet(fieldSet);
	}

	@Test
	public void testCode() {
		Assert.assertEquals("state-code", orgData.getOrg().getCode());
		Assert.assertEquals("100", orgData.getOrg().getInternetSpeed());
		Assert.assertEquals("101", orgData.getOrg().getNetworkSpeed());
		Assert.assertEquals("102", orgData.getOrg().getInternetUtilization());
		Assert.assertEquals("103", orgData.getOrg().getNetworkUtilization());
		Assert.assertEquals("10", orgData.getOrg().getWirelessAccessPoints());
		Assert.assertEquals("50", orgData.getOrg().getSimultaneousTesters());
		Assert.assertEquals("40", orgData.getOrg().getStudentCount());
		Assert.assertEquals("5", orgData.getOrg().getTestingWindowLength());
		Assert.assertEquals("4", orgData.getOrg().getSessionsPerDay());
		Assert.assertEquals(null, orgData.getOrg().getDataEntryComplete());
		Assert.assertEquals("1", orgData.getOrg().getSurveyAdminCount());
		Assert.assertEquals("2", orgData.getOrg().getSurveyAdminUnderstanding());
		Assert.assertEquals("3", orgData.getOrg().getSurveyAdminTraining());
		Assert.assertEquals("4", orgData.getOrg().getSurveyTechstaffCount());
		Assert.assertEquals("5", orgData.getOrg().getSurveyTechstaffUnderstanding());
		Assert.assertEquals("6", orgData.getOrg().getSurveyTechstaffTraining());
		Assert.assertEquals("1", orgData.getOrg().getEnrollmentCountK());
		Assert.assertEquals("11", orgData.getOrg().getEnrollmentCount1());
		Assert.assertEquals("21", orgData.getOrg().getEnrollmentCount2());
		Assert.assertEquals("31", orgData.getOrg().getEnrollmentCount3());
		Assert.assertEquals("41", orgData.getOrg().getEnrollmentCount4());
		Assert.assertEquals("51", orgData.getOrg().getEnrollmentCount5());
		Assert.assertEquals("61", orgData.getOrg().getEnrollmentCount6());
		Assert.assertEquals("71", orgData.getOrg().getEnrollmentCount7());
		Assert.assertEquals("81", orgData.getOrg().getEnrollmentCount8());
		Assert.assertEquals("91", orgData.getOrg().getEnrollmentCount9());
		Assert.assertEquals("101", orgData.getOrg().getEnrollmentCount10());
		Assert.assertEquals("111", orgData.getOrg().getEnrollmentCount11());
		Assert.assertEquals("121", orgData.getOrg().getEnrollmentCount12());
		Assert.assertEquals("schoolType", orgData.getOrg().getSchoolType());
	}
}
