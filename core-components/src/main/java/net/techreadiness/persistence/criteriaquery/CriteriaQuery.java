package net.techreadiness.persistence.criteriaquery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.techreadiness.persistence.IgnoringCaseAliasToBeanResultTransformer;
import net.techreadiness.persistence.criteriaquery.CriteriaFilter.FilterType;
import net.techreadiness.persistence.criteriaquery.TableInfo.FieldInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("prototype")
public class CriteriaQuery<T> {
	private String baseSubSelect;
	private String baseWhere;
	private List<String> fullTextSearchColumns;

	public String getBaseQuery() {
		return baseSubSelect;
	}

	public void setBaseSubSelect(String baseQuery) {
		this.baseSubSelect = baseQuery;
	}

	public List<String> getFullTextSearchColumns() {
		return fullTextSearchColumns;
	}

	public void setFullTextSearchColumns(List<String> fullTextSearchColumns) {
		this.fullTextSearchColumns = fullTextSearchColumns;
	}

	public void setFullTextSearchColumns(String[] fullTextSearchColumns) {
		this.fullTextSearchColumns = Arrays.asList(fullTextSearchColumns);
	}

	@PersistenceContext
	protected EntityManager em;

	/**
	 * Executes a query that is built using full text search, core column filters, and extension column filters. The filters
	 * and sorting to be applied are provided via the criteria parameter, and the code uses introspection to determine which
	 * fields are core and which are extensions (assuming it is a table that with extensions). The querying is extremely
	 * flexible and dynamic, which means it is also not optimized ... use with care on large data sets!
	 *
	 * @param criteria
	 *            Provides the detail filter and sorting information
	 * @param entityClass
	 *            The class of the data to be returned
	 * @return List of result objects of type entityClass
	 */
	@Transactional
	public QueryResult<T> getData(Criteria criteria, Class<?> entityClass) {
		TableInfo tableInfo = TableInfo.create(entityClass);
		QueryResult<T> result = new QueryResult<>();
		Session session = ((HibernateEntityManager) em).getSession();

		StringBuilder sql = new StringBuilder();
		if (StringUtils.isNotBlank(tableInfo.getExtTableName())) {
			sql.append("SELECT {filtered.*}, {extended.*} ");
			sql.append(", filtered." + tableInfo.getPkColumn() + " INDX FROM (");
		}

		String baseSql = generateFilterQuery(criteria, tableInfo, result, true, true);
		result.setExectutedSql(baseSql);
		sql.append(baseSql);

		if (criteria.isPagingActive()) {
			sql.append(" LIMIT " + criteria.getFirstResults());
			sql.append(", " + criteria.getPageSize());
		}
		if (StringUtils.isNotBlank(tableInfo.getExtTableName())) {
			sql.append(") filtered ");
			sql.append(" left join " + tableInfo.getExtTableName() + " extended on (filtered." + tableInfo.getPkColumn()
					+ " = extended." + tableInfo.getPkColumn() + ") ");
		}

		SQLQuery query = session.createSQLQuery(sql.toString());
		if (StringUtils.isNotBlank(tableInfo.getExtTableName())) {
			query.addScalar("INDX"); // this scalar has to be here to workaround a hibernate bug.
			query.addEntity("filtered", tableInfo.getEntityClass());
			query.addJoin("extended", "filtered." + tableInfo.getExtFieldName());
			query.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		} else if (StringUtils.isNotBlank(tableInfo.getTableName())) {
			query.addEntity("main", tableInfo.getEntityClass());
		} else if (Map.class.isAssignableFrom(entityClass)) {
			query.setResultTransformer(AliasToEntityStringMapResultTransformer.INSTANCE);
		} else {
			query.setResultTransformer(new IgnoringCaseAliasToBeanResultTransformer(tableInfo.getEntityClass()));
		}

		for (String key : criteria.getParameters().keys()) {
			result.queryParameters.put(key, criteria.getParameters().get(key));
		}

		query.setProperties(result.queryParameters);
		List<T> rows = query.list();

		result.setRows(rows);
		if (!criteria.isPagingActive()) {
			result.setTotalRowCount(rows.size());
		} else {
			StringBuilder countSql = new StringBuilder();
			countSql.append("select count(*) cnt from (");
			countSql.append(generateFilterQuery(criteria, tableInfo, result, false, false));
			countSql.append(") c");
			SQLQuery countQuery = session.createSQLQuery(countSql.toString());
			countQuery.addScalar("cnt", StandardBasicTypes.INTEGER); // this scalar has to be here to workaround a hibernate
			// bug.
			countQuery.setProperties(result.queryParameters);
			Integer count = (Integer) countQuery.uniqueResult();
			result.setTotalRowCount(count);

		}

		return result;
	}

	private String generateFilterQuery(Criteria criteria, TableInfo tableInfo, QueryResult<T> result,
			boolean populateFilters, boolean includeSort) {
		List<String> extWhereClauses = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		if (criteria != null && criteria.getFilters() != null && populateFilters) {
			for (CriteriaFilter filter : criteria.getFilters()) {
				FieldInfo fieldInfo = tableInfo.getFieldMap().get(filter.getFieldName());

				if (fieldInfo == null) {
					if (!criteria.getParameters().containsKey(filter.getFieldName())) {
						// This is a an custom field.
						result.extensionFilters.add(filter);
					}
				} else {
					// This is a an core field.
					result.coreFilters.add(filter);
				}
			}
		}

		sb.append("SELECT main.* FROM ");
		if (StringUtils.isBlank(baseSubSelect)) {
			sb.append(tableInfo.getTableName());
		} else {
			sb.append("(" + baseSubSelect + ")");
		}

		sb.append(" main ");
		if (StringUtils.isNotBlank(tableInfo.getExtTableName())) {
			if (result.extensionFilters.size() > 0) {
				sb.append(outputExtensionFilters(result, tableInfo, extWhereClauses));
			}
		}
		sb.append(" where 1=1 ");

		if (StringUtils.isNotBlank(baseWhere)) {
			sb.append(" and " + baseWhere + " ");
		}

		// Filter the primary table based on the individual filters.
		sb.append(outputCoreFilters(result, tableInfo));

		// Add the extra where clauses needed for any custom filtering
		for (String clause : extWhereClauses) {
			sb.append(" and " + clause);
		}

		// Now handle the full text search box. I investigate using REGEXP instead of like for this as it seemed
		// like it might be able to better handle multiple terms ... but it performed very poorly, so sticking with LIKE.
		String delim = "";
		if (StringUtils.isNotBlank(criteria.getFullTextSearch()) && fullTextSearchColumns != null
				&& fullTextSearchColumns.size() > 0) {

			String[] searchTerms;

			if (criteria.getDelimitFullTextSearch()) {
				searchTerms = StringUtils.split(criteria.getFullTextSearch());
			} else {
				searchTerms = new String[] { criteria.getFullTextSearch() };
			}

			delim = "";
			int count = 1;
			sb.append(" and (");
			for (String column : fullTextSearchColumns) {
				for (String term : searchTerms) {
					sb.append(delim);
					sb.append(" main.");
					sb.append(column);
					sb.append(" like CONCAT('%',:full_text_search" + count + ",'%') ");
					result.queryParameters.put("full_text_search" + count, term);
					delim = " or ";
					count++;
				}
			}
			sb.append(" )");
		}

		// add order by clause if specified
		if (includeSort && criteria.getSorts() != null && criteria.getSorts().size() > 0) {
			sb.append(" order by ");
			delim = "";
			for (CriteriaSort sort : criteria.getSorts()) {
				FieldInfo fieldInfo = tableInfo.getFieldMap().get(sort.getFieldName());
				if (fieldInfo == null || StringUtils.isBlank(fieldInfo.getColName())) {
					continue;
				}
				sb.append(delim + fieldInfo.getColName());
				if (sort.isDescending()) {
					sb.append(" DESC ");
				} else {
					sb.append(" ASC ");
				}
				delim = ",";
			}
		}
		return sb.toString();
	}

	private String outputExtensionFilters(QueryResult<T> result, TableInfo tableInfo, List<String> whereClause) {
		StringBuilder sb = new StringBuilder();
		int custFilter = 1;
		if (result.extensionFilters.size() > 0) {
			for (CriteriaFilter filter : result.extensionFilters) {
				if (filter.getFilterType() == FilterType.ISNULL) {
					StringBuilder where = new StringBuilder();
					where.append(" main." + tableInfo.getPkColumn() + " not in (");
					where.append("   select " + tableInfo.getPkColumn());
					where.append("   from " + tableInfo.getExtTableName() + " ext");
					where.append("   join entity_field ef on ext.entity_field_id = ef.entity_field_id");
					where.append("   where ef.code = :cust_filter_name" + custFilter + " and ext.value is not null");
					where.append(") ");
					whereClause.add(where.toString());
					result.queryParameters.put("cust_filter_name" + custFilter, filter.getFieldName());
				} else if (filter.getValues() != null || filter.getValues().size() > 0) {
					sb.append(" JOIN (");
					sb.append("   select " + tableInfo.getPkColumn());
					sb.append("   from " + tableInfo.getExtTableName() + " ext");
					sb.append("   join entity_field ef ON ext.entity_field_id = ef.entity_field_id");
					sb.append("   where ef.code = :cust_filter_name" + custFilter + " and ");
					result.queryParameters.put("cust_filter_name" + custFilter, filter.getFieldName());

					if (filter.getFilterType() == FilterType.ISNOTNULL) {
						sb.append(" value is not null");
					} else if (filter.getFilterType() == FilterType.EQUALS) {
						sb.append(" value in (:cust_filter_val" + custFilter + ")");
						result.queryParameters.put("cust_filter_val" + custFilter, filter.getValues());
					} else if (filter.getFilterType() == FilterType.LIKE) {
						String delim = "";
						result.queryParameters.put("cust_filter_name" + custFilter, filter.getFieldName());
						sb.append(" (");
						int i = 1;
						for (Object value : filter.getValues()) {
							sb.append(delim);
							sb.append("value like :cust_filter_val" + custFilter + "_" + i);
							result.queryParameters.put("cust_filter_val" + custFilter + "_" + i, value);
							i++;
							delim = " or ";
						}
						sb.append(")");
					}
					sb.append(") cust_filter_join" + custFilter + " on main." + tableInfo.getPkColumn()
							+ " = cust_filter_join" + custFilter + "." + tableInfo.getPkColumn());
				}
				custFilter++;
			}
		}
		return sb.toString();
	}

	private String outputCoreFilters(QueryResult<T> result, TableInfo tableInfo) {
		String delim;
		StringBuilder sb = new StringBuilder();
		if (result.coreFilters.size() > 0) {
			for (CriteriaFilter filter : result.coreFilters) {
				FieldInfo fieldInfo = tableInfo.getFieldMap().get(filter.getFieldName());

				// Handle the core fields.
				if (filter.getFilterType() == FilterType.ISNOTNULL) {
					sb.append(" and main.");
					sb.append(fieldInfo.getColName());
					sb.append(" is not null ");
				} else if (filter.getFilterType() == FilterType.ISNULL) {
					sb.append(" and main.");
					sb.append(fieldInfo.getColName());
					sb.append(" is null ");
				} else if (filter.getValues() == null || filter.getValues().size() == 0) {
					// Do nothing.
				} else if (filter.getFilterType() == FilterType.EQUALS) {
					sb.append(" and main.");
					sb.append(fieldInfo.getColName());
					if (filter.getFieldName().startsWith("visible")) {
						if (filter.getValues().contains("true") && filter.getValues().contains("false")) {
							sb.append(" in (0, 1)");
						} else {
							if (filter.getValues().contains("true")) {
								sb.append(" in (1)");
							} else {
								if (filter.getValues().contains("false")) {
									sb.append(" in (0)");
								}
							}

						}
					} else {
						sb.append(" in ( :");
						sb.append(filter.getFieldName());
						sb.append(")");
					}
					result.queryParameters.put(filter.getFieldName(), filter.getValues());
				} else if (filter.getFilterType() == FilterType.LIKE) {
					delim = "";
					sb.append(" and (");
					int i = 1;
					for (Object value : filter.getValues()) {
						sb.append(delim);
						sb.append(" main.");
						sb.append(fieldInfo.getColName());
						sb.append(" like :");
						sb.append(filter.getFieldName() + i);
						if (fieldInfo.getDatatype().equals(java.util.Date.class)) {
							String out = "";
							try {
								Date date = new SimpleDateFormat("MM/dd/yyyy").parse((String) value);
								out = DateFormatUtils.format(date, "yyyy-MM-dd");
								out += "%";
							} catch (Exception e) {
								// ignore illegal dates
							}
							result.queryParameters.put(filter.getFieldName() + i, out);
						} else {
							result.queryParameters.put(filter.getFieldName() + i, value);
						}
						i++;
						delim = " or ";
					}
					sb.append(" )");
				}
			}
		}
		return sb.toString();
	}

	public String getBaseWhere() {
		return baseWhere;
	}

	public void setBaseWhere(String baseWhere) {
		this.baseWhere = baseWhere;
	}
}
