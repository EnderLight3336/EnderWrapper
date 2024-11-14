package enderwrapper.internal.security;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import me.enderlight3336.wrapper.security.NamedModuleSP;

import java.util.Map;

public final class SecurityUtil {
    public static Map<Module, NamedModuleSP> MODULE_SP_MAP = new Reference2ObjectOpenHashMap<>();
}
