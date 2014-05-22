package net.techreadiness.persistence.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.techreadiness.persistence.domain.OptionListDO;
import net.techreadiness.persistence.domain.OptionListDO_;
import net.techreadiness.persistence.domain.OptionListValueDO;
import net.techreadiness.persistence.domain.OptionListValueDO_;

import org.springframework.stereotype.Repository;

@Repository
public class OptionListValueDAOImpl extends BaseDAOImpl<OptionListValueDO> implements OptionListValueDAO {

	@Override
	public List<OptionListValueDO> getOptionListValuesByOptionListId(Long optionListId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<OptionListValueDO> cq = cb.createQuery(OptionListValueDO.class);
		Root<OptionListValueDO> root = cq.from(OptionListValueDO.class);
		Join<OptionListValueDO, OptionListDO> j = root.join(OptionListValueDO_.optionList);

		Predicate p = cb.equal(j.get(OptionListDO_.optionListId), optionListId);

		cq.where(p);

		return getResultList(em.createQuery(cq));
	}

}
