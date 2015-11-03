/*
 * Copyright 2015 Jerom van der Sar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
