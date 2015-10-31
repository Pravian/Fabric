package net.pravian.fabric.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimpleTimeFormatter extends Formatter {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        Throwable ex = record.getThrown();

        builder.append("[")
                .append(dateFormat.format(record.getMillis()))
                .append("][")
                .append(record.getLevel().getLocalizedName().toUpperCase())
                .append("]");

        if (!record.getLoggerName().equals(GlobalLogger.getLogger().getName())) {
            builder.append("[")
                    .append(record.getLoggerName())
                    .append("]");
        }

        builder.append(" ")
                .append(formatMessage(record))
                .append('\n');

        if (ex != null) {
            StringWriter writer = new StringWriter();
            ex.printStackTrace(new PrintWriter(writer));
            builder.append(writer);
        }

        return builder.toString();
    }
}
