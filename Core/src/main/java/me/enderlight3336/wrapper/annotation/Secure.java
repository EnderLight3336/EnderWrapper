package me.enderlight3336.wrapper.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation 
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Secure {
    String permission();
}
