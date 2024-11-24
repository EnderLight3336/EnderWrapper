package me.enderlight3336.wrapper.security;

import java.lang.instrument.ClassFileTransformer;

@SuppressWarnings("unused")
public interface SecurityProvider {
    int ALLOW = 1;
    int DEF = 0;
    int DENY = -1;
    int FORCE_ALLOW = 4;
    int FORCE_DENY = -4;
    int canRedefineModule(Class<?> caller);
    int canAddExport(Class<?> caller);
    int canAddOpen(Class<?> caller);
    int canTransform(Class<? extends ClassFileTransformer> transformer, String className);
    int canRedefineClass(Class<? extends ClassFileTransformer> caller, Class<?> target);
}