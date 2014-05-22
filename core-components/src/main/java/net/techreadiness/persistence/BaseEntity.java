package net.techreadiness.persistence;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

@MappedSuperclass
public class BaseEntity {
	private transient Map<String, FieldInfo> fieldMap = null;
	@SuppressWarnings("rawtypes")
	private static LoadingCache<Class, List<PropertyDescriptor>> propertyCache = CacheBuilder.newBuilder().build(
			CacheLoader.from(new PropertyDescriptorLookup()));

	@XmlTransient
	@JsonIgnore
	public Map<String, FieldInfo> getColumnFieldInfo() {
		if (fieldMap == null) {
			fieldMap = new HashMap<>();
			Field[] fields = this.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String colName = fields[i].getName();
				OneToMany oneToMany = fields[i].getAnnotation(OneToMany.class);
				// ManyToOne manyToOne = fields[i].getAnnotation(ManyToOne.class);
				Transient trans = fields[i].getAnnotation(Transient.class);
				String modifier = Modifier.toString(fields[i].getModifiers());

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
				FieldInfo fieldInfo = new FieldInfo(colName, fields[i].getType());
				fieldMap.put(fields[i].getName(), fieldInfo);
			}
		}
		return fieldMap;
	}

	@XmlTransient
	@JsonIgnore
	public Map<String, String> getColumnsAsMap() {
		Map<String, String> colMap = Maps.newTreeMap();

		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String colName = fields[i].getName();
			Column annotation = fields[i].getAnnotation(Column.class);
			if (annotation != null) {
				try {
					colMap.put(colName, PropertyUtils.getProperty(this, colName).toString());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return colMap;
	}

	@XmlTransient
	@JsonIgnore
	public Map<String, String> getAsMap() {
		Map<String, String> entityMap = Maps.newTreeMap();
		try {
			for (PropertyDescriptor prop : propertyCache.get(this.getClass())) {
				String property = prop.getName();
				String className = prop.getPropertyType().getName();

				if (className.startsWith("java.util.") && !className.equals("java.util.Date")
						|| className.startsWith("net.techreadiness") || "class".equals(property) || "asMap".equals(property)
						|| "columnsAsMap".equals(property) || "columnFieldInfo".equals(property)
						|| "extendedDefinition".equals(property) || "exts".equals(property)
						|| "serviceObjectType".equals(property) || "extAttributes".equals(property)) {
					// TODO : I think we have to ignore this for now
					continue;
				}
				Object obj = prop.getReadMethod().invoke(this);
				entityMap.put(property, ConvertUtils.convert(obj));
			}
			return entityMap;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public class FieldInfo {
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

	@SuppressWarnings("rawtypes")
	public static class PropertyDescriptorLookup implements Function<Class, List<PropertyDescriptor>> {
		@Override
		public List<PropertyDescriptor> apply(Class clazz) {
			return Arrays.asList(PropertyUtils.getPropertyDescriptors(clazz));
		}

	}
}
