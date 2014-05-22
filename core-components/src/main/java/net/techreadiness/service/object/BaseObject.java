package net.techreadiness.service.object;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.techreadiness.annotation.CoreExtFields;
import net.techreadiness.annotation.CoreField;
import net.techreadiness.persistence.BaseEntity;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class BaseObject<T extends BaseEntity> implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract Class<T> getBaseEntityType();

	protected DateFormat fmt;

	public BaseObject() {
		fmt = new SimpleDateFormat("M/d/yyyy");
	}

	public Map<String, String> getAsMap() {
		Map<String, String> entityMap = Maps.newTreeMap();
		try {
			for (Class<?> clazz = this.getClass(); clazz != Object.class && clazz != null; clazz = clazz.getSuperclass()) {
				for (Field field : clazz.getDeclaredFields()) {

					if (field.isAnnotationPresent(CoreField.class)) {
						field.setAccessible(true);
						Object obj = field.get(this);
						if (obj != null) {
							if (java.util.Date.class.isAssignableFrom(field.getType())) {
								entityMap.put(field.getName(), fmt.format((Date) obj));
							} else {
								entityMap.put(field.getName(), ConvertUtils.convert(obj));
							}
						}
					} else if (field.isAnnotationPresent(CoreExtFields.class)) {
						if (field.get(this) != null) {
							Map<String, String> map = (Map<String, String>) field.get(this);
							entityMap.putAll(map);
						}

					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return entityMap;
	}

	public void setFromMap(final Map<String, String> map, boolean update) {
		Map<String, String> copy = Maps.newHashMap();
		copy.putAll(map);

		Iterator<Entry<String, String>> iterator = copy.entrySet().iterator();

		try {
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();

				try {
					Field field = this.getClass().getDeclaredField(key);

					if (field != null) {
						if (update || StringUtils.isNotEmpty(value)) {
							Object convertedValue = ConvertUtils.convert(value, field.getType());
							field.set(this, convertedValue);
						}
						iterator.remove();
					}
				} catch (NoSuchFieldException e) {
					// nothing to do, field just may not exist. is to be expected sometimes.
				} catch (Exception e) {
					// probably a conversion error
					e.printStackTrace();
				}
			}

			// now put any remaining field in the extended attributes map (if
			// appropriate).
			if (!copy.isEmpty()) {// if the map is now empty we don't care about copying it into exts, their state remains
									// unchanged
				if (this instanceof BaseObjectWithExts) {
					BaseObjectWithExts<T> thisWithExts = (BaseObjectWithExts<T>) this;
					Map<String, String> exts = thisWithExts.getExtendedAttributes();
					if (exts == null) {
						exts = Maps.newHashMap();
					}
					exts.putAll(copy);
					thisWithExts.setExtendedAttributes(exts);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the ID field for. The ID will be used to determine Object equality.
	 */
	public abstract Long getId();

	/**
	 * Generate hashCode based on ID field. If ID is null, use reflection to build one dynamically.
	 */
	@Override
	public int hashCode() {
		if (getId() == null) {
			return HashCodeBuilder.reflectionHashCode(this);
		}
		return getId().hashCode();
	}

	/**
	 * Determine object equality based on ID field. If the ID fields of both objects is null, use reflection to compare
	 * fields dynamically.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		BaseObject<?> other = (BaseObject<?>) obj;
		if (getId() == null && other.getId() == null) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
		return Objects.equal(getId(), other.getId());
	}

	public static List<Long> getIds(List<? extends BaseObject<?>> baseObjects) {
		List<Long> ids = Lists.newArrayList();
		for (BaseObject<?> object : baseObjects) {
			ids.add(object.getId());
		}
		return ids;
	}
}
