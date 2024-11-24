package enderwrapper.internal;

public final class Native {
    static {
        System.loadLibrary("lib");
    }
    public static native byte[] getClassBytecode(Class<?> clazz);
}
