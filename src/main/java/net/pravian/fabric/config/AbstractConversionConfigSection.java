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

import java.util.List;
import net.pravian.fabric.config.ConfigSection;
import net.pravian.fabric.config.Conversions;

public abstract class AbstractConversionConfigSection extends AbstractConfigSection {

    protected AbstractConversionConfigSection() {
    }

    protected AbstractConversionConfigSection(ConfigSection parent) {
        super(parent);
    }

    @Override
    public ConfigSection getSection(String key) {
        Object value = get(key);
        return value instanceof ConfigSection ? (ConfigSection) value : null;
    }

    @Override
    public boolean getBoolean(String key) {
        return Conversions.asBoolean(get(key));
    }

    @Override
    public byte getByte(String key) {
        return Conversions.asByte(get(key));
    }

    @Override
    public char getChar(String key) {
        return Conversions.asChar(get(key));
    }

    @Override
    public short getShort(String key) {
        return Conversions.asShort(get(key));
    }

    @Override
    public int getInt(String key) {
        return Conversions.asInt(get(key));
    }

    @Override
    public long getLong(String key) {
        return Conversions.asLong(get(key));
    }

    @Override
    public float getFloat(String key) {
        return Conversions.asFloat(get(key));
    }

    @Override
    public double getDouble(String key) {
        return Conversions.asDouble(get(key));
    }

    @Override
    public String getString(String key) {
        return Conversions.asString(get(key));
    }

    @Override
    public List<Object> getList(String key) {
        return Conversions.asList(get(key));
    }

    @Override
    public List<String> getStringList(String key) {
        return Conversions.asStringList(get(key));
    }
}
