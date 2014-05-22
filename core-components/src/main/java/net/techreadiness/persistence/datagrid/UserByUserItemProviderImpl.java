package net.techreadiness.persistence.datagrid;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaQuery;
import net.techreadiness.persistence.criteriaquery.QueryResult;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.service.common.DataGrid;
import net.techreadiness.service.common.DataGridItemProviderImpl;
import net.techreadiness.service.object.User;
import net.techreadiness.service.object.mapping.MappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("prototype")
@Transactional(readOnly = true)
public class UserByUserItemProviderImpl extends DataGridItemProviderImpl<User> implements UserByUserItemProvider {
	private Collection<Long> userIds;

	@Inject
	MappingService mappingService;
	@Autowired
	CriteriaQuery<UserDO> criteriaQuery;

	@Override
	public List<User> getPage(DataGrid<User> grid) {
		int start = (grid.getPage() - 1) * grid.getPageSize();
		int pageSize = 100;

		Criteria criteria = new Criteria(start, pageSize);

		criteriaQuery.setBaseWhere("main.user_id in (:user_id_list)");
		criteria.getParameters().putAll("user_id_list", userIds);

		QueryResult<UserDO> result = criteriaQuery.getData(criteria, UserDO.class);
		setTotalNumberOfItems(result.getTotalRowCount());
		return mappingService.mapFromDOList(result.getRows());
	}

	public Collection<Long> getUserIds() {
		return userIds;
	}

	@Override
	public void setUserIds(Collection<Long> userIds) {
		this.userIds = userIds;
	}

}
