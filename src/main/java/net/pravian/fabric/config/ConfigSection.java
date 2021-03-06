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
import java.util.Set;
import net.pravian.fabric.config.serialization.ConfigReadable;
import net.pravian.fabric.config.serialization.ConfigWritable;
import net.pravian.fabric.config.serialization.SerializationException;

public interface ConfigSection {

    public void clear();

    public Config getRoot();

    public ConfigSection getParent();

    public String getPath();

    public String getFullPath();

    public ConfigSection getSection(String key);

    public ConfigSection createSection(String key);

    public Set<String> getKeys();

    public Set<String> getKeysDeep();

    public boolean contains(String key);

    public boolean containsDirect(String key);

    public Object get(String key);

    public void set(String key, Object object);

    public Object getDirect(String key);

    public void setDirect(String key, Object object);

    public <T> T getSerializable(String key, Class<T> clazz) throws SerializationException;

    public <T> void setSerializable(String key, T serializable) throws SerializationException;

    public void read(String key, ConfigReadable readable) throws SerializationException;

    public void write(String key, ConfigWritable writable) throws SerializationException;

    public boolean readSafe(String key, ConfigReadable readable);

    public boolean writeSafe(String key, ConfigWritable writable);

    // Types
    public boolean getBoolean(String key);

    public byte getByte(String key);

    public char getChar(String key);

    public short getShort(String key);

    public int getInt(String key);

    public long getLong(String key);

    public float getFloat(String key);

    public double getDouble(String key);

    public String getString(String key);

    public List<Object> getList(String key);

    public List<String> getStringList(String key);

}
