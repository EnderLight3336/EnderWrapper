package me.enderlight3336.wrapper.security;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("unused")
public class AccessController {
    public final boolean defV;
    public AccessController(boolean def) {
        this.defV = def;
    }
    public static AccessController emptyTrue = of(true);
    public static @NotNull AccessController of(JSONObject json) throws JSONException {
        if (json == null)
            return emptyTrue;
        if (!json.containsKey("defValue"))
            throw new JSONException("Miss key \"defValue\"");
        JSONObject json1 = json.getJSONObject("overrides");
        boolean defV = json.getBooleanValue("defValue");
        if (json1 == null || json1.isEmpty())
            return new AccessController(defV);
        return switch (json1.size()) {
            case 1 -> {
                Domain domain;
                if ((domain = Domain.ofClass(json)) == null &&
                        (domain = Domain.ofPackage(json)) == null &&
                        (domain = Domain.ofModule(json)) == null)
                    throw new JSONException("Invalid JSON!");
                yield new AccessController1(defV, domain);
            }
            case 2 -> {
                Domain domain1, domain2;
                if ((domain1 = Domain.ofClass(json)) == null) {
                    if ((domain1 = Domain.ofPackage(json)) == null)
                        throw new JSONException("Invalid JSON!");
                    else if ((domain2 = Domain.ofModule(json)) == null) {
                        throw new JSONException("Invalid JSON!");
                    }
                } else if ((domain2 = Domain.ofPackage(json)) == null) {
                    if ((domain2 = Domain.ofModule(json)) == null)
                        throw new JSONException("Invalid JSON!");
                }
                yield new AccessController2(defV, domain1, domain2);
            }
            case 3 -> {
                Domain domain1 = Domain.ofClass(json), domain2 = Domain.ofPackage(json), domain3 = Domain.ofModule(json);
                if (domain1 == null || domain2 == null || domain3 == null)
                    throw new JSONException("Invalid JSON!");
                yield new AccessController3(defV, domain1, domain2, domain3);
            }
            default -> throw new JSONException("Invalid format, \"overrides\" can only have 3 entry at most!");
        };
    }

    public static @NotNull AccessController of(boolean defValue) {
        return new AccessController(defValue);
    }

    public static @NotNull AccessController of(boolean defValue, @NotNull Domain d1) {
        return new AccessController1(defValue, d1);
    }

    public static @NotNull AccessController of(boolean defValue, @NotNull Domain d1, @NotNull Domain d2) {
        return new AccessController2(defValue, d1, d2);
    }

    public static @NotNull AccessController of(boolean defValue, @NotNull Domain d1, @NotNull Domain d2, @NotNull Domain d3) {
        return new AccessController3(defValue, d1, d2, d3);
    }

    public static @NotNull AccessController of(boolean defValue, Domain @NotNull ... domains) {
        return switch (domains.length) {
            case 0 -> new AccessController(defValue);
            case 1 -> new AccessController1(defValue, domains[0]);
            case 2 -> new AccessController2(defValue, domains[0], domains[1]);
            case 3 -> new AccessController3(defValue, domains[0], domains[1], domains[2]);
            default -> new AccessControllerM(defValue, domains);
        };
    }

    public boolean canAccess(Class<?> clazz) {
        return defV;
    }

    public final void checkAccess(Class<?> clazz) {
        if (!canAccess(clazz))
            throw new IllegalCallerException();
    }

    public static class AccessController1 extends AccessController {
        public final Domain domain;
        public AccessController1(boolean def, Domain domain) {
            super(def);

            this.domain = domain;
        }

        @Override
        public boolean canAccess(Class<?> clazz) {
            if (domain.isIn(clazz))
                return !defV;
            else
                return defV;
        }
    }
    public static class AccessController2 extends AccessController {
        public final Domain d1;
        public final Domain d2;
        public AccessController2(boolean def, Domain d1, Domain d2) {
            super(def);

            this.d1 = d1;
            this.d2 = d2;
        }

        @Override
        public boolean canAccess(Class<?> clazz) {
            if (d1.isIn(clazz) || d2.isIn(clazz))
                return !defV;
            else
                return defV;
        }
    }
    public static class AccessController3 extends AccessController {
        public final Domain d1;
        public final Domain d2;
        public final Domain d3;
        public AccessController3(boolean def, Domain d1, Domain d2, Domain d3) {
            super(def);

            this.d1 = d1;
            this.d2 = d2;
            this.d3 = d3;
        }

        @Override
        public boolean canAccess(Class<?> clazz) {
            if (d1.isIn(clazz) || d2.isIn(clazz) || d3.isIn(clazz))
                return !defV;
            else
                return defV;
        }
    }
    public static class AccessControllerM extends AccessController {
        final Domain[] domains;
        public AccessControllerM(boolean def, Domain... domains) {
            super(def);
            this.domains = domains;
        }

        @Override
        public boolean canAccess(Class<?> clazz) {
            for (Domain domain : domains)
                if (domain.isIn(clazz))
                    return !defV;
            return defV;
        }

        @Contract(pure = true)
        public Domain getDomain(int index) {
            return domains[index];
        }

        @Contract(pure = true)
        public Domain[] getDomains() {
            return Arrays.copyOf(domains, domains.length);
        }
    }
}
