package net.techreadiness.service.object;

import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.domain.FileTypeDO;

public class FileType extends BaseObject<FileTypeDO> {
	private static final long serialVersionUID = 1L;
	@CoreField
	protected Long fileTypeId;
	@CoreField
	protected String code;
	@CoreField
	protected String name;

	@Override
	public Class<FileTypeDO> getBaseEntityType() {
		return FileTypeDO.class;
	}

	@Override
	public Long getId() {
		return fileTypeId;
	}

	public Long getFileTypeId() {
		return fileTypeId;
	}

	public void setFileTypeId(Long fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
