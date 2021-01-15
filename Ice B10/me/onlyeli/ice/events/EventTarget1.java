package me.onlyeli.ice.events;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget1 {
    byte value() default 2;
}
