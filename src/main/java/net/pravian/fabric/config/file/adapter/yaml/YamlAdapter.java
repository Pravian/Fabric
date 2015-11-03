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

import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pravian.fabric.config.file.adapter.AbstractAdapter;
import net.pravian.fabric.config.ConfigSection;
import org.yaml.snakeyaml.Yaml;

public class YamlAdapter extends AbstractAdapter {

    private final YamlConfigOptions options = new YamlConfigOptions();
    private final Yaml yaml = new Yaml(options.rawDumperOptions());

    public YamlAdapter(Logger logger) {
        super(logger);
    }

    @Override
    public YamlConfigOptions options() {
        return options;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean read(Reader from, ConfigSection to) {
        Map<Object, Object> dataMap;

        try {
            dataMap = (Map<Object, Object>) yaml.load(from);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not load configuration!", ex);
            return false;
        }

        unmapData(dataMap, to);
        return true;
    }

    @Override
    public boolean write(ConfigSection from, Writer to) {

        Map<String, Object> dataMap = new HashMap<>();
        mapData(from, dataMap);

        try {
            yaml.dump(dataMap, to);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could write configuration.", ex);
            return false;
        }

        return true;
    }

}
