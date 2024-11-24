package me.enderlight3336.wrapper.exception;

import enderwrapper.internal.extension.UnresolvedExt;

public class CycleLoadException extends ExtensionLoadException {
    public CycleLoadException(UnresolvedExt ext) {
        super(ext, "Found cycle load order!All extension under this cycle have been disabled!");
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder("Cycle:[").append(source.name).append(" -> ");
        influenceList.forEach(ext1 -> sb.append(ext1.name).append(" -> "));
        int length = sb.length();
        return sb.delete(length - 5, length - 1).append(source.name).append("]").toString();
    }
}
