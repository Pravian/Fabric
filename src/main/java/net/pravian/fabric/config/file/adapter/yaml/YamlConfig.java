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
package net.pravian.fabric.config.file.adapter.yaml;

import java.util.logging.Logger;
import net.pravian.fabric.config.file.adapter.Adapter;
import net.pravian.fabric.config.file.adapter.AdapterFileConfig;

public class YamlConfig extends AdapterFileConfig {

    public YamlConfig(Logger logger) {
        super(logger, new YamlAdapter(logger));
    }

    @Override
    public YamlConfigOptions options() {
        return (YamlConfigOptions) getAdapter().options();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        throw new UnsupportedOperationException("Cannot set adapter for YamlConfig instance!");
    }

}
