package me.enderlight3336.wrapper.security;

public interface NamedModuleSP extends SecurityProvider{
    boolean canRedefine(Class<?> caller);
    boolean canAddExport(Class<?> caller);
    boolean canAddOpen(Class<?> caller);
}