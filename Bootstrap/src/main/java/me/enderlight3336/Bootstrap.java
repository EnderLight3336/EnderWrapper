package me.enderlight3336;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.module.ModuleFinder;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Bootstrap {
    public static void premain(String agentArg, Instrumentation inst)
            throws URISyntaxException, IOException, UnmodifiableClassException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Path path = Path.of(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        ModuleFinder finder = ModuleFinder.of(path.resolve("module"));
        ModuleLayer bootL = ModuleLayer.boot();
        ModuleLayer layer = bootL.defineModulesWithOneLoader(bootL.configuration().resolve(finder, ModuleFinder.of(), Set.of("me.enderlight3336.wrapper")), Thread.currentThread().getContextClassLoader());
        Module wrapper = layer.findModule("me.enderlight3336.wrapper").get();
        inst.redefineModule(Object.class.getModule(), Set.of(), Map.of("jdk.internal.module", Set.of(wrapper)), Map.of(), Set.of(), Map.of());
        wrapper.getClassLoader().loadClass("me.enderlight3336.wrapper.EnderWrapperMain").getMethod("premain0", Instrumentation.class, String.class).invoke(null, inst, agentArg);
    }
}
