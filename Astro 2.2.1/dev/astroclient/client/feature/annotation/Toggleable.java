package dev.astroclient.client.feature.annotation;

import dev.astroclient.client.feature.Category;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Toggleable {

    String label();

    Category category();

    String description() default "";

    int bind() default Keyboard.KEY_NONE;

    boolean hidden() default false;

}
