package internal;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import me.enderlight3336.wrapper.extension.Extension;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public final class InstrumentUtil {
    public static Instrumentation instrumentation;
    public static final Reference2ObjectArrayMap<Extension, List<ClassFileTransformer>> regTransformer = new Reference2ObjectArrayMap<>();
    public static final ReferenceArrayList<ClassFileTransformer> buffer = new ReferenceArrayList<>();
    public static final ArrayList<ClassFileTransformer> defTransformer = new ArrayList<>();
    public static void init() {
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes)
                    throws IllegalClassFormatException {
                return ClassFileTransformer.super.transform(loader, name, clazz, domain, bytes);
            }

            @Override
            public byte[] transform(Module module, ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes)
                    throws IllegalClassFormatException {
                int index = name.lastIndexOf("\\");
                String pkgName = index == -1 ? "" : name.substring(0, index);
                byte[] b = bytes;
                for (ClassFileTransformer transformer1 : buffer) {
                    if (!module.isOpen(pkgName, transformer1.getClass().getModule()))
                        continue;
                    byte[] ret = transformer1.transform(module, loader, name, clazz, domain, b);
                    if (ret != null)
                        b = ret;
                }
                return b == bytes ? null : b;
            }
        }, false);
    }
}
