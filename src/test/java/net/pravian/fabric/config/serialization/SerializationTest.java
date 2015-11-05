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

import java.util.logging.Logger;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static com.google.common.truth.Truth.*;
import net.pravian.fabric.config.ConfigSection;
import net.pravian.fabric.config.memory.MemoryConfig;

public class SerializationTest {

    private final Vector3iSerializer ser = new Vector3iSerializer();

    @Test
    public void defaultSerialization() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        final ConfigSerialization serialization = config.serialization();

        assertThat(serialization.getSerializer(Vector3i.class));
        assertThat(serialization.getSerializers()).isEmpty();

        serialization.setSerializer(Vector3i.class, ser);

        verifyZeroInteractions(logger);
        assertThat(serialization.getSerializers()).containsExactly(ser);

        assertThat(serialization.hasSerializer(Vector3i.class));
        assertThat(serialization.getSerializer(Vector3i.class)).isEqualTo(ser);

        assertThat(serialization.getSerializer(String.class)).isNull();
    }

    @Test
    public void serialize() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        config.serialization().setSerializer(Vector3i.class, ser);

        Vector3i vec = new Vector3i();
        vec.x = 42;
        vec.y = 4242;
        vec.z = 424242;

        try {
            config.setSerializable("vector", vec);
        } catch (SerializationException ex) {
            assertWithMessage(ex.getMessage()).fail();
        }

        verifyZeroInteractions(logger);
        ConfigSection section = config.getSection("vector");
        assertThat(section).isNotNull();
        assertThat(section.get("x") instanceof Integer);
        assertThat(section.getInt("x")).isEqualTo(42);
        assertThat(section.get("y") instanceof Integer);
        assertThat(section.getInt("y")).isEqualTo(4242);
        assertThat(section.get("z") instanceof Integer);
        assertThat(section.getInt("z")).isEqualTo(424242);
        assertThat(section.getKeys()).containsExactly(
                "x",
                "y",
                "z"
        );
    }

    @Test
    public void deserialize() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        config.serialization().setSerializer(Vector3i.class, ser);

        final ConfigSection section = config.createSection("vector");
        section.set("x", 42);
        section.set("y", 4242);
        section.set("z", 424242);

        Vector3i vec;
        try {
            vec = config.getSerializable("vector", Vector3i.class);
        } catch (SerializationException ex) {
            assertWithMessage(ex.getMessage()).fail();
            return;
        }

        verifyZeroInteractions(logger);
        assertThat(vec).isNotNull();
        assertThat(vec.x).isEqualTo(42);
        assertThat(vec.y).isEqualTo(4242);
        assertThat(vec.z).isEqualTo(424242);
    }

}
