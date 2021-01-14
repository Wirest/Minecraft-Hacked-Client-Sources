package de.iotacb.client.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

	public String[] names();
	public String description() default "";
	public String usage() default "";
	
}
