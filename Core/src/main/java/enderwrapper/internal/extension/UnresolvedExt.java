package enderwrapper.internal.extension;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import enderwrapper.internal.ExtensionManager;
import enderwrapper.internal.loader.ExtLoader;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import me.enderlight3336.wrapper.exception.CycleLoadException;
import me.enderlight3336.wrapper.exception.ExtensionLoadException;
import enderwrapper.internal.log.LogUtil;
import me.enderlight3336.wrapper.extension.Extension;
import me.enderlight3336.wrapper.log.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class UnresolvedExt {
    @NotNull
    public final File file;
    @NotNull
    public final String name,clazz;
    public boolean loading = false, failed = false;
    @NotNull
    public final JSONObject json;
    public final ReferenceArraySet<UnresolvedExt> beforeThis = new ReferenceArraySet<>(5);
    @NotNull
    public final List<String> dependencyList;
    public UnresolvedExt(@NotNull File file) throws IOException, NullPointerException, JSONException {
        this.file = file;
        try (JarFile jar = new JarFile(file)) {
            json = JSON.parseObject(jar.getInputStream(jar.getJarEntry("enderwrapper.json")));
        }
        if ((name = json.getString("name")) == null)
            throw new NullPointerException("KEY Name cannot be null!");
        if ((clazz = json.getString("extension")) == null)
            throw new NullPointerException("KEY Extension cannot be null!");
        List<String> l = (List) json.getJSONArray("dependencies");
        dependencyList = l != null ? l : List.of();
    }
    public void build(final List<UnresolvedExt> extList) {
        Collection<String> strList = (List) json.getJSONArray("beforeThis");
        if (!dependencyList.isEmpty()) {
            if (strList != null)
                strList.addAll(dependencyList);
            else
                strList = dependencyList;
        }
        if (!strList.isEmpty()) {
            strList.forEach(str -> extList.forEach(unresolvedExt -> {
                if (unresolvedExt.name.equals(str)) {
                    this.beforeThis.add(unresolvedExt);
                }
            }));
        }
        if (!(strList = (List) json.getJSONArray("afterThis")).isEmpty()) {
            strList.forEach(str -> extList.forEach(unresolvedExt -> {
                if (unresolvedExt.name.equals(str)) {
                    unresolvedExt.beforeThis.add(this);
                }
            }));
        }
    }
    public void resolve(List<UnresolvedExt> extList) throws ExtensionLoadException {
        if (loading) {
            extList.remove(this);
            failed = true;
            throw new CycleLoadException(this);
        }
        loading = true;
        if (!beforeThis.isEmpty())
            for (UnresolvedExt ext : beforeThis) {
                try {
                    ext.resolve(extList);
                } catch (ExtensionLoadException ex) {
                    if (dependencyList.contains(ext.name)) {
                        failed = true;
                        extList.remove(this);
                        ex.influenceList.add(this);
                        throw ex;
                    } else LogUtil.MAIN.logThrow(ex, Logger.Level.ERROR);
                }
            }
        extList.remove(this);
        Extension ext;
        try {
            ext = ExtLoader.loadExt(this);
        } catch (Exception e) {
            failed = true;
            extList.remove(this);
            throw new ExtensionLoadException(this, e);
        }
        ExtensionManager.extensionMap.put(ext.name, ext);
    }

    @Override
    public String toString() {
        return name;
    }
}
