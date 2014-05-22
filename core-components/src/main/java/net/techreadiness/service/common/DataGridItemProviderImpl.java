package net.techreadiness.service.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.techreadiness.persistence.criteriaquery.Criteria;
import net.techreadiness.persistence.criteriaquery.CriteriaFilter;
import net.techreadiness.persistence.criteriaquery.CriteriaFilter.FilterType;

import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

@Transactional(readOnly = true)
public abstract class DataGridItemProviderImpl<T> implements DataGridItemProvider<T> {
	int totalNumberOfItems = 0;

	@Override
	public int getTotalNumberOfItems(DataGrid<T> grid) {
		return totalNumberOfItems;
	}

	protected Criteria createCriteria(DataGrid<T> grid, String... ignoredFilters) {
		Criteria criteria = new Criteria();
		Multimap<String, String> filters = grid.getFilters();
		ViewDef viewDef = grid.getViewDef();
		List<String> filterKeys = Lists.newArrayList(filters.keys());
		filterKeys.removeAll(Arrays.asList(ignoredFilters));
		for (String key : filterKeys) {
			ViewField field = viewDef == null ? null : viewDef.getField(key);
			FilterType type = CriteriaFilter.FilterType.LIKE;
			List<String> filterValues = new ArrayList<>();

			if (field != null && field.getOptions() != null && field.getOptions().size() > 0) {
				type = CriteriaFilter.FilterType.EQUALS;
				filterValues.addAll(filters.get(key));
			} else {
				for (String value : filters.get(key)) {
					if (field != null && ViewField.DataType.BOOLEAN.toString().equals(field.getDataType())) {
						// for boolean, use the string value (likely "true" or "false")
						// and add in the int 0 or 1 as well.
						Boolean b = Boolean.valueOf(value);
						filterValues.add(value);
						filterValues.add(b ? "1" : "0");
					} else {
						filterValues.add(value += "%");
					}
				}
			}
			criteria.addFilter(new CriteriaFilter(key, type, filterValues));
		}
		criteria.setFullTextSearch(grid.getSearch());

		criteria.setFirstResults((grid.getPage() - 1) * grid.getPageSize());
		criteria.setPageSize(grid.getPageSize());

		return criteria;
	}

	public int getTotalNumberOfItems() {
		return totalNumberOfItems;
	}

	public void setTotalNumberOfItems(int totalNumberOfItems) {
		this.totalNumberOfItems = totalNumberOfItems;
	}
}
