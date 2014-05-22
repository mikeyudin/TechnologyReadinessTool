package net.techreadiness.service.object.mapping;

import java.util.Map;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import net.techreadiness.persistence.domain.ScopeDO;
import net.techreadiness.persistence.domain.ScopeExtDO;
import net.techreadiness.service.object.Scope;

import com.google.common.collect.Maps;

public class ScopeDOandScopeMapper extends CustomMapper<ScopeDO, Scope> {

	@Override
	public void mapAtoB(ScopeDO scopeDO, Scope scope, MappingContext context) {
		Map<String, String> map = Maps.newHashMap();
		if (scopeDO.getScopeExts() != null) {
			for (ScopeExtDO scopeExtDO : scopeDO.getScopeExts()) {
				map.put(scopeExtDO.getEntityField().getCode(), scopeExtDO.getValue());
			}
		}
		scope.setExtendedAttributes(map);
		scope.setScopeTypeId(scopeDO.getScopeType().getScopeTypeId());
		scope.setScopeTypeCode(scopeDO.getScopeType().getCode());
		scope.setScopeTypeName(scopeDO.getScopeType().getName());
		scope.setScopeTypeAllowOrg(scopeDO.getScopeType().isAllowOrg());
		scope.setScopeTypeAllowOrgPart(scopeDO.getScopeType().isAllowOrgPart());
		scope.setScopeTypeAllowUser(scopeDO.getScopeType().isAllowUser());

		// it's possible there is no parent scope, the only
		// way to know (since it is lazily loaded) is to try, though.
		try {
			scope.setParentScopeId(scopeDO.getParentScope().getScopeId());
			scope.setParentScopeCode(scopeDO.getParentScope().getCode());
			scope.setParentScopeName(scopeDO.getParentScope().getName());
			scope.setParentScopePath(scopeDO.getParentScope().getPath());
		} catch (NullPointerException npe) {
			scope.setParentScopeId(null);
			scope.setParentScopeCode("");
			scope.setParentScopeName("");
			scope.setParentScopePath("");
		}
	}

	@Override
	public void mapBtoA(Scope scope, ScopeDO scopeDO, MappingContext context) {
		scopeDO.setExtAttributes(scope.getExtendedAttributes());
	}
}
