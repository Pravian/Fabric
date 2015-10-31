package net.pravian.fabric.logging;

import java.util.logging.Logger;

public class SubLogger extends Logger {

    public SubLogger(String name) {
        super(name, null);
    }

    public SubLogger newSubLogger(String name) {
        SubLogger subLogger = new SubLogger(name);
        subLogger.setParent(this);
        return subLogger;
    }

}
