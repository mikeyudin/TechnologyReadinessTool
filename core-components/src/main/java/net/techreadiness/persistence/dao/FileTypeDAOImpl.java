package net.techreadiness.persistence.dao;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.FileTypeDO;

import org.springframework.stereotype.Repository;

@Repository
public class FileTypeDAOImpl extends BaseDAOImpl<FileTypeDO> implements FileTypeDAO {

	@Override
	public FileTypeDO getByCode(String fileTypeCode) {

		String sql = " select ft from FileTypeDO ft where ft.code = :fileTypeCode";

		TypedQuery<FileTypeDO> query = em.createQuery(sql, FileTypeDO.class);
		query.setParameter("fileTypeCode", fileTypeCode);
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getSingleResult(query);
	}
}
