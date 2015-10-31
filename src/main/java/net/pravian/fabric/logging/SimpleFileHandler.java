package net.pravian.fabric.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SimpleFileHandler extends Handler {

    private final Handler handle;

    public SimpleFileHandler(Formatter formatter, File file) {
        this(formatter, file.getPath());

        if (file.exists()) {
            file.delete();
        }
    }

    public SimpleFileHandler(Formatter formatter, String name) {
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler(name);
            fileHandler.setFormatter(formatter);
        } catch (IOException | SecurityException ex) {
            ex.printStackTrace(); // No logger here yet
        }

        handle = fileHandler;
    }

    @Override
    public void publish(LogRecord lr) {
        handle.publish(lr);
    }

    @Override
    public void flush() {
        handle.flush();
    }

    @Override
    public void close() throws SecurityException {
        handle.close();
    }
}
