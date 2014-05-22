package net.techreadiness.persistence.criteriaquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResult<T> {
	private int totalRowCount = 0;
	private Collection<T> rows = new ArrayList<>();
	private String exectutedSql;
	List<CriteriaFilter> coreFilters = new ArrayList<>();
	List<CriteriaFilter> extensionFilters = new ArrayList<>();
	Map<String, Object> queryParameters = new HashMap<>();

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public Collection<T> getRows() {
		return rows;
	}

	public void setRows(Collection<T> rows) {
		this.rows = rows;
	}

	public String getExectutedSql() {
		return exectutedSql;
	}

	public void setExectutedSql(String exectutedSql) {
		this.exectutedSql = exectutedSql;
	}
}
