package net.pravian.fabric.config.yaml;

import net.pravian.fabric.config.file.adapter.yaml.YamlConfig;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static com.google.common.truth.Truth.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import net.pravian.fabric.config.ConfigSection;

public class YamlConfigFileTest {

    private final File testFile = getResource("test.yml");

    @Test
    public void loading() {
        Logger logger = mock(Logger.class);
        YamlConfig config = new YamlConfig(logger);

        assertThat(testFile).isNotNull();
        assertThat(testFile.exists()).isTrue();

        config.loadFrom(testFile);

        verifyZeroInteractions(logger);
        assertThat(config.contains("some"));
        assertThat(config.getSection("some")).isNotNull();

        assertThat(config.contains("some.string"));
        assertThat(config.get("some.string")).isInstanceOf(String.class);
        assertThat(config.getString("some.string")).isEqualTo("stringy");

        assertThat(config.contains("some.int"));
        assertThat(config.get("some.int")).isInstanceOf(Integer.class);
        assertThat(config.getInt("some.int")).isEqualTo(42);

        assertThat(config.contains("some.list"));
        assertThat(config.get("some.list")).isInstanceOf(List.class);
        assertThat(config.getStrings("some.list")).containsExactly("one", "two", "three");

        assertThat(config.contains("some.section"));
        assertThat(config.getSection("some.section")).isInstanceOf(ConfigSection.class);
        assertThat(config.getString("some.section.string")).isEqualTo("verystringy");
    }

    @Test
    public void writing() {
        Logger logger = mock(Logger.class);
        YamlConfig config = new YamlConfig(logger);

        config.put("some.string", "stringy");
        config.put("some.int", 42);
        config.put("some.list", Arrays.asList("one", "two", "three"));
        config.createSection("some.section").put("string", "justastr");

        StringWriter writer;
        try {
            writer = new StringWriter();
            config.saveTo(writer);
            writer.flush();
        } catch (Exception ex) {
            fail("Could not write config to file!");
            return;
        }
        String data = writer.toString();

        verifyZeroInteractions(logger);
        assertThat(data).isNotEmpty();
        assertThat(data).contains("some");
        assertThat(data).contains("stringy");
        assertThat(data).contains("42");
        assertThat(data).contains("one");
        assertThat(data).contains("two");
        assertThat(data).contains("three");
        assertThat(data).contains("section");
        assertThat(data).contains("justastr");
    }

    @Test
    public void writeLoad() {

        Logger logger = mock(Logger.class);
        YamlConfig config = new YamlConfig(logger);

        config.put("some.string", "stringy");
        config.put("some.int", 42);
        config.put("some.list", Arrays.asList("one", "two", "three"));
        config.createSection("some.section").put("string", "justastr");

        StringWriter writer;
        try {
            writer = new StringWriter();
            config.saveTo(writer);
            writer.flush();
        } catch (Exception ex) {
            throw new AssertionError("Could not write config to string!", ex);
        }

        config.clear();
        assertThat(config.getKeys()).isEmpty();

        try {
            config.loadFrom(new StringReader(writer.toString()));
        } catch (Exception ex) {
            throw new AssertionError("Could not read config from string!", ex);
        }

        assertThat(config.getKeys()).isNotEmpty();
        assertThat(config.getString("some.string")).endsWith("stringy");
        assertThat(config.getInt("some.int")).isEqualTo(42);
        assertThat(config.getStrings("some.list")).containsExactly("one", "two", "three");
        assertThat(config.getSection("some.section")).isNotNull();
        assertThat(config.getString("some.section.string")).isEqualTo("justastr");
    }

    private File getResource(String fileName) {
        return new File(getClass().getClassLoader().getResource(fileName).getFile());
    }

}
