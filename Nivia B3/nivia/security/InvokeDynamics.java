package nivia.security;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface InvokeDynamics {
	boolean enabled() default true;

}
