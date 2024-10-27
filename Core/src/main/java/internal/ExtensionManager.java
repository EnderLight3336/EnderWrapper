package internal;

import com.alibaba.fastjson2.JSONArray;
import internal.extension.UnresolvedExt;
import internal.loader.ExtLoader;
import internal.log.LogUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import me.enderlight3336.wrapper.extension.Extension;
import me.enderlight3336.wrapper.log.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExtensionManager {
    public static Object2ObjectOpenHashMap<String, Extension> extensionMap = new Object2ObjectOpenHashMap<>();
    public static void resolve(@NotNull File path, JSONArray extList) {
        //Read all extension files
        File[] jars;
        if (!path.exists()) {
            path.mkdirs();
            jars = null;
        } else {
            jars = path.listFiles((dir, name) -> name.endsWith(".jar"));
        }
        ArrayList<File> files;boolean b = jars != null && jars.length != 0;
        if (extList != null && !extList.isEmpty()) {
            files = new ArrayList<>(extList.size());
            for (Object o : extList) {
                String str = (String) o;
                if (str != null) {
                    File file = new File(str);
                    if (file.isFile() && file.getName().endsWith(".jar")) {
                        files.add(file);
                        continue;
                    }
                }
                LogUtil.MAIN.warn("Find invalid jar, please check your config!\nDetail: " + str);
            }
            if (b) {
                files.ensureCapacity(files.size());
                Collections.addAll(files, jars);
            }
        } else if (b) {
            files = new ArrayList<>(jars.length);
            Collections.addAll(files, jars);
        } else return;
        //Start resolving
        final List<UnresolvedExt> unresolvedExts = new ReferenceArrayList<>(files.size());
        for (File file : files) {
            try {
                unresolvedExts.add(new UnresolvedExt(file));
            } catch (Exception e) {
                LogUtil.MAIN.catchThrow(e, Logger.Level.ERROR, "Error occurred while try to read JAR " + file.getPath());
            }
        }
        unresolvedExts.forEach(unresolvedExt -> unresolvedExt.build(unresolvedExts));
        while (!unresolvedExts.isEmpty()) {
            UnresolvedExt unresolvedExt = unresolvedExts.get(0);
            try {
                unresolvedExt.resolve(unresolvedExts);
            } catch (Exception ex) {
                LogUtil.MAIN.catchThrow(ex, Logger.Level.ERROR, "Error occurred while try to resolve EXTENSION " + unresolvedExt.name);
            }
        }
        ExtLoader.buildFindTree();
    }
}
