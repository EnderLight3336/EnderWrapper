package enderwrapper.internal.loader;

import enderwrapper.internal.extension.UnresolvedExt;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import jdk.internal.loader.Resource;
import jdk.internal.loader.URLClassPath;
import me.enderlight3336.wrapper.extension.Extension;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖保佑             永无BUG
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？

@SuppressWarnings("removal")
public class ExtLoader extends URLClassLoader {
    static final ClassLoader PARENT = ExtLoader.class.getClassLoader();
    public static final Reference2ObjectOpenHashMap<Extension, ExtLoader> extMap = new Reference2ObjectOpenHashMap<>();
    public static final Object2ObjectOpenHashMap<String, ExtLoader> nameMap = new Object2ObjectOpenHashMap<>();
    public static ExtLoader[] loaderBuffer = null;
    static Field f1, f2;
    public static final MethodHandle handle1;
    static {
        ClassLoader.registerAsParallelCapable();
        Class<?> superClass = URLClassLoader.class;
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            f1 = superClass.getDeclaredField("ucp");
            f1.setAccessible(true);
            f2 = superClass.getDeclaredField("acc");
            f2.setAccessible(true);
            Method m = superClass.getDeclaredMethod("defineClass", String.class, Resource.class);
            m.setAccessible(true);
            handle1 = lookup.unreflect(m);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    final URLClassPath path;
    final AccessControlContext access;
    public ExtLoader(String extName, URL urls, ClassLoader parent) throws IllegalAccessException {
        super("Loader-" + extName, new URL[]{urls}, parent);

        path = (URLClassPath) f1.get(this);
        access = (AccessControlContext) f2.get(this);
    }
    @NotNull
    public static Extension loadExt(UnresolvedExt ext) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ExtLoader loader = nameMap.get(ext.name);
        Extension extension = (Extension) loader.findClass(ext.clazz).getDeclaredConstructors()[0].newInstance(ext.name, loader);
        extMap.put(extension, loader);
        return extension;
    }
    public static void init(List<UnresolvedExt> list) throws MalformedURLException, IllegalAccessException {
        for (UnresolvedExt ext : list) {
            ExtLoader loader = new ExtLoader(ext.name, ext.file.toURI().toURL(), PARENT);
            nameMap.put(ext.name, loader);
        }
        f1 = null;
        f2 = null;
        loaderBuffer = nameMap.values().toArray(new ExtLoader[0]);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            Class<?> ret = null;
            for (ExtLoader loader : loaderBuffer)
                if (loader != this)
                    if ((ret = loader.findLoadedClass(name)) != null)
                        return ret;
            final String pathStr = name.replace('.', '/').concat(".class");
            for (ExtLoader loader : loaderBuffer) {
                try {
                    ret = AccessController.doPrivileged((PrivilegedExceptionAction<Class<?>>) () -> {
                        Resource res = loader.path.getResource(pathStr, false);
                        if (res != null) {
                            try {
                                //invoke    Class<?> defineClass(String name, Resource res)
                                return (Class<?>) handle1.invokeExact(loader, name, res);
                            } catch (ClassFormatError e2) {
                                if (res.getDataError() != null) {
                                    e2.addSuppressed(res.getDataError());
                                }
                                throw e2;
                            } catch (Throwable ex) {
                                throw new ClassNotFoundException(name, ex);
                            }
                        } else {
                            return null;
                        }
                    }, loader.access);
                } catch (PrivilegedActionException ignored) {}
                if (ret != null)
                    return ret;
            }
            throw e;
        }
    }
}
