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
package net.pravian.fabric.config.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pravian.fabric.Check;
import net.pravian.fabric.config.ConfigOptions;
import net.pravian.fabric.config.memory.MemoryConfig;

public abstract class AbstractFileConfig extends MemoryConfig implements FileConfig {

    public AbstractFileConfig(Logger logger) {
        super(logger, new FileConfigOptions());
    }

    public AbstractFileConfig(Logger logger, ConfigOptions options) {
        super(logger, options);
    }

    @Override
    public FileConfigOptions options() {
        return (FileConfigOptions) options;
    }

    @Override
    public boolean loadFrom(File file) {
        try {
            return loadFrom(new FileReader(file));
        } catch (FileNotFoundException ignored) {
        }

        if (!options().copyFromJar()) {
            logger.severe("Could not load configuration. File not found!");
            return false;
        }

        logger.info("Writing default configuration: " + file.getPath());

        final InputStream defConfig;
        try {
            defConfig = getDefaultStream(file);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not write default configuration! Exception obtaining default stream!", ex);
            return false;
        }

        try {
            Files.copy(defConfig, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioex) {
            logger.log(Level.SEVERE, "Could not write default configuration!", ioex);
            return false;
        }

        try (Reader reader = new FileReader(file)) {
            loadFrom(reader);
        } catch (IOException ioex) {
            logger.log(Level.SEVERE, "Could not load default configuration!", ioex);
            return false;
        }

        return true;
    }

    @Override
    public boolean saveTo(File file) {
        Check.notNull(file, "File may not be null");

        try {
            file = file.getAbsoluteFile();
        } catch (Exception ignored) {
        }

        final File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                logger.severe("Could not write configuration. Could not create parent directories!");
                return false;
            }
        }

        try (Writer writer = new FileWriter(file, false)) {
            return saveTo(writer);
        } catch (IOException ioex) {
            logger.log(Level.SEVERE, "Could write configuration. File is a directory!", ioex);
            return false;
        }
    }

    protected InputStream getDefaultStream(File file) throws Exception {
        return getClass().getClassLoader().getResourceAsStream(file.getName());
    }

}
