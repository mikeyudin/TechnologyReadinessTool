package net.techreadiness.aspect;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreSecured;
import net.techreadiness.security.PermissionCode;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.UserService;
import net.techreadiness.service.exception.AuthorizationException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecuredAspect {

	@Inject
	UserService userService;

	/**
	 * Setup our pointcut. This is looking for any public methods.
	 */
	@Pointcut("execution(public * *(..))")
	public void anyPublicMethod() {
		// Point cut definition
	}

	@Around("anyPublicMethod() && @annotation(coreSecured) && args(sc,..)")
	public Object interceptExtensionPoint(ProceedingJoinPoint p, CoreSecured coreSecured, ServiceContext sc)
			throws Throwable {
		PermissionCode[] permissionCodes = coreSecured.value();
		if (userService.hasPermission(sc, permissionCodes)) {
			return p.proceed();
		}
		throw new AuthorizationException("User: " + sc.getUserName() + " denied access to: " + p.toString());
	}
}
