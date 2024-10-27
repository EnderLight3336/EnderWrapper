package me.enderlight3336.wrapper.log;

import internal.console.Console;
import internal.log.LogUtil;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalTime;

public final class Logger {
    final String name;
    @SuppressWarnings("NullableProblems")
    static final PrintWriter writer = new PrintWriter(new Writer() {
        @Override
        public void write(char[] chars, int off, int len) {throw new UnsupportedOperationException();}
        @Override
        public void flush() {throw new UnsupportedOperationException();}
        @Override
        public void close() {throw new UnsupportedOperationException();}
    }) {
        @Override
        public void println(Object x) {
            println(x.toString());
        }

        @Override
        public void println(String x) {
            String str = x + "\n";
            if (Console.ENABLE)
                Console.print(str);
            LogUtil.write(str);
        }
    };
    public Logger(String name) {
        this.name = name;
    }
    public void log(@NotNull Level level, @NotNull String str) {
        String build = "["+ LocalTime.now() +"] ["+name +"/"+level.name()+"] " + str;
        log0(build);
    }
    static void log0(String str) {
        synchronized (writer) {
            writer.println(str);
        }
    }
    public void catchThrow(@NotNull Throwable throwable, @NotNull Level level, String msg) {
        String build = "["+ LocalTime.now() +"] ["+name +"/"+level.name()+"] " + msg;
        synchronized (writer) {
            writer.println(build);
            throwable.printStackTrace(writer);
        }
    }
    static final String s0 = "Catch a throw!";
    public void catchThrow(Throwable throwable, Level level) {
        catchThrow(throwable, level, s0);
    }
    public void debug(String str) {
        log(Level.DEBUG, str);
    }
    public void info(String str) {
        log(Level.INFO, str);
    }
    public void warn(String str) {
        log(Level.WARN, str);
    }
    public void error(String str) {
        log(Level.ERROR, str);
    }
    public void fatal(String str) {
        log(Level.FATAL, str);
    }
    public enum Level{
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL
    }
}
