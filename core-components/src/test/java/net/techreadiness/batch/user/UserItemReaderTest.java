package net.techreadiness.batch.user;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UserItemReaderTest {
	@Inject
	@Named("userReader")
	private ItemStreamReader<UserData> userReader;

	@After
	public void after() {
		userReader.close();
	}

	@Test
	public void testNoDates() throws Exception {
		userReader.open(new ExecutionContext());
		UserData user = userReader.read();
		Assert.assertEquals("c", user.getAction());
		Assert.assertEquals("tester@email.com", user.getUser().getUsername());
		Assert.assertEquals("Paul", user.getUser().getFirstName());
		Assert.assertEquals("Adams", user.getUser().getLastName());
		Assert.assertEquals(null, user.getUser().getEmail());
		MatcherAssert.assertThat("12", Matchers.isIn(user.getOrgCodes()));
		MatcherAssert.assertThat("34", Matchers.isIn(user.getOrgCodes()));
		MatcherAssert.assertThat("56", Matchers.isIn(user.getOrgCodes()));
		MatcherAssert.assertThat("67", Matchers.isIn(user.getRoleCodes()));
		MatcherAssert.assertThat("78", Matchers.isIn(user.getRoleCodes()));
		MatcherAssert.assertThat("89", Matchers.isIn(user.getRoleCodes()));
		Assert.assertNull(user.getUser().getDisableDate());
	}

	@Test
	public void testDates() throws Exception {
		ExecutionContext context = new ExecutionContext();
		context.putInt("FlatFileItemReader.read.count", 1);
		userReader.open(context);
		UserData user = userReader.read();
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2012, 0, 1);
		Assert.assertEquals(cal.getTime(), user.getUser().getActiveBeginDate());

		cal.clear();
		cal.set(2012, 0, 31);
		Assert.assertEquals(cal.getTime(), user.getUser().getActiveEndDate());

		Assert.assertEquals(3, user.getLineNumber());
		Assert.assertNotNull(user.getRawData());
	}

	@Test
	public void testDisabled() throws Exception {
		ExecutionContext context = new ExecutionContext();
		context.putInt("FlatFileItemReader.read.count", 2);
		userReader.open(context);
		UserData user = userReader.read();

		Assert.assertNotNull(user.getUser().getDisableDate());

		Assert.assertEquals(4, user.getLineNumber());
		Assert.assertNotNull(user.getRawData());
	}
}
