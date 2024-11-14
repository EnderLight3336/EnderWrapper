package me.enderlight3336.wrapper.security;

public interface SecurityContext {
    boolean canAppendToBootstrapSearch(Class<?> caller);
    boolean canAppendToSystemSearch(Class<?> caller);
    public static SecurityContext get() {
        Class<?> caller = Reflection.getCaller();
        
    }
}
