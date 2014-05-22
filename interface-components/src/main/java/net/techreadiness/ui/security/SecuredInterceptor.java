package net.techreadiness.ui.security;

import java.lang.reflect.Method;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.security.PermissionCode;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.UserService;
import net.techreadiness.service.exception.AuthorizationException;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SecuredInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	private static final String SERVICE_CONTEXT = "serviceContext";

	@Inject
	protected UserService userService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		Class<?> c = action.getClass();
		Method m;
		if (invocation.getProxy().isMethodSpecified()) {
			m = c.getMethod(invocation.getProxy().getActionName(), (Class<?>[]) null);
		} else {
			m = c.getMethod("execute", (Class<?>[]) null);
		}

		if (m.isAnnotationPresent(CoreSecured.class)) {
			CoreSecured annotation = m.getAnnotation(CoreSecured.class);

			PermissionCode[] permissions = annotation.value();
			if (!userService.hasPermission(
					(ServiceContext) invocation.getInvocationContext().getSession().get(SERVICE_CONTEXT), permissions)) {
				throw new AuthorizationException("Access Denied");
			}
		}

		return invocation.invoke();
	}
}