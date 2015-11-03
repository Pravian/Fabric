package net.pravian.fabric.config.yaml;

import net.pravian.fabric.config.file.adapter.yaml.YamlConfig;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static com.google.common.truth.Truth.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import net.pravian.fabric.config.ConfigSection;

public class YamlConfigFileTest {

    private final File testFile = getResource("test.yml");
    private final File complexTestFile = getResource("complex.yml");

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
        assertThat(config.getStringList("some.list")).containsExactly("one", "two", "three");

        assertThat(config.contains("some.section"));
        assertThat(config.getSection("some.section")).isInstanceOf(ConfigSection.class);
        assertThat(config.getString("some.section.string")).isEqualTo("verystringy");
    }

    @Test
    public void saving() {
        Logger logger = mock(Logger.class);
        YamlConfig config = new YamlConfig(logger);

        config.set("some.string", "stringy");
        config.set("some.int", 42);
        config.set("some.list", Arrays.asList("one", "two", "three"));
        config.createSection("some.section").set("string", "justastr");

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
    public void saveLoad() {

        Logger logger = mock(Logger.class);
        YamlConfig config = new YamlConfig(logger);

        config.set("some.string", "stringy");
        config.set("some.int", 42);
        config.set("some.list", Arrays.asList("one", "two", "three"));
        config.createSection("some.section").set("string", "justastr");

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
        assertThat(config.getStringList("some.list")).containsExactly("one", "two", "three");
        assertThat(config.getSection("some.section")).isNotNull();
        assertThat(config.getString("some.section.string")).isEqualTo("justastr");
    }

    @Test
    public void complexLoad() {
        Logger logger = mock(Logger.class);
        YamlConfig config = new YamlConfig(logger);

        assertThat(complexTestFile).isNotNull();
        assertThat(complexTestFile.exists()).isTrue();

        config.loadFrom(complexTestFile);

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly(
                "parent",
                "directnum",
                "directstring",
                "42",
                "list",
                "multilinewrap",
                "multilinepre"
        ).inOrder();
        assertThat(config.get("directnum")).isEqualTo(2);
        assertThat(config.getString("directstring")).isEqualTo("pizza!");
        assertThat(config.get("42")).isInstanceOf(Boolean.class);
        assertThat(config.getStringList("list")).containsExactly(
                "it's true",
                "unity rules",
                "A very long string with special characters! @#$%").inOrder();
        assertThat(config.getString("multilinewrap").trim()).isEqualTo(
                "This is some multiline text. "
                + "which spans across multiple lines...");
        assertThat(config.getString("multilinepre").trim()).isEqualTo(
                "This is some multiline text.\n"
                + "However, this text does preserve newlines!");

        // parent subsection
        final ConfigSection parent = config.getSection("parent");
        assertThat(parent).isNotNull();
        assertThat(parent.get("subkey")).isInstanceOf(String.class);
        assertThat(parent.get("1")).isInstanceOf(Integer.class);
        assertThat(parent.get("7")).isInstanceOf(String.class);
        assertThat(parent.get("list")).isInstanceOf(List.class);
        assertThat(parent.getStringList("list")).containsExactly("item", "pizza", "fanta").inOrder();

    }

    private File getResource(String fileName) {
        return new File(getClass().getClassLoader().getResource(fileName).getFile());
    }

}
