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

import net.pravian.fabric.config.ConfigOptions;

public class MemoryConfigOptions implements ConfigOptions {

    private char pathSeperator = '.';
    private boolean showDefaults = false;

    @Override
    public MemoryConfigOptions pathSeperator(char seperator) {
        this.pathSeperator = seperator;
        return this;
    }

    @Override
    public char pathSeperator() {
        return pathSeperator;
    }

    @Override
    public boolean showDefaults() {
        return showDefaults;
    }

    @Override
    public ConfigOptions showDefaults(boolean copy) {
        this.showDefaults = copy;
        return this;
    }

}
