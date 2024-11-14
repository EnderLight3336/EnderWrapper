package me.enderlight3336.wrapper.security;

import jdk.internal.reflect.Reflection;

public interface SecurityContext {
    boolean canAppendToBootstrapSearch(Class<?> caller);
    boolean canAppendToSystemSearch(Class<?> caller);
    public static SecurityContext get() {
        Class<?> caller = Reflection.getCallerClass();
        
    }
}
