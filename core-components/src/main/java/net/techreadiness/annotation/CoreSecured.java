package net.techreadiness.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.techreadiness.security.CorePermissionCodes;

/**
 * Used to annotate a method that must be secured. Provide a permission set that will be consulted at runtime.
 * 
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CoreSecured {
	public CorePermissionCodes[] value();
}
