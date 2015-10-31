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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface ConfigSection {

    public void clear();

    public Config getRoot();

    public ConfigSection getParent();

    public String getPath();

    public String getFullPath();

    public Set<String> getKeys();

    public Object get(String key);

    public Object getDirect(String key);

    public String getString(String key);

    public int getInt(String key);

    public List<String> getStrings(String key);

    public ConfigSection getSection(String key);

    public ConfigSection createSection(String key);

    public void put(String key, Object object);

    public void putDirect(String key, Object object);

    public boolean contains(String key);

    public boolean containsDirect(String key);

}
