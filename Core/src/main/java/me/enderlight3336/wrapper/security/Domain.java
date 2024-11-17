package me.enderlight3336.wrapper.security;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.security.CodeSource;

@FunctionalInterface
public interface Domain {
    boolean isIn(Class<?> clazz);

    Domain empty = clazz -> false;
    static Domain of() {
        return empty;
    }
    @Contract("_ -> new")
    static @NotNull Domain ofClass(String @NotNull ... names) {
        return names.length == 1 ? new ClazzDomain(names[0]) : new ClassesDomain(names);
    }
    @Contract("_ -> new")
    static @NotNull Domain ofClass(@NotNull String name) {
        return new ClazzDomain(name);
    }

    /**
     * @throws ClassCastException If mapped value is not null and cannot cast to either {@link String} or {@link JSONArray}
     */
    @Contract("null -> fail")
    static @Nullable Domain ofClass(JSONObject jsonObject) throws ClassCastException {
        Object obj = jsonObject.get("classes");
        if (obj instanceof String s)
            return new ClazzDomain(s);
        else if (obj instanceof JSONArray array) {
            return ofClass(array.toArray(new String[0]));
        } else if (obj != null) {
            throw new ClassCastException();
        } else return null;
    }
    @Contract("_ -> new")
    static @NotNull Domain ofPackage(String @NotNull ... names) {
        return names.length == 1 ? new PkgDomain(names[0]) : new PkgsDomain(names);
    }
    @Contract("_ -> new")
    static @NotNull Domain ofPackage(@NotNull String name) {
        return new PkgDomain(name);
    }
    /**
     * @throws ClassCastException If mapped value is not null and cannot cast to either {@link String} or {@link JSONArray}
     */
    @Contract("null -> fail")
    static @Nullable Domain ofPackage(JSONObject jsonObject) throws ClassCastException {
        Object obj = jsonObject.get("packages");
        if (obj instanceof String s)
            return new PkgDomain(s);
        else if (obj instanceof JSONArray array) {
            return ofPackage(array.toArray(new String[0]));
        } else if (obj != null) {
            throw new ClassCastException();
        } else return null;
    }
    @Contract("_ -> new")
    static @NotNull Domain ofModule(String @NotNull ... names) {
        return names.length == 1 ? new ModDomain(names[0]) : new ModsDomain(names);//todo: undo for ALL-UNNAMED
    }
    /**
     * @throws ClassCastException If mapped value is not null and cannot cast to either {@link String} or {@link JSONArray}
     */
    @Contract("null -> fail")
    static @Nullable Domain ofModule(JSONObject jsonObject) throws ClassCastException {
        Object obj = jsonObject.get("modules");
        if (obj instanceof String s)
            return new ModDomain(s);
        else if (obj instanceof JSONArray array) {
            return ofModule(array.toArray(new String[0]));
        } else if (obj != null) {
            throw new ClassCastException();
        } else return null;
    }

    record ClassesDomain(String[] classesName) implements Domain {
        @Override
        public boolean isIn(Class<?> clazz) {
            for (String name : classesName)
                if (clazz.getName().equals(name))
                    return true;
            return false;
        }
    }
    record ClazzDomain(String clazzName) implements Domain {
        @Override
        public boolean isIn(Class<?> clazz) {
            return clazz.getName().equals(clazzName);
        }
    }
    record PkgsDomain(String[] pkgNames) implements Domain {
        @Override
        public boolean isIn(Class<?> clazz) {
            for (String name : pkgNames)
                if (clazz.getPackageName().equals(name))
                    return true;
            return false;
        }
    }
    record PkgDomain(String pkgName) implements Domain {
        @Override
        public boolean isIn(Class<?> clazz) {
                return clazz.getPackageName().equals(pkgName);
        }
    }
    record ModsDomain(String[] modNames) implements Domain {
        @Override
        public boolean isIn(Class<?> clazz) {
            for (String name : modNames)
                if (clazz.getPackageName().equals(name))
                    return true;
            return false;
        }
    }
    record ModDomain(String modName) implements Domain {
        @Override
        public boolean isIn(Class<?> clazz) {
            return modName.equals(clazz.getModule().getName());
        }
    }
    record JarLocDomain(String jarLoc) implements Domain {
        @Override
        public boolean isIn(Class<?> clazz) {
            CodeSource cs = clazz.getProtectionDomain().getCodeSource();
            if (cs != null) {
                URL url = cs.getLocation();
                if (url != null) {

                }
            }
            return false;
        }
    }
}
