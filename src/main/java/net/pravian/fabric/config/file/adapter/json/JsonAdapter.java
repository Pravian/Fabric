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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pravian.fabric.config.ConfigSection;
import net.pravian.fabric.config.file.FileConfigOptions;
import net.pravian.fabric.config.file.adapter.AbstractAdapter;

public class JsonAdapter extends AbstractAdapter {
    
    private final FileConfigOptions options = new FileConfigOptions();
    private final JsonFactory factory = new JsonFactory();
    private final ObjectMapper mapper = new ObjectMapper(factory);
    private final TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {};
   
    public JsonAdapter(Logger logger) {
        super(logger);
    }

    @Override
    public FileConfigOptions options() {
        return options;
    }

    @Override
    public boolean read(Reader from, ConfigSection to) {
        Map<String, Object> dataMap;
        
        try {
            dataMap = mapper.readValue(from, type);
        } catch (IOException ex) {
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
            mapper.writeValue(to, dataMap);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not write configuration!", ex);
            return false;
        }
        
        return true;
    }
}
