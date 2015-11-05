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

public class ReadWriteTest {

    @Test
    public void read() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        ConfigSection vSection = config.createSection("vector");
        vSection.set("x", 4);
        vSection.set("y", 5);
        vSection.set("z", 6);

        final Vector3i vec = new Vector3i();

        try {
            config.read("vector", vec);
        } catch (SerializationException ex) {
            assertWithMessage(ex.getMessage()).fail();
        }

        verifyZeroInteractions(logger);
        assertThat(vec.x).isEqualTo(4);
        assertThat(vec.y).isEqualTo(5);
        assertThat(vec.z).isEqualTo(6);
    }

    @Test
    public void write() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        final Vector3i vec = new Vector3i();
        vec.x = 4;
        vec.y = 5;
        vec.z = 6;

        try {
            config.write("vector", vec);
        } catch (SerializationException ex) {
            assertWithMessage(ex.getMessage()).fail();
        }

        verifyZeroInteractions(logger);
        final ConfigSection vSection = config.getSection("vector");
        assertThat(vSection.getInt("x")).isEqualTo(4);
        assertThat(vSection.getInt("y")).isEqualTo(5);
        assertThat(vSection.getInt("z")).isEqualTo(6);
        assertThat(vSection.getKeys()).containsExactly(
                "x",
                "y",
                "z"
        );
    }

}
