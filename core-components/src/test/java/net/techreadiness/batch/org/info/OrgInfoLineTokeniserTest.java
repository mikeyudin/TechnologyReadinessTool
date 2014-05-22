package net.techreadiness.batch.org.info;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.file.transform.FieldSet;

public class OrgInfoLineTokeniserTest {

	private OrgInfoLineTokenizer tokenizer;
	private FieldSet fieldSet;

	@Before
	public void setup() {
		tokenizer = new OrgInfoLineTokenizer();
		fieldSet = tokenizer
				.tokenize("code,state,estInetBandwith,estInternalInetBandwidth,estInetBandwidthUtilization,estNetUtil,accessPointCount,maxNumTesters,estTestTakerCount,testWindow,sessionsPerDay,complete,q1,q2,q3,q4,q5,q6,1,1,1,1,1,1,1,1,1,1,1,1,1,Public School");
	}

	@Test
	public void testCode() {
		test("code", 0);
	}

	@Test
	public void testStateCode() {
		test("state", 1);
	}

	@Test
	public void testEstInetBandwidth() {
		test("estInetBandwith", 2);
	}

	@Test
	public void testEstInternNetBandwidth() {
		test("estInternalInetBandwidth", 3);
	}

	@Test
	public void testEstInetBandwidthUtil() {
		test("estInetBandwidthUtilization", 4);
	}

	@Test
	public void testEstNetUtil() {
		test("estNetUtil", 5);
	}

	@Test
	public void testAccessPointCount() {
		test("accessPointCount", 6);
	}

	@Test
	public void testMaxNumTesters() {
		test("maxNumTesters", 7);
	}

	@Test
	public void testEstTestTakerCount() {
		test("estTestTakerCount", 8);
	}

	@Test
	public void testTestWindow() {
		test("testWindow", 9);
	}

	@Test
	public void testSessionsPerDay() {
		test("sessionsPerDay", 10);
	}

	@Test
	public void testComplete() {
		test("complete", 11);
	}

	@Test
	public void testQ1() {
		test("q1", 12);
	}

	@Test
	public void testQ2() {
		test("q2", 13);
	}

	@Test
	public void testQ3() {
		test("q3", 14);
	}

	@Test
	public void testQ4() {
		test("q4", 15);
	}

	@Test
	public void testQ5() {
		test("q5", 16);
	}

	@Test
	public void testQ6() {
		test("q6", 17);
	}

	@Test
	public void testEK() {
		test("1", 18);
	}

	@Test
	public void testE1() {
		test("1", 19);
	}

	@Test
	public void testE2() {
		test("1", 20);
	}

	@Test
	public void testE3() {
		test("1", 21);
	}

	@Test
	public void testE4() {
		test("1", 22);
	}

	@Test
	public void testE5() {
		test("1", 23);
	}

	@Test
	public void testE6() {
		test("1", 24);
	}

	@Test
	public void testE7() {
		test("1", 25);
	}

	@Test
	public void testE8() {
		test("1", 26);
	}

	@Test
	public void testE9() {
		test("1", 27);
	}

	@Test
	public void testE10() {
		test("1", 28);
	}

	@Test
	public void testE11() {
		test("1", 29);
	}

	@Test
	public void testE12() {
		test("1", 30);
	}

	@Test
	public void testSchoolType() {
		test("Public School", 31);
	}

	private void test(String expected, int position) {
		String positionValue = fieldSet.readString(position);
		String nameValue = fieldSet.readString(OrgInfoLineTokenizer.COL_NAMES[position]);
		Assert.assertEquals(expected, positionValue);
		Assert.assertEquals(expected, nameValue);
	}
}
