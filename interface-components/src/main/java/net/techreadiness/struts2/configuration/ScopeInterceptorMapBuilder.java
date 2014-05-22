package net.techreadiness.struts2.configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.DefaultInterceptorMapBuilder;
import org.apache.struts2.convention.StringTools;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.interceptor.ScopeInterceptor;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.config.entities.InterceptorConfig;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.util.AnnotationUtils;

public class ScopeInterceptorMapBuilder extends DefaultInterceptorMapBuilder {

	@Override
	public List<InterceptorMapping> build(Class<?> actionClass, PackageConfig.Builder builder, String actionName,
			Action annotation) {
		List<InterceptorMapping> interceptorList = new ArrayList<>(10);

		// from @InterceptorRefs annotation
		InterceptorRefs interceptorRefs = AnnotationUtils.findAnnotation(actionClass, InterceptorRefs.class);
		if (interceptorRefs != null) {
			interceptorList.addAll(build(actionClass, interceptorRefs.value(), actionName, builder));
		}

		// from @InterceptorRef annotation
		InterceptorRef interceptorRef = AnnotationUtils.findAnnotation(actionClass, InterceptorRef.class);
		if (interceptorRef != null) {
			interceptorList.addAll(build(actionClass, new InterceptorRef[] { interceptorRef }, actionName, builder));
		}

		// from @Action annotation
		if (annotation != null) {
			InterceptorRef[] interceptors = annotation.interceptorRefs();
			if (interceptors != null) {
				interceptorList.addAll(build(actionClass, interceptors, actionName, builder));
			}
		}

		return interceptorList;
	}

	protected List<InterceptorMapping> build(Class<?> actionClass, InterceptorRef[] interceptors, String actionName,
			PackageConfig.Builder builder) {
		List<InterceptorMapping> interceptorList = new ArrayList<>(10);
		for (InterceptorRef interceptor : interceptors) {

			Map<String, String> params = StringTools.createParameterMap(interceptor.params());
			List<String> fields = Lists.newArrayList();
			Object referencedConfig = builder.getInterceptorConfig(interceptor.value());
			if (referencedConfig instanceof InterceptorConfig) {
				InterceptorConfig interceptorConfig = (InterceptorConfig) referencedConfig;
				if (interceptorConfig.getClassName().equals(ScopeInterceptor.class.getName())) {
					List<Field> inheritedFields = getInheritedFields(actionClass);
					for (Field field : inheritedFields) {
						SessionScoped sessionScoped = field.getAnnotation(SessionScoped.class);
						if (sessionScoped != null) {
							fields.add(field.getName());
						}
					}
					String currentSession = params.get("session");
					String newSession = StringUtils.join(fields, ",");
					if (StringUtils.isNotBlank(currentSession)) {
						newSession = currentSession + "," + newSession;
					}
					params.put("session", newSession);

				}
			}
			interceptorList.addAll(buildInterceptorList(builder, interceptor, params));
		}

		return interceptorList;
	}

	protected static List<Field> getInheritedFields(Class<?> type) {
		List<Field> fields = Lists.newArrayList();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}

}
