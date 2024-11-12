package me.enderlight3336.wrapper.security;

import java.lang.instrument.ClassFileTransformer;

public interface SecurityProvider {
    boolean canTransform(Class<? extends ClassFileTransformer> transformer, Class<?> registeror, String className);
    boolean canRedefineClass(Class<?> caller, Class<?> target);
    boolean canAppendToBootstrapSearch(Class<?> caller);
    boolean canAppendToSystemSearch(Class<?> caller);
}