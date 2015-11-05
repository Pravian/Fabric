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
package net.pravian.fabric.config.serialization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigSerialization {

    private final Map<Class<?>, ConfigSerializer<?>> serializers = new HashMap<>();

    public <T> void setSerializer(Class<T> clazz, ConfigSerializer<T> serializer) {
        serializers.put(clazz, serializer);
    }

    @SuppressWarnings("unchecked")
    public <T> ConfigSerializer<T> getSerializer(Class<T> clazz) {
        return (ConfigSerializer<T>) serializers.get(clazz);
    }

    public boolean hasSerializer(Class<?> clazz) {
        return serializers.containsKey(clazz);
    }

    public Set<ConfigSerializer<?>> getSerializers() {
        return new HashSet<>(serializers.values());
    }

}
