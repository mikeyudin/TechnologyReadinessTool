package net.techreadiness.service;

import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;

import net.techreadiness.persistence.dao.EntityDAO;
import net.techreadiness.persistence.dao.EntityFieldDAO;
import net.techreadiness.persistence.dao.ExtDAO;
import net.techreadiness.persistence.domain.EntityFieldDO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgExtDO;
import net.techreadiness.persistence.domain.OrgPartDO;
import net.techreadiness.persistence.domain.OrgPartExtDO;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeExtDO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserExtDO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BaseServiceWithValidationAndExtTest {

	@Mock
	private ExtDAO<ScopeDO, ScopeExtDO> scopeExtDAO;

	@Mock
	private ExtDAO<OrgDO, OrgExtDO> orgExtDAO;

	@Mock
	private ExtDAO<OrgPartDO, OrgPartExtDO> orgPartExtDAO;

	@Mock
	private ExtDAO<UserDO, UserExtDO> userExtDAO;

	@Mock
	private EntityFieldDAO entityFieldDAO;

	private BaseServiceWithValidationAndExt<ScopeDO, ScopeExtDO> baseServiceWithValidationAndExtScopes;
	private BaseServiceWithValidationAndExt<OrgDO, OrgExtDO> baseServiceWithValidationAndExtOrgs;
	private BaseServiceWithValidationAndExt<OrgPartDO, OrgPartExtDO> baseServiceWithValidationAndExtOrgParts;
	private BaseServiceWithValidationAndExt<UserDO, UserExtDO> baseServiceWithValidationAndExtUsers;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		baseServiceWithValidationAndExtScopes = new BaseServiceWithValidationAndExt<>();
		baseServiceWithValidationAndExtOrgs = new BaseServiceWithValidationAndExt<>();
		baseServiceWithValidationAndExtOrgParts = new BaseServiceWithValidationAndExt<>();
		baseServiceWithValidationAndExtUsers = new BaseServiceWithValidationAndExt<>();
	}

	@Test
	public void testStoreExtAttributesForScopeDeletes() {
		baseServiceWithValidationAndExtScopes.setEntityFieldDAO(entityFieldDAO);

		ScopeDO scopeDO = new ScopeDO();
		// 4 fields being passed in
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		exts.put("secret", "");
		// exts.put("magicKey", "false");
		scopeDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		ScopeExtDO superAdmin = new ScopeExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		ScopeExtDO specialCode = new ScopeExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		ScopeExtDO secret = new ScopeExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		ScopeExtDO magicKey = new ScopeExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<ScopeExtDO> mockCurrentList = Lists.newArrayList();
		mockCurrentList.add(superAdmin);
		mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("specialCode"))).thenReturn(specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(scopeExtDAO.getExtDOs(Matchers.any(ScopeDO.class))).thenReturn(mockCurrentList);
		Mockito.when(scopeExtDAO.getNew()).thenReturn(new ScopeExtDO());
		try {
			baseServiceWithValidationAndExtScopes.storeExtFields(context, scopeDO, scopeExtDAO,
					EntityDAO.EntityTypeCode.SCOPE, 1L);
		} finally {
			verify(scopeExtDAO, Mockito.times(2)).delete((ScopeExtDO) Matchers.any());
		}

	}

	@Test
	public void testStoreExtAttributesForScopeUpdates() {
		baseServiceWithValidationAndExtScopes.setEntityFieldDAO(entityFieldDAO);

		ScopeDO scopeDO = new ScopeDO();
		// 4 fields being passed in
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		// exts.put("secret", "password");
		// exts.put("magicKey", "false");
		scopeDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		ScopeExtDO superAdmin = new ScopeExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		ScopeExtDO specialCode = new ScopeExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		ScopeExtDO secret = new ScopeExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		ScopeExtDO magicKey = new ScopeExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<ScopeExtDO> mockCurrentList = Lists.newArrayList();
		mockCurrentList.add(superAdmin);
		mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("specialCode"))).thenReturn(specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(scopeExtDAO.getExtDOs(Matchers.any(ScopeDO.class))).thenReturn(mockCurrentList);
		Mockito.when(scopeExtDAO.getNew()).thenReturn(new ScopeExtDO());
		try {
			baseServiceWithValidationAndExtScopes.storeExtFields(context, scopeDO, scopeExtDAO,
					EntityDAO.EntityTypeCode.SCOPE, 1L);
		} finally {
			verify(scopeExtDAO, Mockito.times(2)).update((ScopeExtDO) Matchers.any());
		}
	}

	@Test
	public void testStoreExtAttributesForScopeCreates() {
		baseServiceWithValidationAndExtScopes.setEntityFieldDAO(entityFieldDAO);

		ScopeDO scopeDO = new ScopeDO();
		// 4 fields being passed in
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		exts.put("secret", "password");
		exts.put("magicKey", "false");
		scopeDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		ScopeExtDO superAdmin = new ScopeExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		ScopeExtDO specialCode = new ScopeExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		ScopeExtDO secret = new ScopeExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		ScopeExtDO magicKey = new ScopeExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<ScopeExtDO> mockCurrentList = Lists.newArrayList();
		mockCurrentList.add(superAdmin);
		mockCurrentList.add(specialCode);
		// mockCurrentList.add(secret);
		// mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("specialCode"))).thenReturn(specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.SCOPE), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(scopeExtDAO.getExtDOs(Matchers.any(ScopeDO.class))).thenReturn(mockCurrentList);
		Mockito.when(scopeExtDAO.getNew()).thenReturn(new ScopeExtDO());
		try {
			baseServiceWithValidationAndExtScopes.storeExtFields(context, scopeDO, scopeExtDAO,
					EntityDAO.EntityTypeCode.SCOPE, 1L);
		} finally {
			verify(scopeExtDAO, Mockito.times(2)).create((ScopeExtDO) Matchers.any());
		}
	}

	@Test
	public void testStoreExtAttributesForOrgDeletes() {
		baseServiceWithValidationAndExtOrgs.setEntityFieldDAO(entityFieldDAO);

		OrgDO orgDO = new OrgDO();
		// 4 fields being passed in
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		exts.put("secret", "");
		// exts.put("magicKey", "false");
		orgDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		OrgExtDO superAdmin = new OrgExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		OrgExtDO specialCode = new OrgExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		OrgExtDO secret = new OrgExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		OrgExtDO magicKey = new OrgExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<OrgExtDO> mockCurrentList = Lists.newArrayList();
		mockCurrentList.add(superAdmin);
		mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("specialCode"))).thenReturn(specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(orgExtDAO.getExtDOs(Matchers.any(OrgDO.class))).thenReturn(mockCurrentList);
		Mockito.when(orgExtDAO.getNew()).thenReturn(new OrgExtDO());
		try {
			baseServiceWithValidationAndExtOrgs.storeExtFields(context, orgDO, orgExtDAO, EntityDAO.EntityTypeCode.ORG, 1L);
		} finally {
			verify(orgExtDAO, Mockito.times(2)).delete((OrgExtDO) Matchers.any());
		}
	}

	@Test
	public void testStoreExtAttributesForOrgUpdates() {
		baseServiceWithValidationAndExtOrgs.setEntityFieldDAO(entityFieldDAO);

		OrgDO orgDO = new OrgDO();
		// 4 fields being passed in
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		// exts.put("secret", "");
		// exts.put("magicKey", "false");
		orgDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		OrgExtDO superAdmin = new OrgExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		OrgExtDO specialCode = new OrgExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		OrgExtDO secret = new OrgExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		OrgExtDO magicKey = new OrgExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<OrgExtDO> mockCurrentList = Lists.newArrayList();
		mockCurrentList.add(superAdmin);
		mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("specialCode"))).thenReturn(specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(orgExtDAO.getExtDOs(Matchers.any(OrgDO.class))).thenReturn(mockCurrentList);
		Mockito.when(orgExtDAO.getNew()).thenReturn(new OrgExtDO());
		try {
			baseServiceWithValidationAndExtOrgs.storeExtFields(context, orgDO, orgExtDAO, EntityDAO.EntityTypeCode.ORG, 1L);
		} finally {
			verify(orgExtDAO, Mockito.times(2)).delete((OrgExtDO) Matchers.any());
		}

	}

	@Test
	public void testStoreExtAttributesForOrgCreates() {
		baseServiceWithValidationAndExtOrgs.setEntityFieldDAO(entityFieldDAO);

		OrgDO orgDO = new OrgDO();
		// 4 fields being passed in
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		// exts.put("secret", "");
		// exts.put("magicKey", "false");
		orgDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		OrgExtDO superAdmin = new OrgExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		OrgExtDO specialCode = new OrgExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		OrgExtDO secret = new OrgExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		OrgExtDO magicKey = new OrgExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<OrgExtDO> mockCurrentList = Lists.newArrayList();
		// mockCurrentList.add(superAdmin);
		// mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("specialCode"))).thenReturn(specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(orgExtDAO.getExtDOs(Matchers.any(OrgDO.class))).thenReturn(mockCurrentList);
		Mockito.when(orgExtDAO.getNew()).thenReturn(new OrgExtDO());
		try {
			baseServiceWithValidationAndExtOrgs.storeExtFields(context, orgDO, orgExtDAO, EntityDAO.EntityTypeCode.ORG, 1L);
		} finally {
			verify(orgExtDAO, Mockito.times(2)).delete((OrgExtDO) Matchers.any());
		}
	}

	@Test
	public void testStoreExtAttributesForOrgPartDeletes() {
		baseServiceWithValidationAndExtOrgParts.setEntityFieldDAO(entityFieldDAO);

		OrgPartDO orgPartDO = new OrgPartDO();

		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		exts.put("secret", "");
		// exts.put("magicKey", "false");
		orgPartDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		OrgPartExtDO superAdmin = new OrgPartExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		OrgPartExtDO specialCode = new OrgPartExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		OrgPartExtDO secret = new OrgPartExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		OrgPartExtDO magicKey = new OrgPartExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<OrgPartExtDO> mockCurrentList = Lists.newArrayList();
		mockCurrentList.add(superAdmin);
		mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("specialCode"))).thenReturn(
				specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(orgPartExtDAO.getExtDOs(Matchers.any(OrgPartDO.class))).thenReturn(mockCurrentList);
		Mockito.when(orgPartExtDAO.getNew()).thenReturn(new OrgPartExtDO());
		try {
			baseServiceWithValidationAndExtOrgParts.storeExtFields(context, orgPartDO, orgPartExtDAO,
					EntityDAO.EntityTypeCode.ORG_PART, 1L);
		} finally {
			verify(orgPartExtDAO, Mockito.times(2)).delete((OrgPartExtDO) Matchers.any());
		}
	}

	@Test
	public void testStoreExtAttributesForOrgPartUpdates() {
		baseServiceWithValidationAndExtOrgParts.setEntityFieldDAO(entityFieldDAO);

		OrgPartDO orgPartDO = new OrgPartDO();
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		exts.put("secret", "");
		// exts.put("magicKey", "false");
		orgPartDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		OrgPartExtDO superAdmin = new OrgPartExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		OrgPartExtDO specialCode = new OrgPartExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		OrgPartExtDO secret = new OrgPartExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		OrgPartExtDO magicKey = new OrgPartExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<OrgPartExtDO> mockCurrentList = Lists.newArrayList();
		mockCurrentList.add(superAdmin);
		mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("specialCode"))).thenReturn(
				specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(orgPartExtDAO.getExtDOs(Matchers.any(OrgPartDO.class))).thenReturn(mockCurrentList);
		Mockito.when(orgPartExtDAO.getNew()).thenReturn(new OrgPartExtDO());
		try {
			baseServiceWithValidationAndExtOrgParts.storeExtFields(context, orgPartDO, orgPartExtDAO,
					EntityDAO.EntityTypeCode.ORG_PART, 1L);
		} finally {
			verify(orgPartExtDAO, Mockito.times(2)).update((OrgPartExtDO) Matchers.any());
		}
	}

	@Test
	public void testStoreExtAttributesForOrgPartCreates() {
		baseServiceWithValidationAndExtOrgParts.setEntityFieldDAO(entityFieldDAO);

		OrgPartDO orgPartDO = new OrgPartDO();
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		exts.put("secret", "");
		// exts.put("magicKey", "false");
		orgPartDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		OrgPartExtDO superAdmin = new OrgPartExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		OrgPartExtDO specialCode = new OrgPartExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		OrgPartExtDO secret = new OrgPartExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		OrgPartExtDO magicKey = new OrgPartExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<OrgPartExtDO> mockCurrentList = Lists.newArrayList();
		// mockCurrentList.add(superAdmin);
		// mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("specialCode"))).thenReturn(
				specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.ORG_PART), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(orgPartExtDAO.getExtDOs(Matchers.any(OrgPartDO.class))).thenReturn(mockCurrentList);
		Mockito.when(orgPartExtDAO.getNew()).thenReturn(new OrgPartExtDO());
		try {
			baseServiceWithValidationAndExtOrgParts.storeExtFields(context, orgPartDO, orgPartExtDAO,
					EntityDAO.EntityTypeCode.ORG_PART, 1L);
		} finally {
			verify(orgPartExtDAO, Mockito.times(2)).create((OrgPartExtDO) Matchers.any());
		}
	}

	@Test
	public void testStoreExtAttributesForUserDeletes() {
		baseServiceWithValidationAndExtUsers.setEntityFieldDAO(entityFieldDAO);

		UserDO userDO = new UserDO();
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		exts.put("secret", "");
		// exts.put("magicKey", "false");
		userDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		UserExtDO superAdmin = new UserExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		UserExtDO specialCode = new UserExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		UserExtDO secret = new UserExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		UserExtDO magicKey = new UserExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<UserExtDO> mockCurrentList = Lists.newArrayList();
		mockCurrentList.add(superAdmin);
		mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("specialCode"))).thenReturn(specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(userExtDAO.getExtDOs(Matchers.any(UserDO.class))).thenReturn(mockCurrentList);
		Mockito.when(userExtDAO.getNew()).thenReturn(new UserExtDO());
		try {
			baseServiceWithValidationAndExtUsers.storeExtFields(context, userDO, userExtDAO, EntityDAO.EntityTypeCode.USER,
					1L);
		} finally {
			verify(userExtDAO, Mockito.times(2)).delete((UserExtDO) Matchers.any());
		}
	}

	@Test
	public void testStoreExtAttributesForUserUpdates() {
		baseServiceWithValidationAndExtUsers.setEntityFieldDAO(entityFieldDAO);

		UserDO userDO = new UserDO();
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		exts.put("secret", "");
		// exts.put("magicKey", "false");
		userDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		UserExtDO superAdmin = new UserExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		UserExtDO specialCode = new UserExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		UserExtDO secret = new UserExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		UserExtDO magicKey = new UserExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<UserExtDO> mockCurrentList = Lists.newArrayList();
		mockCurrentList.add(superAdmin);
		mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("specialCode"))).thenReturn(specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(userExtDAO.getExtDOs(Matchers.any(UserDO.class))).thenReturn(mockCurrentList);
		Mockito.when(userExtDAO.getNew()).thenReturn(new UserExtDO());
		try {
			baseServiceWithValidationAndExtUsers.storeExtFields(context, userDO, userExtDAO, EntityDAO.EntityTypeCode.USER,
					1L);
		} finally {
			verify(userExtDAO, Mockito.times(2)).update((UserExtDO) Matchers.any());
		}
	}

	@Test
	public void testStoreExtAttributesForUserCreates() {
		baseServiceWithValidationAndExtUsers.setEntityFieldDAO(entityFieldDAO);

		UserDO userDO = new UserDO();
		Map<String, String> exts = Maps.newHashMap();
		exts.put("superAdmin", "true");
		exts.put("specialCode", "8675309");
		exts.put("secret", "");
		// exts.put("magicKey", "false");
		userDO.setExtAttributes(exts);

		EntityFieldDO superAdminDO = new EntityFieldDO();
		superAdminDO.setCode("superAdmin");
		EntityFieldDO specialCodeDO = new EntityFieldDO();
		specialCodeDO.setCode("specialCode");
		EntityFieldDO secretDO = new EntityFieldDO();
		secretDO.setCode("secret");
		EntityFieldDO magicKeyDO = new EntityFieldDO();
		magicKeyDO.setCode("false");

		// create for 4ExtDO's
		UserExtDO superAdmin = new UserExtDO();
		superAdmin.setEntityField(superAdminDO);
		superAdmin.setValue("false");

		UserExtDO specialCode = new UserExtDO();
		specialCode.setEntityField(specialCodeDO);
		specialCode.setValue("9891212");

		UserExtDO secret = new UserExtDO();
		secret.setEntityField(secretDO);
		secret.setValue("password");

		UserExtDO magicKey = new UserExtDO();
		magicKey.setEntityField(magicKeyDO);
		magicKey.setValue("true");

		List<UserExtDO> mockCurrentList = Lists.newArrayList();
		// mockCurrentList.add(superAdmin);
		// mockCurrentList.add(specialCode);
		mockCurrentList.add(secret);
		mockCurrentList.add(magicKey);

		ServiceContext context = Mockito.mock(ServiceContext.class);
		Mockito.when(context.getScopeId()).thenReturn(4L);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("superAdmin"))).thenReturn(superAdminDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("specialCode"))).thenReturn(specialCodeDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("secret"))).thenReturn(secretDO);
		Mockito.when(
				entityFieldDAO.findByScopeAndTypeAndCode(Matchers.any(Long.class),
						Matchers.eq(EntityDAO.EntityTypeCode.USER), Matchers.eq("magicKey"))).thenReturn(magicKeyDO);
		// set already existing ext fields
		Mockito.when(userExtDAO.getExtDOs(Matchers.any(UserDO.class))).thenReturn(mockCurrentList);
		Mockito.when(userExtDAO.getNew()).thenReturn(new UserExtDO());
		try {
			baseServiceWithValidationAndExtUsers.storeExtFields(context, userDO, userExtDAO, EntityDAO.EntityTypeCode.USER,
					1L);
		} finally {
			verify(userExtDAO, Mockito.times(2)).create((UserExtDO) Matchers.any());
		}
	}

}