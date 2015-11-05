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
package net.pravian.fabric.config.memory;

import java.util.HashSet;
import net.pravian.fabric.config.AbstractConversionConfigSection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.pravian.fabric.config.ConfigSection;
import net.pravian.fabric.config.Conversions;
import net.pravian.fabric.config.serialization.SerializationException;

public class MemoryConfigSection extends AbstractConversionConfigSection {

    protected final Map<String, Object> data = new LinkedHashMap<>();
    private final String path;
    private final String fullPath;

    protected MemoryConfigSection() {
        this.path = "";
        this.fullPath = "";
    }

    protected MemoryConfigSection(ConfigSection parent, String directKey) {
        super(parent);
        this.path = directKey;
        this.fullPath = root.equals(parent) ? directKey : parent.getFullPath() + root.options().pathSeperator() + directKey;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getFullPath() {
        return fullPath;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public Set<String> getKeys() {
        return data.keySet();
    }

    @Override
    public Set<String> getKeysDeep() {
        final char sep = getRoot().options().pathSeperator();

        final Set<String> keys = new HashSet<>();

        for (String topKey : getKeys()) {
            keys.add(topKey);

            final Object value = getDirect(topKey);
            if (!(value instanceof ConfigSection)) {
                continue;
            }

            for (String subKey : ((ConfigSection) value).getKeysDeep()) {
                keys.add(topKey + sep + subKey);
            }
        }

        return keys;
    }

    @Override
    public boolean containsDirect(String key) {
        if (key.indexOf(root.options().pathSeperator()) != -1) {
            throw new IllegalArgumentException("Cannot check direct contains in full path!");
        }

        return data.containsKey(key);
    }

    @Override
    public Object getDirect(String key) {
        if (key.indexOf(root.options().pathSeperator()) != -1) {
            throw new IllegalArgumentException("Cannot get direct value from full path!");
        }

        return data.get(key);
    }

    @Override
    public void setDirect(String key, Object object) {
        if (key.indexOf(root.options().pathSeperator()) != -1) {
            throw new IllegalArgumentException("Cannot put direct in full path!");
        }

        if (object == null) {
            data.remove(key);
        } else {
            data.put(key, Conversions.objectify(object));
        }
    }

    @Override
    public ConfigSection createSection(String fullKey) {

        ConfigSection section = this;

        final char seperator = getRoot().options().pathSeperator();

        int sepIndex;

        // Create subsection
        while ((sepIndex = fullKey.indexOf(seperator)) != -1) {
            String directKey = fullKey.substring(0, sepIndex);

            // Get/create subsection
            ConfigSection subSection = section.getSection(directKey);
            if (subSection == null) {
                subSection = new MemoryConfigSection(section, directKey);
                section.set(directKey, subSection);
            }

            // Next subsection
            section = subSection;
            fullKey = fullKey.substring(sepIndex + 1, fullKey.length());
        }

        MemoryConfigSection newSection = new MemoryConfigSection(section, fullKey);
        section.set(fullKey, newSection);
        return newSection;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.fullPath);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MemoryConfigSection other = (MemoryConfigSection) obj;
        return Objects.equals(this.fullPath, other.fullPath);
    }
}
