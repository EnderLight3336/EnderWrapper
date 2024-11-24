package me.enderlight3336.wrapper.security;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import enderwrapper.internal.log.LogUtil;
import enderwrapper.internal.security.SecurityUtil;
import me.enderlight3336.wrapper.log.Logger;

import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.nio.charset.StandardCharsets;

public interface NamedModuleSP extends SecurityProvider{
    @SuppressWarnings("unchecked")
    static SecurityProvider of(Module module) {
        SecurityProvider ret = SecurityUtil.MODULE_SP_MAP.get(module);
        if (ret == null) {
            try (InputStream stream = module.getResourceAsStream("enderwrapper.json")) {
                if (stream != null) {
                    JSONObject json = JSON.parseObject(stream, StandardCharsets.UTF_8);
                    String provider = json.getString("SecurityProvider");
                    ret = provider != null ? ((Class<? extends SecurityProvider>) Class.forName(module, provider)).newInstance() :
                            new CustomSP(json);
                } else ret = new ImplSP(module);
            } catch (Throwable e) {
                LogUtil.MAIN.logThrow(e, Logger.Level.ERROR, "Catch a throw! Module: " + module.getName() + " has been used default SecurityProvider!");
                ret = new ImplSP(module);
            }
            SecurityUtil.MODULE_SP_MAP.put(module, ret);
        }
        return ret;
    }
    //todo:undo
    class ImplSP implements NamedModuleSP{
        final Module module;
        public ImplSP(Module module) {
            this.module = module;
        }
        @Override
        public int canRedefineModule(Class<?> caller) {
            return -1;
        }

        @Override
        public int canAddExport(Class<?> caller) {
            return -1;
        }

        @Override
        public int canAddOpen(Class<?> caller) {
            return -1;
        }

        @Override
        public int canTransform(Class<? extends ClassFileTransformer> transformer, String className) {
            return -1;
        }

        @Override
        public int canRedefineClass(Class<? extends ClassFileTransformer> caller, Class<?> target) {
            return -1;
        }
    }
    class CustomSP implements NamedModuleSP {
        CustomSP(JSONObject json) {}
        final AccessContainer ac1;
        @Override
        public int canRedefineModule(Class<?> caller) {
            for (Domain domain : a1)
                if (domain.isIn(caller))
                    return 1;
            for (Domain domain : d1)
                if (domain.isIn(caller))
                    return -1;
            return 0;
        }

        @Override
        public int canAddExport(Class<?> caller) {
            return 0;
        }

        @Override
        public int canAddOpen(Class<?> caller) {
            return 0;
        }

        @Override
        public int canTransform(Class<? extends ClassFileTransformer> transformer, String className) {
            return 0;
        }

        @Override
        public int canRedefineClass(Class<? extends ClassFileTransformer> caller, Class<?> target) {
            return 0;
        }
    }
}