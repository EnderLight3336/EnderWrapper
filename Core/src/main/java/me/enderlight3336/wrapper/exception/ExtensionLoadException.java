package me.enderlight3336.wrapper.exception;

import enderwrapper.internal.extension.UnresolvedExt;

import java.util.ArrayList;
import java.util.List;

public class ExtensionLoadException extends Exception {
    public final UnresolvedExt source;
    public final List<UnresolvedExt> influenceList = new ArrayList<>();
    public ExtensionLoadException(UnresolvedExt source, String s) {
        super(s);

        this.source = source;
    }

    public ExtensionLoadException(UnresolvedExt source, String message, Throwable cause) {
        super(message, cause);
        this.source = source;
    }

    public ExtensionLoadException(UnresolvedExt source, Throwable cause) {
        super(cause);
        this.source = source;
    }

    @Override
    public String getMessage() {
        if (influenceList.isEmpty()) {
            return "cause=" + source.name;
        }
        return "cause='" + source.name + "' influence=" + influenceList;
    }
}
