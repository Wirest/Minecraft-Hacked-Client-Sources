package de.iotacb.client.module;

import static java.lang.annotation.ElementType.CONSTRUCTOR;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {

	public String name();
	public String description() default "";
	Category category();
	int key() default 0;
	
}
