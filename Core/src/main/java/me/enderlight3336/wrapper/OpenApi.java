package me.enderlight3336.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class OpenApi {
    @NotNull
    public static java.lang.instrument.Instrumentation getInstrumentation() {
        return Instrumentation.instance;
    }
    public static boolean isSecureInstrument() {
        return isSecureInstrument;
    }
    public static final boolean isSecureInstrument = Instrumentation.instance instanceof Instrumentation.SecureInstrumentation;

    /**
     * @return Null means SecureInstrument is disabled
     */
    @Nullable
    public static Instrumentation.SecureInstrumentation getSecureInstrumentation() {
        return Instrumentation.SecureInstrumentation.instance;
    }
}
