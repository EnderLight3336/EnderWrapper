package me.enderlight3336.wrapper.security;

import java.lang.instrument.ClassFileTransformer;

public interface SecurityProvider {
    boolean canRedefineModule(Class<?> caller);
    boolean canAddExport(Class<?> caller);
    boolean canAddOpen(Class<?> caller);
    boolean canTransform(Class<? extends ClassFileTransformer> transformer, String className);
    boolean canRedefineClass(Class<? extends ClassFileTransformer> caller, Class<?> target);
    default void checkRedefineClass(Class<? extends ClassFileTransformer> caller, Class<?> target) {
        if (!canRedefineClass(caller, target))
            throw new IllegalCallerException();
    }
    default void checkTransform(Class<? extends ClassFileTransformer> transfformer, String className) {
        if (!canTransform(transfformer, className))
            throw new IllegalCallerException();
    }
}