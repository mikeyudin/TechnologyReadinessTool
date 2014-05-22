package net.techreadiness.aspect;

import javax.inject.Inject;

import net.techreadiness.annotation.CoreDataModificationStatus;
import net.techreadiness.service.DataModificationStatus;
import net.techreadiness.service.DataModificationStatus.ModificationState;
import net.techreadiness.service.ServiceContext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataModificationStatusAspect {

	@Inject
	DataModificationStatus dataModificationStatus;

	/**
	 * Setup our pointcut. This is looking for any usage of the annotation {@code CoreDataModificationStatus}.
	 */
	@Pointcut("bean(*ServiceImpl)")
	public void extensionPointcut() {
		// Point cut definition
	}

	@Order(value = 2)
	@Around("extensionPointcut() && @annotation(coreDataModificationStatus) && args(sc,..)")
	public Object interceptExtensionPoint(ProceedingJoinPoint p, CoreDataModificationStatus coreDataModificationStatus,
			ServiceContext sc) throws Throwable {
		Object ret = null;

		dataModificationStatus.setModificationType(coreDataModificationStatus.modificationType());
		dataModificationStatus.setModificationState(ModificationState.REQUESTED);
		try {
			ret = p.proceed();
		} catch (Throwable e) {
			dataModificationStatus.setModificationState(ModificationState.FAILURE);

			throw e;
		}

		dataModificationStatus.setModificationState(ModificationState.SUCCESS);

		return ret;
	}
}
