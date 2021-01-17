package me.cupboard.command.argument;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {
    String[] handles() default { "@main" };
    
    String syntax() default "";
}
