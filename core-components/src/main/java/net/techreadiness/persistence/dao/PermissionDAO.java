package net.techreadiness.persistence.dao;

import java.util.List;

import net.techreadiness.persistence.domain.PermissionDO;

public interface PermissionDAO extends BaseDAO<PermissionDO> {

	List<PermissionDO> findAllAncestorPermissions(Long scopeId);

}
