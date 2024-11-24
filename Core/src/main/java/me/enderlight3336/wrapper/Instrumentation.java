package me.enderlight3336.wrapper;

import com.alibaba.fastjson2.JSONObject;
import jdk.internal.reflect.Reflection;
import enderwrapper.internal.instrument.InstrumentUtil;
import enderwrapper.internal.Util;
import me.enderlight3336.wrapper.security.AccessContainer;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.UnmodifiableClassException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public class Instrumentation implements java.lang.instrument.Instrumentation {
    public static final Instrumentation instance = Util.config.getBooleanValue("SecureInstrument") ? new SecureInstrumentation() : new Instrumentation();
    private Instrumentation() {}
    @Override
    public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
        InstrumentUtil.addTransformer(transformer, canRetransform);
    }
    @Override
    public void addTransformer(ClassFileTransformer transformer) {
        InstrumentUtil.addTransformer(transformer, true);
    }
    @Override
    public boolean removeTransformer(ClassFileTransformer transformer) {
        return InstrumentUtil.removeTransformer(transformer);
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
        return true;
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
        public static final SecureInstrumentation instance = Instrumentation.instance instanceof SecureInstrumentation ? (SecureInstrumentation) Instrumentation.instance : null;
        private SecureInstrumentation() {
            super();

            JSONObject config = Util.config;
            acAddTransformer = AccessContainer.of(config.getJSONObject("AddTransformer"));
            acRetransformClass = AccessContainer.of(config.getJSONObject("AddTransformer"));
            acRedefineClass = AccessContainer.of(config.getJSONObject("AddTransformer"));
            acAppendBootstrapSearch = AccessContainer.of(config.getJSONObject("AddTransformer"));
            acAppendSystemSearch = AccessContainer.of(config.getJSONObject("AddTransformer"));
            acSetNativeMethodPrefix = AccessContainer.of(config.getJSONObject("AddTransformer"));
            acRedefineModule = AccessContainer.of(config.getJSONObject("AddTransformer"));
        }
        final AccessContainer acAddTransformer, acRetransformClass, acRedefineClass, acAppendBootstrapSearch, acAppendSystemSearch, acSetNativeMethodPrefix, acRedefineModule;

        @Override
        public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
            acAddTransformer.checkAccess(Reflection.getCallerClass());
            super.addTransformer(transformer, canRetransform);
        }

        @Override
        public void addTransformer(ClassFileTransformer transformer) {
            acAddTransformer.checkAccess(Reflection.getCallerClass());
            super.addTransformer(transformer);
        }

        @Override
        public boolean removeTransformer(ClassFileTransformer transformer) {
            return super.removeTransformer(transformer);
        }

        @Override
        public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
            super.retransformClasses(classes);
        }

        @Override
        public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException, SecurityException {
            super.redefineClasses(definitions);
        }

        @Override
        public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {
            acAppendBootstrapSearch.checkAccess(Reflection.getCallerClass());
            super.appendToBootstrapClassLoaderSearch(jarfile);
        }

        @Override
        public void appendToSystemClassLoaderSearch(JarFile jarfile) {
            acAppendBootstrapSearch.checkAccess(Reflection.getCallerClass());
            super.appendToSystemClassLoaderSearch(jarfile);
        }

        @Override
        public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
            super.setNativeMethodPrefix(transformer, prefix);
        }

        @Override
        public void redefineModule(Module module, Set<Module> extraReads, Map<String, Set<Module>> extraExports, Map<String, Set<Module>> extraOpens, Set<Class<?>> extraUses, Map<Class<?>, List<Class<?>>> extraProvides) {
            super.redefineModule(module, extraReads, extraExports, extraOpens, extraUses, extraProvides);
        }
    }
}
