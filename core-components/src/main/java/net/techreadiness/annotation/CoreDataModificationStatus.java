package net.techreadiness.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.techreadiness.persistence.BaseEntity;

/**
 * Used to annotate a service method that modifies data. An aspect will advise these methods and populate a request-scoped
 * bean that can be used by a front end to help display results to a user about the status of the actions they just
 * performed. e.g. "Changes saved successfully.".
 * 
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CoreDataModificationStatus {
	public enum ModificationType {
		NONE, UPDATE, DELETE, OTHER
	}

	public ModificationType modificationType();

	// Not using this yet, mainly for future use.
	public Class<? extends BaseEntity> entityClass();
}
