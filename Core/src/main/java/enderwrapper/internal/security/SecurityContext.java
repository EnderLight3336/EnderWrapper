package enderwrapper.internal.security;

import com.alibaba.fastjson2.JSONObject;

public final class SecurityContext {
    public static final Reference2ObjectMap modContexts = new Reference2ObjectOpenHashMap();
    public static SecurityContext get(Class<?> caller) {
        Module module = caller.getModule();
        if (module.isNamed()) {
            SecurityContext context = modContexts.get(module);
            if (context != null) {
                return context;
            }
        }
        return null;
    }
    public static void load(JSONObject json) {}
    public static void loadExtra(JSONObject json) {}
}
