package net.techreadiness.service.object.mapping;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import net.techreadiness.persistence.dao.GenericDAO;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgExtDO;
import net.techreadiness.persistence.domain.OrgTypeDO;
import net.techreadiness.persistence.domain.PermissionDO;
import net.techreadiness.persistence.domain.RoleDO;
import net.techreadiness.service.object.Org;
import net.techreadiness.service.object.Permission;
import net.techreadiness.service.object.Role;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MappingServiceTest {
	MappingService mappingService;
	OrgDO orgDO;

	@Mock
	GenericDAO genericDao;

	@Before
	public void setup() {
		MappingServiceImpl mappingService = new MappingServiceImpl();
		mappingService.genericDao = genericDao;
		this.mappingService = mappingService;

		orgDO = new OrgDO();
		orgDO.setOrgId(42L);
		orgDO.setOrgExts(new ArrayList<OrgExtDO>());

		OrgTypeDO orgType = new OrgTypeDO();
		orgType.setCode("orgType");
		orgDO.setOrgType(orgType);

	}

	@Test
	public void testMapFromEntityToBaseObject() {

		Org org = mappingService.map(orgDO, Org.class);

		Assert.assertEquals((Long) 42L, org.getOrgId());
		Assert.assertEquals("orgType", org.getOrgTypeCode());
	}

	@Test
	public void testMapFromBaseObjectToEntity() {
		Org org = new Org();
		org.setOrgId(Long.valueOf(42));

		OrgDO orgDO = mappingService.map(org);

		Assert.assertEquals(Long.valueOf(42), orgDO.getOrgId());
	}

	@Test
	public void testToStringRepresentation() {
		Org org = new Org();
		org.setOrgId(Long.valueOf(42));

		String orgId = mappingService.toStringRepresentation(org);

		Assert.assertEquals("42", orgId);
	}

	@Test
	public void testFromStringRepresentation() {
		when(genericDao.find(OrgDO.class, 42L)).thenReturn(orgDO);

		Org org = mappingService.fromStringRepresentation(Org.class, "42");

		Assert.assertEquals(Long.valueOf(42), org.getOrgId());
	}

	@Test
	public void testPermissionMap() {
		PermissionDO permissionDO = new PermissionDO();
		permissionDO.setPermissionId(12L);

		Permission permission = mappingService.map(permissionDO);

		Assert.assertEquals(permissionDO.getPermissionId(), permission.getPermissionId());
	}

	@Test
	public void testPermissionListMap() {

		List<PermissionDO> list = new ArrayList<>();

		PermissionDO permissionDO = new PermissionDO();
		permissionDO.setPermissionId(12L);
		list.add(permissionDO);

		permissionDO = new PermissionDO();
		permissionDO.setPermissionId(16L);
		list.add(permissionDO);

		List<Permission> mappedList = mappingService.mapFromDOList(list);

		Assert.assertEquals(mappedList.size(), 2);
		Assert.assertEquals(mappedList.get(0).getPermissionId(), Long.valueOf(12L));
		Assert.assertEquals(mappedList.get(1).getPermissionId(), Long.valueOf(16L));
	}

	@Test
	public void testRoleMap() {
		RoleDO roleDO = new RoleDO();
		roleDO.setRoleId(12L);

		Role role = mappingService.map(roleDO);

		Assert.assertEquals(roleDO.getRoleId(), role.getRoleId());
	}
}
