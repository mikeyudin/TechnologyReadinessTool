package net.techreadiness.ui.util;

import java.lang.reflect.Member;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletContext;

import net.techreadiness.persistence.AuditedBaseEntity;
import net.techreadiness.service.object.BaseObject;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.conversion.TypeConverter;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.inject.Inject;

public class ReadinessTypeConverterFactory extends XWorkConverter implements Function<String, TypeConverter> {
	/** Null object we will return from the lookup cache since ComputingMap does not cache null values **/
	private static final TypeConverter NONE = new TypeConverter() {
		@Override
		public Object convertValue(Map<String, Object> context, Object target, Member member, String propertyName,
				Object value, Class toType) {
			return null;
		}
	};

	private BaseEntityTypeConverter baseEntityTypeConverter;
	private ServiceObjectTypeConverter baseObjectTypeConverter;
	private LoadingCache<String, TypeConverter> lookupCache;
	private TextProvider textProvider;

	@Inject
	public ReadinessTypeConverterFactory(ServletContext servletContext) {
		baseEntityTypeConverter = ContextUtils.getRequiredBeanOfType(BaseEntityTypeConverter.class, servletContext);
		baseObjectTypeConverter = ContextUtils.getRequiredBeanOfType(ServiceObjectTypeConverter.class, servletContext);
		lookupCache = CacheBuilder.newBuilder().build(CacheLoader.from(this));
	}

	@Override
	public TypeConverter lookup(String className) {
		TypeConverter typeConverter;
		try {
			typeConverter = lookupCache.get(className);
			if (typeConverter != NONE) {
				return typeConverter;
			}
		} catch (ExecutionException e) {
			// Ignore failure
		}

		return super.lookup(className);
	}

	@Override
	public TypeConverter apply(String className) {
		try {
			Class<?> clas = Class.forName(className);
			if (AuditedBaseEntity.class.isAssignableFrom(clas)) {
				return baseEntityTypeConverter;
			} else if (BaseObject.class.isAssignableFrom(clas)) {
				return baseObjectTypeConverter;
			} else if (Date.class.isAssignableFrom(clas)) {
				return new ConfigurableDateConverter(textProvider);
			}
		} catch (ClassNotFoundException ignore) {
			// Ignore failure
		}
		return NONE;
	}

	public TextProvider getTextProvider() {
		return textProvider;
	}

	@Inject
	public void setTextProvider(TextProvider textProvider) {
		this.textProvider = textProvider;
	}

}
