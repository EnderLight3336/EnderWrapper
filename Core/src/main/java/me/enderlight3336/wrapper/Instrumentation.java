package me.enderlight3336.wrapper;

import jdk.internal.reflect.Reflection;
import enderwrapper.internal.InstrumentUtil;
import enderwrapper.internal.Util;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.UnmodifiableClassException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public class Instrumentation implements java.lang.instrument.Instrumentation {
    public static final Instrumentation instance = Util.config.getBooleanVaule("SecureInstrument") ? new SecureInstrumentation() : new Instrumentation();
    private Instrumentation() {}
    @Override
    public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
        InstrumentUtil.instrumentation.addTransformer(transformer, canRetransform);
    }
    @Override
    public void addTransformer(ClassFileTransformer transformer) {
        InstrumentUtil.instrumentation.addTransformer(transformer);
    }
    @Override
    public boolean removeTransformer(ClassFileTransformer transformer) {
        return InstrumentUtil.instrumentation.removeTransformer(transformer);
    }
    @Override
    public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
        InstrumentUtil.instrumentation.retransformClasses(classes);
    }
    @Override
    public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException, SecurityException {
        InstrumentUtil.instrumentation.redefineClasses(definitions);
    }
    @Override
    public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {
        InstrumentUtil.instrumentation.appendToBootstrapClassLoaderSearch(jarfile);
    }
    @Override
    public void appendToSystemClassLoaderSearch(JarFile jarfile) {
        InstrumentUtil.instrumentation.appendToSystemClassLoaderSearch(jarfile);
    }
    @Override
    public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
        InstrumentUtil.instrumentation.setNativeMethodPrefix(transformer, prefix);
    }
    @Override
    public void redefineModule(Module module, Set<Module> extraReads, Map<String, Set<Module>> extraExports, Map<String, Set<Module>> extraOpens, Set<Class<?>> extraUses, Map<Class<?>, List<Class<?>>> extraProvides) {
        InstrumentUtil.instrumentation.redefineModule(module, extraReads, extraExports, extraOpens, extraUses, extraProvides);
    }
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

    public static final class SecureInstrumentation extends Instrumentation {
        private SecureInstrumentation() {
            super();
        }
        Class<?>[] globalClass;
        Checker[] globalFunc;
        @FunctionalInterface
        public static interface Checker {
            boolean check(Class<?> caller);
        }
    }
}
