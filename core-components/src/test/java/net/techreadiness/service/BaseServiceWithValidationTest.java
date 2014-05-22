package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.persistence.dao.EntityDAO.EntityTypeCode;
import net.techreadiness.persistence.dao.EntityFieldDAO;
import net.techreadiness.persistence.domain.EntityDataTypeDO;
import net.techreadiness.persistence.domain.EntityFieldDO;
import net.techreadiness.persistence.domain.OptionListDO;
import net.techreadiness.persistence.domain.OptionListValueDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.service.common.ValidationError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/net/techreadiness/service/messageSourceContext.xml" })
public class BaseServiceWithValidationTest {

	private ServiceContext serviceContext;

	private Stub stub;
	@Inject
	private MessageSource messageSource;

	@Mock
	private EntityFieldDAO entityFieldDAO;

	@Before
	public void before() {
		stub = new Stub();
		MockitoAnnotations.initMocks(this);
		stub.setEntityFieldDAO(entityFieldDAO);
		stub.setMessageSource(messageSource);
	}

	private class Stub extends BaseServiceWithValidationImpl {
		// Stub definition
	}

	@Test
	public void testCopyExtFieldsToCoreForNullNumermicFields() {
		ScopeDO scopeDO = new ScopeDO();
		Map<String, String> map = Maps.newHashMap();
		map.put("description", "scopeDesc");
		map.put("name", "scopeName");
		map.put("code", "scopeCode");
		map.put("scopeId", "");

		scopeDO.setExtAttributes(map);

		stub.copyExtFieldsToCore(serviceContext, scopeDO);

		ScopeDO correctDO = new ScopeDO();
		correctDO.setScopeId(null);
		correctDO.setDescription("scopeDesc");
		correctDO.setName("scopeName");
		correctDO.setCode("scopeCode");

		Assert.assertEquals("Both DO's should have a null scopeId!", correctDO, scopeDO);
	}

	@Test
	public void testValidateForIgnoringEmptyFieldLengthValidation() {
		// setup entityfields
		EntityDataTypeDO entityDataTypeDO = new EntityDataTypeDO();
		entityDataTypeDO.setCode("string");

		EntityFieldDO favoriteColorDO = new EntityFieldDO();
		favoriteColorDO.setCode("favoriteColor");
		favoriteColorDO.setEntityDataType(entityDataTypeDO);
		EntityFieldDO emailDO = new EntityFieldDO();
		emailDO.setCode("email");
		emailDO.setEntityDataType(entityDataTypeDO);
		// emailDO.setMinLength(40);
		EntityFieldDO shortNameDO = new EntityFieldDO();
		shortNameDO.setCode("shortName");
		shortNameDO.setEntityDataType(entityDataTypeDO);
		EntityFieldDO eyeColorDO = new EntityFieldDO();
		eyeColorDO.setCode("eyeColor");
		eyeColorDO.setEntityDataType(entityDataTypeDO);
		eyeColorDO.setMinLength(40);// should ignore this and not report an error

		List<EntityFieldDO> entityList = Lists.newArrayList();
		entityList.add(favoriteColorDO);
		entityList.add(emailDO);
		entityList.add(shortNameDO);
		entityList.add(eyeColorDO);

		Map<String, String> extAttributes = Maps.newHashMap();
		extAttributes.put("favoriteColor", "blue");
		extAttributes.put("email", "gonzo@sesamestreet.com");
		extAttributes.put("shortName", "bob");
		extAttributes.put("eyeColor", "");

		// setup mocks
		Mockito.when(entityFieldDAO.findByScopePathAndType(Matchers.any(Long.class), Matchers.eq(EntityTypeCode.ORG)))
				.thenReturn(entityList);
		List<ValidationError> errors = Lists.newArrayList();
		try {
			errors = stub.performValidation(extAttributes, Long.valueOf(4), EntityTypeCode.ORG);
		} finally {
			Assert.assertEquals("No errors should be returned from this test!", 0, errors.size());
		}
	}

	@Test
	public void testValidateForMaxLength() {
		// setup entityfields
		EntityDataTypeDO entityDataTypeDO = new EntityDataTypeDO();
		entityDataTypeDO.setCode("string");

		EntityFieldDO favoriteColorDO = new EntityFieldDO();
		favoriteColorDO.setCode("favoriteColor");
		favoriteColorDO.setEntityDataType(entityDataTypeDO);
		EntityFieldDO emailDO = new EntityFieldDO();
		emailDO.setCode("email");
		emailDO.setName("Email");
		emailDO.setEntityDataType(entityDataTypeDO);
		emailDO.setMaxLength(5);// should trigger a single error of max length type
		EntityFieldDO shortNameDO = new EntityFieldDO();
		shortNameDO.setCode("shortName");
		shortNameDO.setEntityDataType(entityDataTypeDO);
		EntityFieldDO eyeColorDO = new EntityFieldDO();
		eyeColorDO.setCode("eyeColor");
		eyeColorDO.setEntityDataType(entityDataTypeDO);
		eyeColorDO.setMinLength(40);// should ignore this and not report an error

		List<EntityFieldDO> entityList = Lists.newArrayList();
		entityList.add(favoriteColorDO);
		entityList.add(emailDO);
		entityList.add(shortNameDO);
		entityList.add(eyeColorDO);

		Map<String, String> extAttributes = Maps.newHashMap();
		extAttributes.put("favoriteColor", "blue");
		extAttributes.put("email", "gonzo@sesamestreet.com");
		extAttributes.put("shortName", "bob");
		extAttributes.put("eyeColor", "");

		// setup mocks
		Mockito.when(entityFieldDAO.findByScopePathAndType(Matchers.any(Long.class), Matchers.eq(EntityTypeCode.ORG)))
				.thenReturn(entityList);
		List<ValidationError> errors = Lists.newArrayList();
		try {
			errors = stub.performValidation(extAttributes, Long.valueOf(4), EntityTypeCode.ORG);
		} catch (Exception e) {
			// Handle error validation in finally clause
		} finally {
			Assert.assertEquals("Only 1 error should return from this test case!", 1, errors.size());
			Assert.assertTrue("Wrong error message found! Should be maximum length error string!", errors.get(0)
					.getOnlineMessage().contains("is longer than the maximum of"));// check it contains the right string
		}
	}

	@Test
	public void testValidateForMinLength() {
		// setup entityfields
		EntityDataTypeDO entityDataTypeDO = new EntityDataTypeDO();
		entityDataTypeDO.setCode("string");

		EntityFieldDO favoriteColorDO = new EntityFieldDO();
		favoriteColorDO.setCode("favoriteColor");
		favoriteColorDO.setEntityDataType(entityDataTypeDO);
		EntityFieldDO emailDO = new EntityFieldDO();
		emailDO.setCode("email");
		emailDO.setName("Email");
		emailDO.setEntityDataType(entityDataTypeDO);
		emailDO.setMinLength(25);// should trigger a single error of min length type
		EntityFieldDO shortNameDO = new EntityFieldDO();
		shortNameDO.setCode("shortName");
		shortNameDO.setEntityDataType(entityDataTypeDO);
		EntityFieldDO eyeColorDO = new EntityFieldDO();
		eyeColorDO.setCode("eyeColor");
		eyeColorDO.setEntityDataType(entityDataTypeDO);
		eyeColorDO.setMinLength(40);// should ignore this and not report an error

		List<EntityFieldDO> entityList = Lists.newArrayList();
		entityList.add(favoriteColorDO);
		entityList.add(emailDO);
		entityList.add(shortNameDO);
		entityList.add(eyeColorDO);

		Map<String, String> extAttributes = Maps.newHashMap();
		extAttributes.put("favoriteColor", "blue");
		extAttributes.put("email", "gonzo@sesamestreet.com");
		extAttributes.put("shortName", "bob");
		extAttributes.put("eyeColor", "");

		// setup mocks
		Mockito.when(entityFieldDAO.findByScopePathAndType(Matchers.any(Long.class), Matchers.eq(EntityTypeCode.ORG)))
				.thenReturn(entityList);
		List<ValidationError> errors = Lists.newArrayList();
		try {
			errors = stub.performValidation(extAttributes, Long.valueOf(4), EntityTypeCode.ORG);
		} finally {
			Assert.assertEquals("Only 1 error should return from this test case!", 1, errors.size());
			Assert.assertTrue("Wrong error message found! Should be minimum length error string!", errors.get(0)
					.getOnlineMessage().contains("does not meet the minimum length of"));// check it contains the right
																							// string
		}
	}

	/**
	 * Previously there were error reporting issues when numeric fields weren't numeric. Validation would return a
	 * non-numeric error and a RegEx mismatch error. The code has been altered to skip the regEx test if it fails initial
	 * numeric check. This unit test verifies that only 1 error returns in that scenario. It also verifies that the correct
	 * string appears in the message.
	 */
	@Test
	public void testNonNumericFailureOnlyReturnsOneError() {
		// setup entityfields
		EntityDataTypeDO numberType = new EntityDataTypeDO();
		numberType.setCode("number");

		EntityFieldDO fakeNumberDO = new EntityFieldDO();
		fakeNumberDO.setCode("fakeNumber");
		fakeNumberDO.setEntityDataType(numberType);

		List<EntityFieldDO> entityList = Lists.newArrayList();
		entityList.add(fakeNumberDO);

		Map<String, String> extAttributes = Maps.newHashMap();
		extAttributes.put("fakeNumber", "CHARACTERS!!!!");

		// setup mocks
		Mockito.when(entityFieldDAO.findByScopePathAndType(Matchers.any(Long.class), Matchers.eq(EntityTypeCode.ORG)))
				.thenReturn(entityList);
		List<ValidationError> errors = Lists.newArrayList();
		try {
			errors = stub.performValidation(extAttributes, Long.valueOf(4), EntityTypeCode.ORG);
		} finally {
			Assert.assertEquals("Only 1 error should return from this test case!", 1, errors.size());// single error in the
																										// list and it's the
																										// non-numeric error
			Assert.assertTrue("Wrong error message found! Should be a not numeric error string!", errors.get(0)
					.getOnlineMessage().contains("is not numeric"));// check it contains the right string
		}
	}

	/**
	 * Previously there were error reporting issues when numeric fields weren't numeric. Validation would return a
	 * non-numeric error and a RegEx mismatch error. The code has been altered to skip the regEx test if it fails initial
	 * numeric check. This unit test verifies that only 1 error returns in that scenario. It also verifies that the correct
	 * string appears in the message.
	 */
	@Test
	public void testNumericEntityType() {
		// setup entityfields
		EntityDataTypeDO numberType = new EntityDataTypeDO();
		numberType.setCode("number");

		EntityFieldDO fakeNumberDO = new EntityFieldDO();
		fakeNumberDO.setCode("fakeNumber");
		fakeNumberDO.setEntityDataType(numberType);

		List<EntityFieldDO> entityList = Lists.newArrayList();
		entityList.add(fakeNumberDO);

		Map<String, String> extAttributes = Maps.newHashMap();
		extAttributes.put("fakeNumber", "1");

		// setup mocks
		Mockito.when(entityFieldDAO.findByScopePathAndType(Matchers.any(Long.class), Matchers.eq(EntityTypeCode.ORG)))
				.thenReturn(entityList);
		List<ValidationError> errors = Lists.newArrayList();
		errors = stub.performValidation(extAttributes, Long.valueOf(4), EntityTypeCode.ORG);
		Assert.assertEquals("No errors expected for this test case!", 0, errors.size());// single error in the list and it's
																						// the

		extAttributes.put("fakeNumber", "");
		errors = stub.performValidation(extAttributes, Long.valueOf(4), EntityTypeCode.ORG);
		Assert.assertEquals("No errors expected for this test case!", 0, errors.size());// single error in the list and it's
																						// the

		extAttributes.put("fakeNumber", null);
		errors = stub.performValidation(extAttributes, Long.valueOf(4), EntityTypeCode.ORG);
		Assert.assertEquals("No errors expected for this test case!", 0, errors.size());// single error in the list and it's
																						// the
	}

	@Test
	public void testOptionNotInList() {
		EntityFieldDO field = new EntityFieldDO();
		EntityDataTypeDO dataType = new EntityDataTypeDO();
		dataType.setCode("string");
		field.setEntityDataType(dataType);
		field.setCode("optionList");
		field.setName("Option List Test");
		OptionListDO optionList = new OptionListDO();
		List<OptionListValueDO> optionListValues = Lists.newArrayList();
		OptionListValueDO value1 = new OptionListValueDO();
		value1.setName("Value 1");
		value1.setValue("value1");
		optionListValues.add(value1);
		optionList.setOptionListValues(optionListValues);
		field.setOptionList(optionList);

		List<EntityFieldDO> entityList = Lists.newArrayList();
		entityList.add(field);

		Mockito.when(entityFieldDAO.findByScopePathAndType(Matchers.any(Long.class), Matchers.eq(EntityTypeCode.ORG)))
				.thenReturn(entityList);

		Map<String, String> extAttr = Maps.newHashMap();
		extAttr.put(field.getCode(), "n2");

		List<ValidationError> errors = stub.performValidation(extAttr, Long.valueOf(2), EntityTypeCode.ORG);
		Assert.assertEquals("There should be one error because the ext value was not a part of the option list.", 1,
				errors.size());

		extAttr.put(field.getCode(), "value1");
		errors = stub.performValidation(extAttr, Long.valueOf(2), EntityTypeCode.ORG);
		Assert.assertEquals("There should not be errors because the ext value was a part of the option list.", 0,
				errors.size());

		extAttr.put(field.getCode(), "vAlUe1");
		errors = stub.performValidation(extAttr, Long.valueOf(2), EntityTypeCode.ORG);
		Assert.assertEquals("There should not be errors because the ext value was a part of the option list.", 0,
				errors.size());
		Assert.assertEquals("The entity field value should be the case of the option list value.", value1.getValue(),
				extAttr.get(field.getCode()));
	}
}
