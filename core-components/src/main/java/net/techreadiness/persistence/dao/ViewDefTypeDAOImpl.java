package net.techreadiness.persistence.dao;

import javax.persistence.TypedQuery;

import net.techreadiness.persistence.domain.ViewDefTypeDO;

import org.springframework.stereotype.Repository;

@Repository
public class ViewDefTypeDAOImpl extends BaseDAOImpl<ViewDefTypeDO> implements ViewDefTypeDAO {

	@Override
	public ViewDefTypeDO getByCode(String code) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select vdt ");
		sb.append(" from ViewDefTypeDO vdt");
		sb.append(" where vdt.code = :code");

		TypedQuery<ViewDefTypeDO> query = em.createQuery(sb.toString(), ViewDefTypeDO.class);
		query.setParameter("code", code);

		query.setHint("org.hibernate.cacheable", Boolean.TRUE);

		return getSingleResult(query);
	}
}
