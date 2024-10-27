package me.enderlight3336;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Set;

public final class Bootstrap {
    public static void premain(String agentArg, Instrumentation inst)
            throws URISyntaxException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Module module;
        if (!agentArg.equals("debug")) {
            ModuleLayer layer = ModuleLayer.boot();
            Configuration par = layer.configuration();
            ModuleLayer l1 = layer.defineModulesWithOneLoader(
                    par.resolveAndBind(
                            ModuleFinder.of(Path.of(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().resolve("module")),
                            ModuleFinder.of(),
                            Set.of("internal")),
                    Bootstrap.class.getClassLoader());
            module = l1.findModule("internal").get();
        } else {
            module = ModuleLayer.boot().findModule("enderwrapper").get();
        }
        Class.forName(module, "me.enderlight3336.wrapper.EnderWrapperMain").getMethod("premain0", Instrumentation.class).invoke(null, inst);
    }
}
