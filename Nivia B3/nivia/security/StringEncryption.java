package nivia.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface StringEncryption {
	int level = 1;
	boolean enabled() default true;

}
