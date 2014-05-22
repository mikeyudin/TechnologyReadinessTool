package net.techreadiness.util;

/* Copyright 2008 Aaron Porter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Id;

import org.apache.log4j.Logger;

/**
 * EntityUtil provides some convenience functions for working with entities.
 * 
 */
public class EntityUtils {
	private static Logger log = Logger.getLogger(EntityUtils.class);

	private static final Map<Class<?>, Class<?>> idTypeCache = new ConcurrentHashMap<>();

	private EntityUtils() {
		throw new AssertionError();
	}

	/**
	 * Return the proxified class for passed class. If the passed class is not CGLIB enhanced, then it returns that same
	 * class.
	 * 
	 * Thanks Remi!
	 * 
	 * @param proxifiedClass
	 *            the proxy class
	 * @return the original class
	 */
	public static Class<? extends Object> deproxifyCglibClass(Class<? extends Object> proxifiedClass) {
		String proxifiedClassName = proxifiedClass.getName();

		int i = proxifiedClassName.indexOf("$$");

		if (i == -1) {
			return proxifiedClass;
		}
		String className = proxifiedClassName.replaceAll("[_]*\\$\\$.*", "");

		try {
			Class<? extends Object> clazz = Class.forName(className);
			return clazz;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Checks the fields and methods of <code>clazz</code> to determine the primary key's type. Results are cached to improve
	 * performance.
	 * 
	 * @param clazz
	 *            the class to examine
	 * @return the primary key's type or null
	 */
	public static Class<?> getIdType(Class<? extends Object> clazz) {
		clazz = deproxifyCglibClass(clazz);

		Class<?> idType = idTypeCache.get(clazz);

		if (idType != null) {
			return idType;
		}

		idType = getIdTypeFromFields(clazz);

		if (idType == null) {
			idType = getIdTypeFromMethods(clazz);
		}

		if (idType == null) {
			Class<? extends Object> superclass = clazz.getSuperclass();

			while (idType == null && superclass != null) {
				idType = getIdTypeFromFields(clazz);
				superclass = superclass.getSuperclass();
			}
		}

		if (idType != null) {
			idTypeCache.put(clazz, idType);
		}

		return idType;
	}

	/**
	 * Looks for an Id annotation on the fields of <code>clazz</code> to figure out the primary key's type.
	 * 
	 * @param clazz
	 *            the class to examine
	 * @return the primary key's type or null
	 */
	public static Class<?> getIdTypeFromFields(Class<? extends Object> clazz) {
		for (Field field : clazz.getDeclaredFields()) {

			if (field.getAnnotation(Id.class) == null) {
				continue;
			}

			try {

				return field.getType();
			} catch (Exception e) {
				log.error(e);
			}
		}

		Class<? extends Object> superclass = clazz.getSuperclass();

		if (superclass != null) {
			return getIdTypeFromFields(superclass);
		}

		return null;
	}

	/**
	 * Looks for an Id annotation on the methods of <code>clazz</code> to figure out the primary key's type.
	 * 
	 * @param clazz
	 *            the class to examine
	 * @return the primary key's type or null
	 */
	public static Class<?> getIdTypeFromMethods(Class<? extends Object> clazz) {
		for (Method method : clazz.getMethods()) {

			if (method.getParameterTypes().length != 0 || method.getAnnotation(Id.class) == null) {
				continue;
			}

			try {

				return method.getReturnType();
			} catch (Exception e) {
				log.error(e);
			}
		}

		return null;
	}

	private static final Map<Class<?>, Object> idAccessor = new ConcurrentHashMap<>();

	/**
	 * Gets the value of the primary key for the specified entity.
	 * 
	 * @param entity
	 *            the target entity
	 * @return the primary key or null
	 */
	public static Object getId(Object entity) {
		Class<?> clazz = deproxifyCglibClass(entity.getClass());

		Object accessor = idAccessor.get(clazz);

		if (accessor == null) {

			accessor = findIdField(clazz);

			if (accessor == null) {
				accessor = findIdMethod(clazz);
			}

			if (accessor == null) {
				Class<? extends Object> superclass = clazz.getSuperclass();

				while (accessor == null && superclass != null) {
					accessor = findIdField(superclass);

					if (accessor == null) {
						accessor = findIdMethod(superclass);
					}

					superclass = superclass.getSuperclass();
				}
			}

			if (accessor != null) {
				idAccessor.put(clazz, accessor);
			}
		}

		if (accessor != null) {
			if (accessor instanceof Method) {
				try {
					return ((Method) accessor).invoke(entity, new Object[] {});
				} catch (Exception e) {
					log.error(e);
				}
			} else if (accessor instanceof Field) {
				try {
					return ((Field) accessor).get(entity);
				} catch (Exception e) {
					log.error(e);
				}
			}
		}

		throw new IllegalStateException("Unable to get ID for entity, see error log for details");
	}

	/**
	 * Attempts to find a field annotated with Id.
	 * 
	 * @param clazz
	 *            the class to examine
	 * @return the annotated field or null
	 */
	public static Field findIdField(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {

			if (field.getAnnotation(Id.class) == null) {
				continue;
			}

			field.setAccessible(true);
			return field;
		}

		Class<?> superclass = clazz.getSuperclass();

		if (superclass != null) {
			return findIdField(superclass);
		}

		return null;
	}

	/**
	 * Attempts to find a method annotated with Id.
	 * 
	 * @param clazz
	 *            the class to examine
	 * @return the annotated method or null
	 */
	public static Method findIdMethod(Class<?> clazz) {
		for (Method method : clazz.getMethods()) {

			if (method.getParameterTypes().length != 0 || method.getAnnotation(Id.class) == null) {
				continue;
			}

			return method;
		}

		return null;
	}
}
