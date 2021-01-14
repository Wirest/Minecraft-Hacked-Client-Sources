package com.mentalfrostbyte.jello.event;


import java.lang.annotation.*;

import com.mentalfrostbyte.jello.event.types.Priority;

/**
 * Marks a method so that the EventManager knows that it should be registered.
 * The priority of the method is also set with this.
 *
 * @author DarkMagician6
 * @see com.darkmagician6.eventapi.types.Priority
 * @since July 30, 2013
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {

    byte value() default Priority.MEDIUM;
}
