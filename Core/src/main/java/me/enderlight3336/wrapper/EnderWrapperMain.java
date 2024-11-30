package me.enderlight3336.wrapper;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import enderwrapper.internal.ExtensionManager;
import enderwrapper.internal.instrument.InstrumentUtil;
import enderwrapper.internal.loader.ExtLoader;
import enderwrapper.internal.log.LogUtil;
import enderwrapper.internal.console.Console;
import enderwrapper.internal.security.SecurityManager;
import jdk.internal.module.Modules;
import me.enderlight3336.wrapper.log.Logger;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.module.ModuleDescriptor;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.jar.Manifest;

import static enderwrapper.internal.Util.config;

@SuppressWarnings("DataFlowIssue")
public final class EnderWrapperMain {
    static final Module selfMod = EnderWrapperMain.class.getModule();
    static final File PATH = new File(
            new File(EnderWrapperMain.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                    .getParentFile().getParentFile(),
            ".internal");
    static final String version;
    static {
        try (InputStream input = EnderWrapperMain.class.getResource("/META-INF/MANIFEST.MF").openStream()) {
            version = new Manifest(input).getMainAttributes().getValue("Version");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("unused")
    public static void premain0(Instrumentation instrumentation, String agentArg) throws Throwable {
        Modules.addOpens(URL.class.getModule(),"java.net" , selfMod);
        Modules.addExports(System.class.getModule(), "jdk.internal.loader", selfMod);
        Modules.addExports(System.class.getModule(), "jdk.internal.reflect", selfMod);
        LogUtil.init();
        LogUtil.MAIN.info("============EnderWrapper============");
        LogUtil.MAIN.info("Version:  " + version);
        LogUtil.MAIN.info("Progress 1  : Initialization");
        if (!(instrumentation.isRedefineClassesSupported() && instrumentation.isRetransformClassesSupported() && instrumentation.isNativeMethodPrefixSupported())) {
            RuntimeException ex = new RuntimeException("Current runtime is not supported!");
            LogUtil.MAIN.logThrow(ex, Logger.Level.FATAL);
            throw ex;
        }
        InstrumentUtil.instrumentation = instrumentation;
        InstrumentUtil.init();

        JSONObject json;
        try (InputStream inputStream = Files.newInputStream(PATH.toPath().resolve("security.json"))) {
            json = JSON.parseObject(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LogUtil.MAIN.logThrow(e, Logger.Level.FATAL);
            throw e;
        }
        SecurityManager.init(json);

        String configPath = System.getProperty("internal.config");
        File configFile = (configPath == null ? new File(PATH, "config.json") : new File(configPath));
        if (!configFile.exists()) {
            if (configPath == null) {
                try (OutputStream out = Files.newOutputStream(configFile.toPath()); InputStream input = EnderWrapperMain.class.getResourceAsStream("/ExampleConfig.json")) {
                    out.write(input.readAllBytes());
                } catch (Exception ex) {
                    LogUtil.MAIN.logThrow(ex, Logger.Level.FATAL);
                    throw ex;
                }
                throw new RuntimeException("Config is generated, please ");
            } else {
                FileNotFoundException ex = new FileNotFoundException("Cannot find config at Path: " + configFile);
                LogUtil.MAIN.logThrow(ex, Logger.Level.FATAL);
                throw ex;
            }
        }

        try (InputStream input = Files.newInputStream(configFile.toPath())) {
            config = JSON.parseObject(input, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            LogUtil.MAIN.logThrow(ex, Logger.Level.FATAL);
            throw ex;
        }

        LogUtil.MAIN.info("Progress 2  : Resolving Extension");
        ExtensionManager.resolve(new File(PATH, "extensions"), config.getJSONArray("extra_extension"));

        ExtLoader.init(List.of());
        Console.setLoaded();
        ModuleDescriptor desc = selfMod.getDescriptor();
        for (ModuleDescriptor.Exports exports : desc.exports()) {
            if (exports.source().startsWith("enderwrapper.internal")) {}//TODO;undo
        }
    }
    public static File getPath() {
        return PATH;
    }
}
