package net.techreadiness.persistence.dao;

import net.techreadiness.persistence.domain.FileDO;

import org.apache.commons.lang3.StringUtils;

public interface FileDAO extends BaseDAO<FileDO> {

	public enum FileTypeCode {
		ORG_IMPORT, ORG_EXPORT, USER_IMPORT, USER_EXPORT, DEVICE_IMPORT, DEVICE_EXPORT, ORG_INFO_IMPORT, ORG_INFO_EXPORT;

		public boolean isImport() {
			return StringUtils.containsIgnoreCase(this.name(), "import");
		}
	}

}
