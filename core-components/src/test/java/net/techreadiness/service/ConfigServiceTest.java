package net.techreadiness.service;

import net.techreadiness.persistence.dao.ScopeExtDAO;
import net.techreadiness.persistence.domain.ScopeExtDO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ConfigServiceTest {
	@Mock
	private ScopeExtDAO scopeExtDao;
	private ConfigServiceImpl configService;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		configService = new ConfigServiceImpl();
		configService.setScopeExtDao(scopeExtDao);
	}

	@Test
	public void testGetBooleanNull() throws Exception {
		ScopeExtDO value = Mockito.mock(ScopeExtDO.class);
		Mockito.when(value.getValue()).thenReturn(null);
		Mockito.when(scopeExtDao.getLowestExistingConfigurationItem(Matchers.any(Long.class), Matchers.anyString()))
				.thenReturn(null);
		Boolean booleanValue = configService.isBooleanActive(null, null, null);
		Assert.assertFalse("Boolean values should default to false.", booleanValue);
	}

	@Test
	public void testGetBooleanValueTrue() throws Exception {
		ScopeExtDO value = Mockito.mock(ScopeExtDO.class);
		Mockito.when(value.getValue()).thenReturn("true");
		Mockito.when(scopeExtDao.getLowestExistingConfigurationItem(Matchers.any(Long.class), Matchers.anyString()))
				.thenReturn(null);
		Boolean booleanValue = configService.isBooleanActive(null, null, null);
		Assert.assertFalse("Boolean values should return true if ext value is 'true'.", booleanValue);
	}

	@Test
	public void testGetBooleanValueFalse() throws Exception {
		ScopeExtDO value = Mockito.mock(ScopeExtDO.class);
		Mockito.when(value.getValue()).thenReturn("false");
		Mockito.when(scopeExtDao.getLowestExistingConfigurationItem(Matchers.any(Long.class), Matchers.anyString()))
				.thenReturn(null);
		Boolean booleanValue = configService.isBooleanActive(null, null, null);
		Assert.assertFalse("Boolean values should be false if ext value is not 'true'.", booleanValue);
	}

	@Test
	public void testGetBooleanValueUnexpectedText() throws Exception {
		ScopeExtDO value = Mockito.mock(ScopeExtDO.class);
		Mockito.when(value.getValue()).thenReturn("foobar");
		Mockito.when(scopeExtDao.getLowestExistingConfigurationItem(Matchers.any(Long.class), Matchers.anyString()))
				.thenReturn(null);
		Boolean booleanValue = configService.isBooleanActive(null, null, null);
		Assert.assertFalse("Boolean values should be false if ext value is not 'true'.", booleanValue);
	}
}
