package enderwrapper.internal.security;

import com.alibaba.fastjson2.JSONObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import me.enderlight3336.wrapper.security.NamedModuleSP;
import me.enderlight3336.wrapper.security.SecurityProvider;

import java.util.Map;

public final class SecurityUtil {
    public static Map<Module, SecurityProvider> MODULE_SP_MAP = new Reference2ObjectOpenHashMap<>();
    public static Object2ObjectOpenHashMap<String, JSONObject> overDefSPConfig;
    public static void init(JSONObject json) {}
}
