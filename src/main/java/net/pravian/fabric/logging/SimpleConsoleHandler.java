package net.pravian.fabric.logging;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SimpleConsoleHandler extends Handler {

    public static final String LINE_SEPERATOR = System.getProperty("line.separator");
    private final Formatter formatter;

    public SimpleConsoleHandler(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void publish(LogRecord lr) {
        System.out.print(formatter.format(lr));
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
