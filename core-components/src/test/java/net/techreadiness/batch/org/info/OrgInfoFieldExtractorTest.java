package net.techreadiness.batch.org.info;

import net.techreadiness.batch.CoreFieldExtractor;
import net.techreadiness.batch.org.OrgData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class OrgInfoFieldExtractorTest {

	private static CoreFieldExtractor<OrgData> fieldExtractor;
	private OrgData orgData;

	@BeforeClass
	public static void setupClass() {
		fieldExtractor = new CoreFieldExtractor<>();
		fieldExtractor.setResource(new ClassPathResource("net/techreadiness/batch/org-info/org-info-export.txt"));
	}

	@Before
	public void setup() {
		orgData = new OrgData();
	}

	@Test
	public void testLocalCode() {
		String localCode = "localCode";
		orgData.getOrg().setLocalCode(localCode);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals(localCode, fields[0]);
	}

	@Test
	public void testStateCode() {
		String stateCode = "stateCode";
		orgData.getOrg().setState(stateCode);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals(stateCode, fields[1]);
	}

	@Test
	public void testinternetSpeed() {
		String internetSpeed = "100";
		orgData.getOrg().setInternetSpeed(internetSpeed);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("100", fields[2]);
	}

	@Test
	public void testNetworkSpeed() {
		orgData.getOrg().setNetworkSpeed("100");
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("100", fields[3]);
	}

	@Test
	public void testInternetUtilization() {
		String internetUtilization = "100";
		orgData.getOrg().setInternetUtilization(internetUtilization);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("100", fields[4]);
	}

	@Test
	public void testNetworkUtilization() {
		String networkUtilization = "100";
		orgData.getOrg().setNetworkUtilization(networkUtilization);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("100", fields[5]);
	}

	@Test
	public void testWirelessDeviceCount() {
		String wirelessAccessPoints = "100";
		orgData.getOrg().setWirelessAccessPoints(wirelessAccessPoints);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("100", fields[6]);
	}

	@Test
	public void testsimultaneousTesters() {
		String simultaneousTesters = "5";
		orgData.getOrg().setSimultaneousTesters(simultaneousTesters);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("5", fields[7]);
	}

	@Test
	public void testStudentCount() {
		String studentCount = "120";
		orgData.getOrg().setStudentCount(studentCount);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("120", fields[8]);
	}

	@Test
	public void testtestingWindowLength() {
		String testingWindowLength = "5";
		orgData.getOrg().setTestingWindowLength(testingWindowLength);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("5", fields[9]);
	}

	@Test
	public void sessionsPerDay() {
		String sessionsPerDay = "4";
		orgData.getOrg().setSessionsPerDay(sessionsPerDay);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("4", fields[10]);
	}

	@Test
	public void dataEntryComplete() {
		String dataEntryComplete = "dataEntryComplete";
		orgData.getOrg().setDataEntryComplete(dataEntryComplete);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("dataEntryComplete", fields[11]);
	}

	@Test
	public void surveyAdminCount() {
		String surveyAdminCount = "6";
		orgData.getOrg().setSurveyAdminCount(surveyAdminCount);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("6", fields[12]);
	}

	@Test
	public void surveyAdminUnderstanding() {
		String surveyAdminUnderstanding = "6";
		orgData.getOrg().setSurveyAdminUnderstanding(surveyAdminUnderstanding);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("6", fields[13]);
	}

	@Test
	public void surveyAdminTraining() {
		String surveyAdminTraining = "5";
		orgData.getOrg().setSurveyAdminTraining(surveyAdminTraining);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("5", fields[14]);
	}

	@Test
	public void surveyTechstaffCount() {
		String surveyTechstaffCount = "5";
		orgData.getOrg().setSurveyTechstaffCount(surveyTechstaffCount);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("5", fields[15]);
	}

	@Test
	public void surveyTechstaffUnderstanding() {
		String surveyTechstaffUnderstanding = "7";
		orgData.getOrg().setSurveyTechstaffUnderstanding(surveyTechstaffUnderstanding);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("7", fields[16]);
	}

	@Test
	public void surveyTechstaffTraining() {
		String surveyTechstaffTraining = "7";
		orgData.getOrg().setSurveyTechstaffTraining(surveyTechstaffTraining);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("7", fields[17]);

	}

	@Test
	public void enrollmentCountK() {
		String enrollmentCountK = "52";
		orgData.getOrg().setEnrollmentCountK(enrollmentCountK);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("52", fields[18]);
	}

	@Test
	public void enrollmentCount1() {
		String enrollmentCount1 = "50";
		orgData.getOrg().setEnrollmentCount1(enrollmentCount1);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("50", fields[19]);
	}

	@Test
	public void enrollmentCount2() {
		String enrollmentCount2 = "60";
		orgData.getOrg().setEnrollmentCount2(enrollmentCount2);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("60", fields[20]);
	}

	@Test
	public void enrollmentCount3() {
		String enrollmentCount3 = "54";
		orgData.getOrg().setEnrollmentCount3(enrollmentCount3);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("54", fields[21]);
	}

	public void enrollmentCount4() {
		String enrollmentCount4 = "50";
		orgData.getOrg().setEnrollmentCount4(enrollmentCount4);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("50", fields[22]);
	}

	public void enrollmentCount5() {
		String enrollmentCount5 = "53";
		orgData.getOrg().setEnrollmentCount5(enrollmentCount5);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("53", fields[23]);
	}

	public void enrollmentCount6() {
		String enrollmentCount6 = "49";
		orgData.getOrg().setEnrollmentCount6(enrollmentCount6);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("49", fields[24]);
	}

	public void enrollmentCount7() {
		String enrollmentCount7 = "0";
		orgData.getOrg().setEnrollmentCount7(enrollmentCount7);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("0", fields[25]);
	}

	public void enrollmentCount8() {
		String enrollmentCount8 = "0";
		orgData.getOrg().setEnrollmentCount8(enrollmentCount8);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("0", fields[26]);
	}

	public void enrollmentCount9() {
		String enrollmentCount9 = "0";
		orgData.getOrg().setEnrollmentCount9(enrollmentCount9);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("0", fields[27]);
	}

	public void enrollmentCount10() {
		String enrollmentCount10 = "0";
		orgData.getOrg().setEnrollmentCount10(enrollmentCount10);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("0", fields[28]);
	}

	public void enrollmentCount11() {
		String enrollmentCount11 = "0";
		orgData.getOrg().setEnrollmentCount11(enrollmentCount11);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("0", fields[29]);
	}

	public void enrollmentCount12() {
		String enrollmentCount12 = "0";
		orgData.getOrg().setEnrollmentCount12(enrollmentCount12);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals("0", fields[30]);
	}

	public void schoolType() {
		String schoolType = "schoolType";
		orgData.getOrg().setSchoolType(schoolType);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals(schoolType, fields[31]);
	}

	public void testExtAttr(String code, int index) {
		orgData.getOrg().getExtendedAttributes().put(code, code);
		Object[] fields = fieldExtractor.extract(orgData);

		Assert.assertEquals(code, fields[index]);
	}
}
