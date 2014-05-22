package net.techreadiness.service.object.mapping;

import java.util.Map;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import net.techreadiness.persistence.domain.OrgDO;
import net.techreadiness.persistence.domain.OrgExtDO;
import net.techreadiness.service.object.Org;

import com.google.common.collect.Maps;

public class OrgDOandOrgMapper extends CustomMapper<OrgDO, Org> {

	@Override
	public void mapAtoB(OrgDO orgDO, Org org, MappingContext context) {
		Map<String, String> map = Maps.newHashMap();
		if (orgDO.getOrgExts() != null) {
			for (OrgExtDO orgExtDO : orgDO.getOrgExts()) {
				map.put(orgExtDO.getEntityField().getCode(), orgExtDO.getValue());
			}
		}
		org.setExtendedAttributes(map);

		// it's quite possible there is no parent org, the only
		// way to know (since it is lazily loaded) is to try, though.
		try {
			org.setParentOrgId(orgDO.getParentOrg().getOrgId());
			org.setParentOrgCode(orgDO.getParentOrg().getCode());
			org.setParentOrgName(orgDO.getParentOrg().getName());
			org.setParentOrgLocalCode(orgDO.getParentOrg().getLocalCode());
		} catch (NullPointerException npe) {
			org.setParentOrgId(null);
			org.setParentOrgCode("");
			org.setParentOrgName("");
		}

		org.setOrgTypeId(orgDO.getOrgType().getOrgTypeId());
		org.setOrgTypeCode(orgDO.getOrgType().getCode());
		org.setOrgTypeName(orgDO.getOrgType().getName());
		org.setOrgTypeAllowDevice(orgDO.getOrgType().isAllowDevice());
	}

	@Override
	public void mapBtoA(Org org, OrgDO orgDO, MappingContext context) {
		orgDO.setExtAttributes(org.getExtendedAttributes());
	}

}
