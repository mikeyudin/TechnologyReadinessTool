package net.techreadiness.persistence.dao;

import net.techreadiness.persistence.domain.FileTypeDO;

public interface FileTypeDAO extends BaseDAO<FileTypeDO> {

	FileTypeDO getByCode(String fileTypeCode);
}
