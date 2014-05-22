package net.techreadiness.persistence;

import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

/**
 * The case of the alias names is vendor specific (Oracle and others return all upper case) which caused the standard
 * AliasToBeanResultTransformer to not find the appropriate getter and setter. This code was posted as a potential
 * enhancement to hibernate ... https://hibernate.onjira.com/browse/HHH-5815.
 */
public class IgnoringCaseAliasToBeanResultTransformer implements ResultTransformer {

	private static final long serialVersionUID = -3779317531110592988L;

	// IMPL NOTE : due to the delayed population of setters (setters cached
	// for performance), we really cannot properly define equality for
	// this transformer

	@SuppressWarnings("rawtypes")
	private final Class resultClass;
	private final PropertyAccessor propertyAccessor;
	private Setter[] setters;
	private Field[] fields;

	@SuppressWarnings("rawtypes")
	public IgnoringCaseAliasToBeanResultTransformer(final Class resultClass) {
		if (resultClass == null) {
			throw new IllegalArgumentException("resultClass cannot be null");
		}
		this.resultClass = resultClass;
		propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[] {
				PropertyAccessorFactory.getPropertyAccessor(resultClass, null),
				PropertyAccessorFactory.getPropertyAccessor("field") });
		fields = this.resultClass.getDeclaredFields();
	}

	@Override
	public Object transformTuple(final Object[] tuple, final String[] aliases) {
		Object result;

		try {
			if (setters == null) {
				setters = new Setter[aliases.length];
				for (int i = 0; i < aliases.length; i++) {
					String alias = aliases[i];
					if (alias != null) {
						Setter setter;
						try {
							setter = propertyAccessor.getSetter(resultClass, alias);
						} catch (final PropertyNotFoundException e) {
							for (final Field field : fields) {
								final String fieldName = field.getName();
								if (fieldName.equalsIgnoreCase(alias)) {
									alias = fieldName;
									break;
								}
							}
							setter = propertyAccessor.getSetter(resultClass, alias);
						}
						setters[i] = setter;
					}
				}
			}
			result = resultClass.newInstance();

			for (int i = 0; i < aliases.length; i++) {
				if (setters[i] != null) {
					setters[i].set(result, tuple[i], null);
				}
			}
		} catch (final InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName(), e);
		} catch (final IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName(), e);
		}

		return result;
	}

	@Override
	public List transformList(final List collection) {
		return collection;
	}

	@Override
	public int hashCode() {
		int result;
		result = resultClass.hashCode();
		result = 31 * result + propertyAccessor.hashCode();
		return result;
	}
}