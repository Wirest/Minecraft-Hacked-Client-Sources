// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@GwtCompatible
public @interface GwtCompatible {
    boolean serializable() default false;
    
    boolean emulated() default false;
}
