package me.enderlight3336.wrapper.security;

import java.lang.instrument.ClassFileTransformer;

public interface SecurityProvider {
    boolean canAddExport(Class<?> caller, Module target);
    boolean canAddOpen(Class<?> caller, Module target);
    boolean canTransform(Class<? extends ClassFileTransformer> transformer, Class<?> registeror, String className);
    boolean canRedefineClass(Class<?> caller, Class<?> target);
}