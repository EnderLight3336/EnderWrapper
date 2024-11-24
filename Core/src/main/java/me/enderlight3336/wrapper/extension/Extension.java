package me.enderlight3336.wrapper.extension;

import enderwrapper.internal.extension.UnresolvedExt;
import enderwrapper.internal.loader.ExtLoader;
import me.enderlight3336.wrapper.log.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;

public abstract class Extension {
    static {
        try {
            AccessibleObject.setAccessible(new AccessibleObject[]{ExtLoader.class.getMethod("loadExt", UnresolvedExt.class)}, true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    protected final Logger logger;
    public final String name;
    protected final ExtLoader loader;
    Extension(String name, ExtLoader loader) {
        this.name = name;
        this.logger = new Logger(name);
        this.loader = loader;
    }

    public abstract void init() throws Throwable;

    public abstract void preload() throws Throwable;

    public abstract void load() throws Throwable;

    @Override
    public final int hashCode() {
        return name.hashCode();
    }
}