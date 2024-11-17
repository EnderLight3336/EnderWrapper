package enderwrapper.internal.instrument;

import enderwrapper.internal.Util;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pay attention to thread SYNCHRONIZATION issues!!!!
 */
public final class InstrumentUtil {
    /**
     * Pay attention to thread SYNCHRONIZATION issues!!!!
     */
    public static final Reference2ObjectOpenHashMap<ClassFileTransformer, Class<?>> transMap = new Reference2ObjectOpenHashMap<>();
    /**
     * Pay attention to thread SYNCHRONIZATION issues!!!!
     */
    public static final Reference2ObjectOpenHashMap<ClassFileTransformer, Class<?>> unretransMap = new Reference2ObjectOpenHashMap<>();
    static final Class<?> defV = InstrumentUtil.class;
    public static Instrumentation instrumentation;
    public static volatile boolean write1 = false, write2 = false;
    public static volatile AtomicInteger reader1 = new AtomicInteger(0), reader2 = new AtomicInteger(1);

    public static void init() {
        instrumentation.addTransformer(Util.config.getBooleanValue("SecureTransformer", false) ? new SecureTransformer() : new DefaultTransformer(),
                Util.config.getBooleanValue("canRetransform", true));
    }

    public static void addTransformer(ClassFileTransformer transformer, @Nullable Class<?> v, boolean canRetransform) {
        if (v == null)
            v = defV;
        if (canRetransform) {
            writeLock1();
            transMap.put(transformer, v);
            writeUnlock1();
        } else {
            writeLock2();
            unretransMap.put(transformer, v);
            writeUnlock2();
        }
    }

    public static void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
        addTransformer(transformer, defV, canRetransform);
    }

    public static boolean removeTransformer(ClassFileTransformer transformer) {
        boolean ret;
        writeLock1();
        ret = transMap.remove(transformer) != null;
        writeUnlock1();
        if (ret)
            return true;
        else {
            writeLock2();
            ret = unretransMap.remove(transformer) != null;
            writeUnlock2();
            return ret;
        }
    }

    public static boolean removeFirstFindTransformer(Class<?> clazz) {
        writeLock1();
        Reference2ObjectMap.FastEntrySet<ClassFileTransformer, Class<?>> entrySet;
        for (Reference2ObjectMap.Entry<ClassFileTransformer, Class<?>> entry : entrySet = transMap.reference2ObjectEntrySet()) {
            if (entry.getValue() == clazz) {
                entrySet.remove(entry);
                writeUnlock1();
                return true;
            }
        }
        writeUnlock1();
        writeLock2();
        for (Reference2ObjectMap.Entry<ClassFileTransformer, Class<?>> entry : entrySet = unretransMap.reference2ObjectEntrySet()) {
            if (entry.getValue() == clazz) {
                entrySet.remove(entry);
                writeUnlock2();
                return true;
            }
        }
        writeUnlock2();
        return false;
    }

    public static void writeLock1() {
        while (write1) Thread.yield();//wait write lock release
        write1 = true; //get write lock
        while (reader1.get() != 0) Thread.yield();//wait all read lock release
    }

    public static void writeUnlock1() {
        write1 = false;
    }

    public static void readLock1() {
        while (write1) Thread.yield();//wait write lock release
        reader1.getAndIncrement();
    }

    public static void readUnlock1() {
        reader1.getAndDecrement();
    }

    public static void writeLock2() {
        while (write2) Thread.yield();//wait write lock release
        write2 = true; //get write lock
        while (reader2.get() != 0) Thread.yield();//wait all read lock release
    }

    public static void writeUnlock2() {
        write2 = false;
    }

    public static void readLock2() {
        while (write2) Thread.yield();//wait write lock release
        reader2.getAndIncrement();
    }

    public static void readUnlock2() {
        reader2.getAndDecrement();
    }
}
