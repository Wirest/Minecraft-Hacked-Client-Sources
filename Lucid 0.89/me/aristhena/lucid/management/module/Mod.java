/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.management.module;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Mod {
    String name() default "null";
    
    int keybind() default -1;
    
    boolean shown() default true;
    
    boolean enabled() default false;
    
    String suffix() default "";
}


