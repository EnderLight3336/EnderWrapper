package me.enderlight3336.wrapper;

import com.alibaba.fastjson2.JSON;
import enderwrapper.internal.ExtensionManager;
import enderwrapper.internal.InstrumentUtil;
import enderwrapper.internal.loader.ExtLoader;
import enderwrapper.internal.log.LogUtil;
import enderwrapper.internal.console.Console;
import jdk.internal.module.Modules;
import me.enderlight3336.wrapper.log.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    static final Module wrapperModule = EnderWrapperMain.class.getModule();
    static final File path = new File(
            new File(EnderWrapperMain.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                    .getParentFile(),
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
    public static void premain0(Instrumentation instrumentation) throws Throwable {
        Modules.addOpens(URL.class.getModule(),"java.net" , wrapperModule);
        Modules.addExports(System.class.getModule(), "jdk.internal.loader", wrapperModule);
        Modules.addExports(System.class.getModule(), "jdk.internal.reflect", wrapperModule);
        LogUtil.init();
        if (!(instrumentation.isRedefineClassesSupported() && instrumentation.isRetransformClassesSupported() && instrumentation.isNativeMethodPrefixSupported())) {
            IllegalStateException ex = new IllegalStateException();
            LogUtil.MAIN.logThrow(ex, Logger.Level.FATAL);
            throw ex;
        }
        InstrumentUtil.instrumentation = instrumentation;

        LogUtil.MAIN.info("============EnderWrapper============");
        LogUtil.MAIN.info("Version:  " + version);
        LogUtil.MAIN.info("Progress 1  : Initialization");

        String configPath = System.getProperty("internal.config");
        File configFile = (configPath == null ? new File(path, "config.json") : new File(configPath));
        if (!configFile.exists()) {
            FileNotFoundException ex = new FileNotFoundException("Cannot find config at Path: " + configFile);
            LogUtil.MAIN.logThrow(ex, Logger.Level.FATAL);
            throw ex;
        }

        try (InputStream input = Files.newInputStream(configFile.toPath())) {
            config = JSON.parseObject(input, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            LogUtil.MAIN.logThrow(ex, Logger.Level.FATAL);
            throw ex;
        }

        LogUtil.MAIN.info("Progress 2  : Resolving Extension");
        ExtensionManager.resolve(new File(path, "extensions"), config.getJSONArray("extra_extension"));

        ExtLoader.init(List.of());
        Console.setLoaded();
        ModuleDescriptor descriptor = wrapperModule.getDescriptor();
    }
    public static File getPath() {
        return path;
    }
}
