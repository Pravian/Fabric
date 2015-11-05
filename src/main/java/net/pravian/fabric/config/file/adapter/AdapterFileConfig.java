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

import java.io.Reader;
import java.io.Writer;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import net.pravian.fabric.config.file.AbstractFileConfig;

public class AdapterFileConfig extends AbstractFileConfig {

    @Getter
    @Setter
    private Adapter adapter;

    public AdapterFileConfig(Logger logger, Adapter adapter) {
        super(logger, adapter.options());
        this.adapter = adapter;
    }

    @Override
    public boolean loadFrom(Reader reader) {
        return adapter.read(reader, this);
    }

    @Override
    public boolean saveTo(Writer writer) {
        return adapter.write(this, writer);
    }

}
