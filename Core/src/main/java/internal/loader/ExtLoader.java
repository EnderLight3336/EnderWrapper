package internal.loader;

import internal.extension.UnresolvedExt;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import me.enderlight3336.wrapper.extension.Extension;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ExtLoader extends URLClassLoader {
    static final ClassLoader pare = ExtLoader.class.getClassLoader();
    public static final Reference2ObjectOpenHashMap<Extension, ExtLoader> loaderMap = new Reference2ObjectOpenHashMap<>();
    static {
        ClassLoader.registerAsParallelCapable();
        //Load Class Extension
        //noinspection ResultOfMethodCallIgnored
        Extension.class.getName();
    }
    public ExtLoader(String extName, URL urls, ClassLoader parent) {
        super("Loader-" + extName, new URL[]{urls}, parent);
    }
    @NotNull
    public static Extension loadExt(UnresolvedExt ext) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ExtLoader loader = new ExtLoader(ext.name, ext.file.toURI().toURL(), pare);
        Extension extension = (Extension) loader.findClass(ext.clazz).getDeclaredConstructors()[0].newInstance(ext.name, loader);
        loaderMap.put(extension, loader);
        return extension;
    }
    public static void buildFindTree() {}

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ClassNotFoundException threw;
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            threw = e;
            for (ExtLoader loader : loaderMap.values())
                try {
                    return loader.findClass(name);
                } catch (ClassNotFoundException exc) {
                    exc.addSuppressed(threw);
                    threw = exc;
                }
            throw threw;
        }
    }
}
