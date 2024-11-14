package me.enderlight3336.wrapper.security;

import java.lang.instrument.ClassFileTransformer;

public interface SecurityProvider {
    boolean canRedefineModule(Class<?> caller);
    boolean canAddExport(Class<?> caller);
    boolean canAddOpen(Class<?> caller);
    boolean canTransform(Class<? extends ClassFileTransformer> transformer, Class<?> registeror, String className);
    boolean canRedefineClass(Class<?> caller, Class<?> target);
}