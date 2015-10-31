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
package net.pravian.fabric.config.file;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import net.pravian.fabric.config.Config;

public interface FileConfig extends Config {

    public boolean loadFrom(File file);

    public boolean loadFrom(Reader reader);

    public boolean saveTo(File file);

    public boolean saveTo(Writer writer);

}
