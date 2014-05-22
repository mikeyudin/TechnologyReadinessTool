package net.techreadiness.persistence.criteriaquery;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.techreadiness.persistence.AbstractAuditedBaseEntityWithExt;

import org.apache.commons.lang3.StringUtils;

public class TableInfo {
	private static Map<Class<?>, TableInfo> cache = new HashMap<>();
	private Map<String, FieldInfo> fieldMap = new HashMap<>();
	private Class<?> entityClass;
	private String tableName;
	private String extTableName;
	private String extFieldName;
	private String pkColumn;

	private TableInfo(Class<?> clazz) {
		entityClass = clazz;
	}

	public static TableInfo create(Class<?> clazz) {
		TableInfo tableInfo = cache.get(clazz);
		if (tableInfo != null) {
			return tableInfo;
		}
		tableInfo = new TableInfo(clazz);
		Field[] fields = clazz.getDeclaredFields();
		Table table = clazz.getAnnotation(Table.class);
		if (table != null) {
			tableInfo.setTableName(table.name());
		}

		for (int i = 0; i < fields.length; i++) {
			String colName = fields[i].getName();
			OneToMany oneToMany = fields[i].getAnnotation(OneToMany.class);
			Transient trans = fields[i].getAnnotation(Transient.class);
			String modifier = Modifier.toString(fields[i].getModifiers());

			Type genericType = fields[i].getGenericType();
			if (genericType instanceof ParameterizedType) {
				ParameterizedType type = (ParameterizedType) genericType;
				Type[] typeArguments = type.getActualTypeArguments();
				for (Type typeArgument : typeArguments) {
					Class<?> typeArgClass = (Class<?>) typeArgument;
					Class<?> superclass = typeArgClass.getSuperclass();
					if (AbstractAuditedBaseEntityWithExt.class.equals(superclass)) {
						Table extTable = typeArgClass.getAnnotation(Table.class);
						tableInfo.setExtFieldName(fields[i].getName());
						tableInfo.setExtTableName(extTable.name());
					}
				}
			}
			if (trans != null || oneToMany != null || StringUtils.contains(modifier, "static")) {
				continue;
			}

			JoinColumn joinColumn = fields[i].getAnnotation(JoinColumn.class);
			if (joinColumn != null && StringUtils.isNotBlank(joinColumn.name())) {
				colName = joinColumn.name();
			}

			Column column = fields[i].getAnnotation(Column.class);
			if (column != null && StringUtils.isNotBlank(column.name())) {
				colName = column.name();
			}

			Id id = fields[i].getAnnotation(Id.class);
			if (id != null) {
				tableInfo.setPkColumn(colName);
			}

			FieldInfo fieldInfo = new FieldInfo(colName, fields[i].getType());
			tableInfo.fieldMap.put(fields[i].getName(), fieldInfo);
		}

		cache.put(clazz, tableInfo);

		return tableInfo;
	}

	public Map<String, FieldInfo> getFieldMap() {
		return fieldMap;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public String getExtTableName() {
		return extTableName;
	}

	public void setExtTableName(String extTableName) {
		this.extTableName = extTableName;
	}

	public static class FieldInfo {
		private String colName;
		private Class<?> datatype;

		public FieldInfo(String colName, Class<?> datatype) {
			super();
			this.colName = colName;
			this.datatype = datatype;
		}

		public String getColName() {
			return colName;
		}

		public void setColName(String colName) {
			this.colName = colName;
		}

		public Class<?> getDatatype() {
			return datatype;
		}

		public void setDatatype(Class<?> datatype) {
			this.datatype = datatype;
		}

		@Override
		public String toString() {
			return colName + "(" + datatype + ")";
		}

	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPkColumn() {
		return pkColumn;
	}

	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}

	public String getExtFieldName() {
		return extFieldName;
	}

	public void setExtFieldName(String extFieldName) {
		this.extFieldName = extFieldName;
	}

}
