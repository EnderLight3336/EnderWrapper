package me.enderlight3336.wrapper.security;

@FunctionalInterface
public interface AccessChecker {
    boolean check(Class<?> caller);
}
