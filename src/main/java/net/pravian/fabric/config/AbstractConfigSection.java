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
package net.pravian.fabric.config;

import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import javax.naming.ConfigurationException;
import net.pravian.fabric.config.Config;
import net.pravian.fabric.config.ConfigSection;
import net.pravian.fabric.config.serialization.ConfigReadable;
import net.pravian.fabric.config.serialization.ConfigSerializer;
import net.pravian.fabric.config.serialization.ConfigWritable;
import net.pravian.fabric.config.serialization.SerializationException;

public abstract class AbstractConfigSection implements ConfigSection {

    protected final Config root;
    protected final ConfigSection parent;

    protected AbstractConfigSection() {
        // Constructor for Config objects
        if (!(this instanceof Config)) {
            throw new AssertionError("Can not instantiate ConfigurationSection when not a config!");
        }

        this.root = (Config) this;
        this.parent = null;
    }

    protected AbstractConfigSection(ConfigSection parent) {
        this.root = parent.getRoot();
        this.parent = parent;
    }

    @Override
    public Config getRoot() {
        return root;
    }

    @Override
    public ConfigSection getParent() {
        return parent;
    }

    @Override
    public boolean contains(String key) {
        return get(key) != null;
    }

    //
    // ConfigReadable & ConfigWritable
    //
    @Override
    public void read(String key, ConfigReadable readable) throws SerializationException {
        ConfigSection section = getSection(key);
        if (section != null) {
            readable.readFrom(section);
        }
    }

    @Override
    public void write(String key, ConfigWritable writable) throws SerializationException {
        ConfigSection section = createSection(key);
        writable.writeTo(section);
    }

    @Override
    public boolean readSafe(String key, ConfigReadable readable) {
        try {
            read(key, readable);
            return true;
        } catch (SerializationException cex) {
            getRoot().getLogger().log(Level.SEVERE, "Could not read readable object from " + key, cex);
            return false;
        }
    }

    @Override
    public boolean writeSafe(String key, ConfigWritable writable) {
        try {
            write(key, writable);
            return true;
        } catch (SerializationException cex) {
            getRoot().getLogger().log(Level.SEVERE, "Could not write writable object from " + key, cex);
            return false;
        }
    }

    //
    // ConfigSerializer
    //
    @Override
    @SuppressWarnings("unchecked")
    public <T> void setSerializable(String fullKey, T object) {
        if (object == null) {
            set(fullKey, null);
            return;
        }

        final Class<T> clazz = (Class<T>) object.getClass();

        if (!root.serialization().hasSerializer(clazz)) {
            throw new SerializationException("Could not serialize object. No serializer present for " + clazz.getName());
        }

        final ConfigSerializer<T> serializer = root.serialization().getSerializer(clazz);
        serializer.writeTo(createSection(fullKey), object);
    }

    @Override
    public <T> T getSerializable(String fullKey, Class<T> clazz) {
        if (!root.serialization().hasSerializer(clazz)) {
            throw new SerializationException("Could not deserialize object. No serializer present for " + clazz.getName());
        }

        final ConfigSection section = getSection(fullKey);
        if (section == null) {
            return null;
        }

        final ConfigSerializer<T> serializer = root.serialization().getSerializer(clazz);
        return serializer.readFrom(section);
    }

    //
    // get & set
    //
    @Override
    public Object get(String fullKey) {
        final char seperator = getRoot().options().pathSeperator();

        int sepIndex;
        ConfigSection section = this;

        // Find the correct config subsection
        while ((sepIndex = fullKey.indexOf(seperator)) != -1) {

            // Get the direct value
            String directKey = fullKey.substring(0, sepIndex);
            section = section.getSection(directKey);

            // Can't find the section
            if (section == null) {
                return null;
            }

            // Next subsection
            fullKey = fullKey.substring(sepIndex + 1, fullKey.length());
        }

        Object value = section.getDirect(fullKey);
        if (value != null || !root.options().showDefaults()) {
            return value;
        }

        return root.getDefault(fullKey);
    }

    @Override
    public void set(String key, Object object) {
        final Iterator<String> paths = Arrays.asList(key.split("\\.")).iterator();

        ConfigSection section = this;
        String directKey = paths.next();

        // Recursively find/create the containing section
        while (paths.hasNext()) {
            ConfigSection nextSection = section.getSection(directKey);
            if (nextSection == null) {
                nextSection = section.createSection(directKey);
            }
            section = nextSection;
            directKey = paths.next();
        }

        section.setDirect(directKey, object);
    }

    // TODO: Get this to work...
    /*
     * @Override
     * public void put(String key, Object object) {
     * final char seperator = root.options().pathSeperator();
     *
     *
     * String sectionKey = null;
     * String directKey = key;
     *
     * // Find out if we're putting data in a subsection
     * final int lastSeperator = key.lastIndexOf(seperator);
     * if (lastSeperator != -1) {
     * sectionKey = key.substring(0, lastSeperator);
     * directKey = key.substring(lastSeperator + 1, key.length());
     * }
     *
     * final ConfigSection parentSection = sectionKey == null ? this : createSection(sectionKey);
     *
     * // Put the data
     * parentSection.putDirect(directKey, object);
     * }
     */
}
