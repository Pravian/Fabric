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
package net.pravian.fabric.config.file.adapter.json;

import java.util.logging.Logger;
import net.pravian.fabric.config.file.FileConfigOptions;
import net.pravian.fabric.config.file.adapter.Adapter;
import net.pravian.fabric.config.file.adapter.AdapterConfig;

public class JsonConfig extends AdapterConfig {

    public JsonConfig(Logger logger) {
        super(logger, new JsonAdapter(logger));
    }

    @Override
    public FileConfigOptions options() {
        return getAdapter().options();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        throw new UnsupportedOperationException("Cannot set adapter for JsonConfig instance!");
    }

}
