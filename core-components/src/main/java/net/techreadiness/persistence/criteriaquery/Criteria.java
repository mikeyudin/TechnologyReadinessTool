package net.techreadiness.persistence.criteriaquery;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Criteria {
	private String queryName;
	private String fullTextSearch;
	private boolean delimitFullTextSearch = true;

	private List<CriteriaSort> sorts = new ArrayList<>();
	private List<CriteriaFilter> filters = new ArrayList<>();
	private Multimap<String, Object> parameters = HashMultimap.create();
	private Integer firstResults = null;
	private Integer pageSize = null;

	public Criteria(String queryName, Integer firstResults, Integer pageSize) {
		this.queryName = queryName;
		this.firstResults = firstResults;
		this.pageSize = pageSize;
	}

	public Criteria(Integer firstResults, Integer pageSize) {
		this.firstResults = firstResults;
		this.pageSize = pageSize;
	}

	public Criteria() {
	}

	/**
	 * Name of the query to be executed. This query must be defined in the CMOS meta-data table (CM_QUERY_SQL).
	 * 
	 * @return query name
	 */
	@XmlElement(required = true)
	public String getQueryName() {
		return queryName;
	}

	/**
	 * Sets the name of the query to be executed. This query must be defined in the CMOS meta-data table (CM_QUERY_SQL).
	 * 
	 * @param queryName
	 *            name of query to be executed.
	 */
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	/**
	 * Set the first result (starting at 1) to be retrieved. This can be used in combination with {@code setPageSize} to do
	 * paging.
	 * 
	 * @return first result (starting at 1) to be retrieved
	 */
	public Integer getFirstResults() {
		return firstResults;
	}

	/**
	 * Set the first row (starting at 1) to be retrieved.
	 * 
	 * @param firstResults
	 *            first row to be retrieved
	 */
	public void setFirstResults(Integer firstResults) {
		this.firstResults = firstResults;
	}

	/**
	 * Returns the limit upon the number of rows to be retrieved.
	 */
	@XmlElement(required = true)
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * Set a limit upon the number of rows to be retrieved.
	 * 
	 * @param pageSize
	 *            max number of rows to be retrieved
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(queryName);
		if (sorts != null && sorts.size() > 0) {
			sb.append(" ( sortOrder=[");
			for (CriteriaSort sort : sorts) {
				sb.append(sort.fieldName + (sort.isDescending() ? "asc " : "desc "));
			}
			sb.append(")");
		}
		if (firstResults != null) {
			sb.append(", firstResults=" + firstResults);
		}
		if (pageSize != null) {
			sb.append(", pageSize=" + pageSize);
		}
		sb.append(", parameters=[");
		if (filters == null) {
			sb.append("none");
		} else {
			for (CriteriaFilter filter : filters) {
				sb.append(filter + ",");
			}
			// sb.append(StringUtils.join(parameters, ','));
		}
		sb.append("]");
		return sb.toString();
	}

	public String getFullTextSearch() {
		return fullTextSearch;
	}

	public void setFullTextSearch(String fullTextSearch) {
		this.fullTextSearch = fullTextSearch;
	}

	public List<CriteriaFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<CriteriaFilter> filters) {
		this.filters = filters;
	}

	public void addFilter(CriteriaFilter filter) {
		filters.add(filter);
	}

	public void addSimpleFilter(String field, String value) {
		filters.add(new CriteriaFilter(field, value));
	}

	public void addSort(CriteriaSort sort) {
		sorts.add(sort);
	}

	public List<CriteriaSort> getSorts() {
		return sorts;
	}

	public void setSorts(List<CriteriaSort> sorts) {
		this.sorts = sorts;
	}

	public boolean isPagingActive() {
		return pageSize != null && firstResults != null;
	}

	public Multimap<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Multimap<String, Object> parameters) {
		this.parameters = parameters;
	}

	public boolean getDelimitFullTextSearch() {
		return delimitFullTextSearch;
	}

	public void setDelimitFullTextSearch(boolean delimitFullTextSearch) {
		this.delimitFullTextSearch = delimitFullTextSearch;
	}
}
