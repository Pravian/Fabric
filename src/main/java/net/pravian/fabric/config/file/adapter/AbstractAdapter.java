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
package net.pravian.fabric.config.file.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pravian.fabric.config.ConfigSection;
import net.pravian.fabric.config.Conversions;

public abstract class AbstractAdapter implements Adapter {

    protected final Logger logger;

    public AbstractAdapter(Logger logger) {
        this.logger = logger;
    }

    protected void mapData(ConfigSection from, Map<String, Object> to) {
        for (String directKey : from.getKeys()) {
            try {
                Object value = from.get(directKey);

                if (!(value instanceof ConfigSection)) {
                    to.put(directKey, value);
                    continue;
                }

                // Create new map
                Map<String, Object> subMap = new HashMap<>();
                to.put(directKey, subMap);

                // Recursively write that map
                mapData((ConfigSection) value, subMap);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Could not write configuration map: " + from.getFullPath() + "." + directKey);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void unmapData(Map<? super Object, Object> from, ConfigSection to) {

        // Decompose map for section
        for (Object directKey : from.keySet()) {
            try {
                final String keyString = Conversions.asString(directKey);
                final Object value = from.get(directKey);

                if (!(value instanceof Map)) {
                    to.set(keyString, value);
                    continue;
                }

                ConfigSection newSection = to.createSection(keyString);
                unmapData((Map<Object, Object>) value, newSection);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Could not load configuration map: " + to.getFullPath() + "." + directKey);
            }
        }
    }
}
