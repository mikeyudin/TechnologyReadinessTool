package net.techreadiness.service.object.mapping;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import net.techreadiness.persistence.domain.FileDO;
import net.techreadiness.service.object.File;

public class FileDOandFileMapper extends CustomMapper<FileDO, File> {

	@Override
	public void mapAtoB(FileDO fileDO, File file, MappingContext context) {
		file.setFileTypeId(fileDO.getFileType().getFileTypeId());
		file.setFileTypeCode(fileDO.getFileType().getCode());
		file.setFileTypeName(fileDO.getFileType().getName());
		file.setUserId(fileDO.getUser().getUserId());
		file.setUsername(fileDO.getUser().getUsername());
	}

}
