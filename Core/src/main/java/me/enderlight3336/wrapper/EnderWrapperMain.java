package me.enderlight3336.wrapper;

import com.alibaba.fastjson2.JSON;
import internal.ExtensionManager;
import internal.InstrumentUtil;
import internal.log.LogUtil;
import lombok.Getter;
import internal.console.Console;
import me.enderlight3336.wrapper.log.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.jar.Manifest;

import static internal.Util.config;

@SuppressWarnings("DataFlowIssue")
public final class EnderWrapperMain {
    @Getter
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
    public static void premain0(Instrumentation instrumentation) throws IOException {
        LogUtil.init();
        if (!(instrumentation.isRedefineClassesSupported() && instrumentation.isRetransformClassesSupported())) {
            IllegalStateException ex = new IllegalStateException();
            LogUtil.MAIN.catchThrow(ex, Logger.Level.FATAL);
            throw ex;
        }
        InstrumentUtil.instrumentation = instrumentation;
        //noinspection UnusedAssignment
        instrumentation = null;

        LogUtil.MAIN.info("============EnderWrapper============");
        LogUtil.MAIN.info("Version:  " + version);
        LogUtil.MAIN.info("Progress 1  : Initialization");

        String configPath = System.getProperty("internal.config");
        File configFile = configPath == null ? new File(path, "config.json") : new File(configPath);
        if (!configFile.exists()) {
            FileNotFoundException ex = new FileNotFoundException("Cannot find config at Path: " + configFile);
            LogUtil.MAIN.catchThrow(ex, Logger.Level.FATAL);
            throw ex;
        }

        try (InputStream input = Files.newInputStream(configFile.toPath())) {
            config = JSON.parseObject(input, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            LogUtil.MAIN.catchThrow(ex, Logger.Level.FATAL);
            throw ex;
        }

        LogUtil.MAIN.info("Progress 2  : Resolving Extension");
        ExtensionManager.resolve(new File(path, "extensions"), config.getJSONArray("extra_extension"));

        Console.setLoaded();
    }
}
