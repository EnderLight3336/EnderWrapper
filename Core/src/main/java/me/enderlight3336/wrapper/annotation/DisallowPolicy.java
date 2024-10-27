package me.enderlight3336.wrapper.annotation;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({CONSTRUCTOR, FIELD, METHOD, PACKAGE, MODULE, TYPE, RECORD_COMPONENT})
@AllowPolicy(source = {}, packages = {}, modules = {})
public @interface DisallowPolicy {
    @Nullable String[] sources();
    @Nullable String[] packages();
    @Nullable String[] modules();
}
