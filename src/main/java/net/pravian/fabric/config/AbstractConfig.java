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

import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import net.pravian.fabric.config.Config;
import net.pravian.fabric.config.ConfigOptions;
import net.pravian.fabric.config.memory.MemoryConfig;
import net.pravian.fabric.config.simple.SimpleConfigOptions;
import net.pravian.fabric.config.simple.SimpleConfigSection;

public abstract class AbstractConfig extends SimpleConfigSection implements Config {

    @Getter
    protected final Logger logger;
    @Getter
    protected final ConfigOptions options;
    @Getter
    @Setter
    protected Config defaults;

    protected AbstractConfig(Logger logger) {
        this(logger, null);
    }

    protected AbstractConfig(Logger logger, ConfigOptions options) {
        this.logger = logger;
        this.options = (options != null ? options : new SimpleConfigOptions());
    }

    @Override
    public ConfigOptions options() {
        return options;
    }

    @Override
    public void putDefault(String key, Object value) {
        if (defaults == null) {
            defaults = new MemoryConfig(logger);
        }

        defaults.set(key, value);
    }

    @Override
    public Object getDefault(String key) {
        if (defaults == null) {
            return null;
        }

        return defaults.get(key);
    }

}
