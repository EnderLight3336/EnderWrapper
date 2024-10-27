module EnderWrapper {
    requires static lombok;
    requires static org.jetbrains.annotations;

    requires java.instrument;
    requires java.desktop;

    requires com.alibaba.fastjson2;
    requires it.unimi.dsi.fastutil;

    exports me.enderlight3336.wrapper;
    exports me.enderlight3336.wrapper.annotation;
    exports me.enderlight3336.wrapper.command;
    exports me.enderlight3336.wrapper.exception;
    exports me.enderlight3336.wrapper.extension;
    exports me.enderlight3336.wrapper.log;
    exports me.enderlight3336.wrapper.security;
    exports me.enderlight3336.wrapper.transformer;
    exports internal.console;
    exports internal.extension;
    exports internal.log;
}
