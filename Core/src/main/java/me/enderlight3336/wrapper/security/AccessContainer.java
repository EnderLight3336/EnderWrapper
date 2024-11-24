package me.enderlight3336.wrapper.security;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * This class provide a lot of useful feature to determine access operation
 */
@SuppressWarnings("unused")
public class AccessContainer {
    public final boolean defV;
    AccessContainer(boolean def) {
        this.defV = def;
    }
    public static AccessContainer emptyTrue = of(true);

    /**
     * JSON Specification<p>
     * {
     *     "defValue" : true/false,
     *     "inversion" : {(optional, see json1)},
     *     "allow" : {(optional, see json1)},
     *     "deny" : {(optional, see json1)}
     * }<p>
     * "json1" : {
     *     "class" : [(String...)],
     *     "module" : [(String...)],
     *     "package" : [(String...)],
     *     "jar" : [(JAR_PATH_String...)]
     * }<p>
     * Key "defValue" is necessary, other key is optional
     */
    public static @NotNull AccessContainer of(JSONObject json) throws JSONException {
        if (json == null)
            return emptyTrue;
        if (!json.containsKey("defValue"))
            throw new JSONException("Miss key \"defValue\"");
        JSONObject json1 = json.getJSONObject("overrides");
        boolean defV = json.getBooleanValue("defValue");
        if (json1 == null || json1.isEmpty())
            return new AccessContainer(defV);
        return switch (json1.size()) {
            case 1 -> {
                Domain domain;
                if ((domain = Domain.ofClass(json)) == null &&
                        (domain = Domain.ofPackage(json)) == null &&
                        (domain = Domain.ofModule(json)) == null)
                    throw new JSONException("Invalid JSON!");
                yield new FlipImpl1(defV, domain);
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
                yield new FlipImpl2(defV, domain1, domain2);
            }
            case 3 -> {
                Domain domain1 = Domain.ofClass(json), domain2 = Domain.ofPackage(json), domain3 = Domain.ofModule(json);
                if (domain1 == null || domain2 == null || domain3 == null)
                    throw new JSONException("Invalid JSON!");
                yield new FlipImpl3(defV, domain1, domain2, domain3);
            }
            default -> throw new JSONException("Invalid format, \"overrides\" can only have 3 entry at most!");
        };
    }

    public static @NotNull AccessContainer of(boolean defValue) {
        return new AccessContainer(defValue);
    }

    public static @NotNull AccessContainer ofF(boolean defValue, @NotNull Domain d1) {
        return new FlipImpl1(defValue, d1);
    }

    public static @NotNull AccessContainer ofF(boolean defValue, @NotNull Domain d1, @NotNull Domain d2) {
        return new FlipImpl2(defValue, d1, d2);
    }

    public static @NotNull AccessContainer ofF(boolean defValue, @NotNull Domain d1, @NotNull Domain d2, @NotNull Domain d3) {
        return new FlipImpl3(defValue, d1, d2, d3);
    }

    public static @NotNull AccessContainer ofF(boolean defValue, Domain @NotNull ... domains) {
        return switch (domains.length) {
            case 0 -> new AccessContainer(defValue);
            case 1 -> new FlipImpl1(defValue, domains[0]);
            case 2 -> new FlipImpl2(defValue, domains[0], domains[1]);
            case 3 -> new FlipImpl3(defValue, domains[0], domains[1], domains[2]);
            default -> new FlipImplM(defValue, domains);
        };
    }
    public static @NotNull AccessContainer ofM(boolean defValue, Domain allow, Domain deny) {
        return new ImplA(defValue, allow, deny);
    }
    public static @NotNull AccessContainer ofM(boolean defValue, Domain @NotNull [] allow, Domain @NotNull [] deny) {
        return new ImplM(defValue, allow, deny);
    }

    public boolean canAccess(Class<?> clazz) {
        return defV;
    }

    public final void checkAccess(Class<?> clazz) {
        if (!canAccess(clazz))
            throw new IllegalCallerException();
    }

    public static class FlipImpl1 extends AccessContainer {
        public final Domain domain;
        public FlipImpl1(boolean def, Domain domain) {
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
    public static class FlipImpl2 extends AccessContainer {
        public final Domain d1, d2;
        public FlipImpl2(boolean def, Domain d1, Domain d2) {
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
    public static class FlipImpl3 extends AccessContainer {
        public final Domain d1, d2, d3;
        public FlipImpl3(boolean def, Domain d1, Domain d2, Domain d3) {
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
    public static class FlipImplM extends AccessContainer {
        final Domain[] domains;
        public FlipImplM(boolean def, Domain... domains) {
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
    public static class ImplA extends AccessContainer {
        final Domain a1, d1;
        ImplA(boolean def, Domain a1, Domain d1) {
            super(def);
            this.a1 = a1;
            this.d1 = d1;
        }

        @Override
        public boolean canAccess(Class<?> clazz) {
            if (a1.isIn(clazz))
                return true;
            if (d1.isIn(clazz))
                return false;
            return defV;
        }
    }
    public static class ImplM extends AccessContainer {
        final Domain[] allow, deny;
        public ImplM(boolean def, Domain[] allow, Domain[] deny) {
            super(def);
            this.allow = allow;
            this.deny = deny;
        }

        @Override
        public boolean canAccess(Class<?> clazz) {
            for (Domain domain : allow)
                if (domain.isIn(clazz))
                    return true;
            for (Domain domain : deny)
                if (domain.isIn(clazz))
                    return false;
            return defV;
        }
    }
}
