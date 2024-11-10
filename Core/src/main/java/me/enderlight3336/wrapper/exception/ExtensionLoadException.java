package me.enderlight3336.wrapper.exception;

import enderwrapper.internal.extension.UnresolvedExt;

import java.util.ArrayList;
import java.util.List;

public class ExtensionLoadException extends Exception {
    public final UnresolvedExt causeExt;
    public final List<UnresolvedExt> influenceList = new ArrayList<>();
    public ExtensionLoadException(UnresolvedExt causeExt, String s) {
        super(s);

        this.causeExt = causeExt;
    }

    public ExtensionLoadException(UnresolvedExt causeExt, String message, Throwable cause) {
        super(message, cause);
        this.causeExt = causeExt;
    }

    public ExtensionLoadException(UnresolvedExt causeExt, Throwable cause) {
        super(cause);
        this.causeExt = causeExt;
    }

    @Override
    public String getMessage() {
        if (influenceList.isEmpty()) {
            return "cause=" + causeExt.name;
        }
        return "cause='" + causeExt.name + "' influence=" + influenceList;
    }
}
