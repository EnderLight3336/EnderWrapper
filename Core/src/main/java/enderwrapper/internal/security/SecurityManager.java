package enderwrapper.internal.security;

import com.alibaba.fastjson2.JSONObject;
import me.enderlight3336.wrapper.security.AccessContainer;

public final class SecurityManager {
    public static boolean ENABLE;
    public static AccessContainer transClass, redefineClass;
    public static void init(JSONObject json) {
        ENABLE = json != null && !json.getBooleanValue("enable");
        if (ENABLE) {
            JSONObject jo;
            if ((jo = json.getJSONObject("TransformClass")) != null) {
            }
        }
    }
}