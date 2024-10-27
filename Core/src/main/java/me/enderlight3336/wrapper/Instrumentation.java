package me.enderlight3336.wrapper;

import internal.InstrumentUtil;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.UnmodifiableClassException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public final class Instrumentation implements java.lang.instrument.Instrumentation {
    public static final Instrumentation instance = new Instrumentation();
    public Instrumentation() {}
    @Override
    public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {

    }
    @Override
    public void addTransformer(ClassFileTransformer transformer) {

    }
    @Override
    public boolean removeTransformer(ClassFileTransformer transformer) {
        return false;
    }
    @Override
    public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {

    }
    @Override
    public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException, SecurityException {
        for (ClassDefinition definition : definitions) {
            Class<?> clazz = definition.getDefinitionClass();
            if (!clazz.getModule().isOpen(clazz.getPackageName()))
                throw new SecurityException("You must have");
        }
        InstrumentUtil.instrumentation.redefineClasses(definitions);
    }
    @Override
    public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {}
    @Override
    public void appendToSystemClassLoaderSearch(JarFile jarfile) {}
    @Override
    public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {}
    @Override
    public void redefineModule(Module module, Set<Module> extraReads, Map<String, Set<Module>> extraExports, Map<String, Set<Module>> extraOpens, Set<Class<?>> extraUses, Map<Class<?>, List<Class<?>>> extraProvides) {}
    @Override
    public boolean isModifiableModule(Module module) {
        return InstrumentUtil.instrumentation.isModifiableModule(module);
    }
    @Override
    public boolean isNativeMethodPrefixSupported() {
        return InstrumentUtil.instrumentation.isNativeMethodPrefixSupported();
    }
    @Override
    public long getObjectSize(Object objectToSize) {
        return InstrumentUtil.instrumentation.getObjectSize(objectToSize);
    }
    @Override
    public boolean isModifiableClass(Class<?> theClass) {
        return InstrumentUtil.instrumentation.isModifiableClass(theClass);
    }
    @Override
    public boolean isRedefineClassesSupported() {
        return true;
    }
    @Override
    public boolean isRetransformClassesSupported() {
        return true;
    }
    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getAllLoadedClasses() {
        return InstrumentUtil.instrumentation.getAllLoadedClasses();
    }
    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getInitiatedClasses(ClassLoader loader) {
        return InstrumentUtil.instrumentation.getInitiatedClasses(loader);
    }
}
