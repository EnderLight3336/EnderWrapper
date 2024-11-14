package me.enderlight3336.wrapper.security;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import enderwrapper.internal.log.LogUtil;
import enderwrapper.internal.security.SecurityUtil;
import me.enderlight3336.wrapper.log.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.nio.charset.StandardCharsets;

public interface NamedModuleSP extends SecurityProvider{
    @SuppressWarnings("unchecked")
    static NamedModuleSP get(Module module) {
        NamedModuleSP ret = SecurityUtil.MODULE_SP_MAP.get(module);
        if (ret == null) {
            try (InputStream stream = module.getResourceAsStream("enderwrapper.json")) {
                if (stream != null) {
                    JSONObject json = JSON.parseObject(stream, StandardCharsets.UTF_8);
                    String provider = json.getString("SecurityProvider");
                    if (provider != null) {
                        ret = ((Class<? extends NamedModuleSP>) Class.forName(module, provider)).newInstance();
                    }
                } else
                    ret = new ImplSP(module);
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
        public boolean canRedefineModule(Class<?> caller) {
            return false;
        }

        @Override
        public boolean canAddExport(Class<?> caller) {
            return false;
        }

        @Override
        public boolean canAddOpen(Class<?> caller) {
            return false;
        }

        @Override
        public boolean canTransform(Class<? extends ClassFileTransformer> transformer, Class<?> registeror, String className) {
            return false;
        }

        @Override
        public boolean canRedefineClass(Class<?> caller, Class<?> target) {
            return false;
        }
    }
}