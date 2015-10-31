package net.pravian.fabric.logging;

import java.io.File;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import net.pravian.fabric.Check;

public class GlobalLogger extends SubLogger {

    private static GlobalLogger logger;

    private GlobalLogger(String name) {
        super(name);
    }

    public void setupFileLogging(String file) {
        setupFileLogging(new SimpleTimeFormatter(), file);
    }

    public void setupFileLogging(Formatter formatter, File file) {
        logger.addHandler(new SimpleFileHandler(formatter, file));
    }

    public void setupFileLogging(Formatter formatter, String file) {
        logger.addHandler(new SimpleFileHandler(formatter, file));
    }

    public void setupConsoleLogging() {
        setupConsoleLogging(new SimpleTimeFormatter());
    }

    public void setupConsoleLogging(Formatter formatter) {
        logger.addHandler(new SimpleConsoleHandler(formatter));
    }

    public void handleException(Throwable ex) {
        logger.log(Level.SEVERE, "Received uncaught exception!", ex);
        System.exit(1);
    }

    public static GlobalLogger setupGlobalLogger(Class<?> clazz) {
        return setupGlobalLogger(clazz.getSimpleName());
    }

    public static GlobalLogger setupGlobalLogger(String name) {
        Check.isNull(logger, "Cannot setup global logger twice");

        logger = new GlobalLogger(name);
        logger.setLevel(Level.INFO);

        return logger;
    }

    public static GlobalLogger getLogger() {
        return Check.notNull(logger, "Global logger has not be set up");
    }
}
