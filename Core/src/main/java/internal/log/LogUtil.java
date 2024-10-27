package internal.log;

import internal.console.Console;
import me.enderlight3336.wrapper.EnderWrapperMain;
import me.enderlight3336.wrapper.log.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardOpenOption.*;

public final class LogUtil {
    static final File LOG_PATH = new File(EnderWrapperMain.getPath(), "log");
    static File LOG;
    static OutputStream writer;
    @ApiStatus.Internal
    public static Logger MAIN = new Logger("EnderWrapper");
    static final ExecutorService service = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "EnderWrapper IO Worker");
        thread.setDaemon(true);
        return thread;
    });
    public static void init() throws IOException {
        Console.init();
        LOG_PATH.mkdirs();
        String data = LocalDate.now().toString();
        for (int i = 1; true; i++) {
            File file = new File(LOG_PATH, data + "#" + i + ".log");
            if (!file.exists()){
                LOG = file;
                break;
            }
        }
        writer = Files.newOutputStream(LOG.toPath(), CREATE, APPEND, WRITE);
    }
    public static void write(String str) {
        service.submit(new Task(str.getBytes()));
    }

    record Task(byte[] b) implements Runnable {
        @Override
        public void run() {
            try {
                writer.write(b);
            } catch (IOException e) {
                LogUtil.MAIN.catchThrow(e, Logger.Level.ERROR, "Error occurred while on logging");
            }
        }
    }
}
