package me.enderlight3336.wrapper.exception;

import internal.extension.UnresolvedExt;

import java.util.ArrayList;
import java.util.List;

public class ExtensionLoadException extends Exception {
    public final UnresolvedExt ext;
    public final List<UnresolvedExt> list = new ArrayList<>();
    public ExtensionLoadException(UnresolvedExt ext, String s) {
        super(s);

        this.ext = ext;
    }

    public ExtensionLoadException(UnresolvedExt ext, String message, Throwable cause) {
        super(message, cause);
        this.ext = ext;
    }

    public ExtensionLoadException(UnresolvedExt ext, Throwable cause) {
        super(cause);
        this.ext = ext;
    }

    @Override
    public String getMessage() {
        if (list.isEmpty()) {
            return "cause=" + ext.name;
        }
        return "cause='" + ext.name + "' influence=" + list;
    }
}
