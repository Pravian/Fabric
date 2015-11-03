package net.pravian.fabric.config;

import net.pravian.fabric.config.memory.MemoryConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.logging.Logger;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static com.google.common.truth.Truth.*;
import static org.junit.Assert.*;

public class ConfigTest {

    @Test
    public void configSanity() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        assertThat(config).isEqualTo(config);
        assertThat(config.getKeys()).isEmpty();
        assertThat(config.getFullPath()).isEmpty();
        assertThat(config.getRoot()).isEqualTo(config);
        assertThat(config.getParent()).isEqualTo(null);

        verifyZeroInteractions(logger);
    }

    @Test
    public void simplePutGet() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        assertTrue(config.getKeys().isEmpty());

        config.put("some", "data");

        verifyZeroInteractions(logger);
        assertTrue(config.getKeys().size() == 1);
        assertTrue(config.get("some").equals("data"));

        config.clear();

        verifyZeroInteractions(logger);
        assertTrue(config.get("some") == null);
        assertTrue(config.getKeys().isEmpty());
    }

    @Test
    public void section() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        ConfigSection section = config.createSection("some");

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).isNotEmpty();
        assertThat(config.get("some")).isInstanceOf(ConfigSection.class);
        assertThat(config.get("some")).isEqualTo(section);

        assertThat(section.getFullPath()).isEqualTo("some");
        assertThat(section.getParent()).isEqualTo(config);
        assertThat(section.getRoot()).isEqualTo(config);
        assertThat(section.getKeys()).isEmpty();

        config.put("some", null);

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).isEmpty();
        assertThat(config.get("some")).isNull();
    }

    @Test
    public void subSection() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        ConfigSection section = config.createSection("some.section");
        ConfigSection parent = section.getParent();

        verifyZeroInteractions(logger);

        // section
        assertThat(section.getFullPath()).isEqualTo("some.section");
        assertThat(section.getRoot()).isEqualTo(config);
        assertThat(section.getKeys()).isEmpty();

        // some
        assertThat(parent.getFullPath()).isEqualTo("some");
        assertThat(parent.getRoot()).isEqualTo(config);
        assertThat(parent.getKeys()).containsExactly("section");

        // config
        assertThat(config.getKeys()).containsExactly("some");
        assertThat(config.get("some")).isEqualTo(section.getParent());
        assertThat(config.get("some.section")).isEqualTo(section);
        assertThat(config).isEqualTo(parent.getParent());
        assertThat(config).isEqualTo(section.getParent().getParent());
    }

    @Test
    public void contains() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        ConfigSection some = config.createSection("some");
        some.put("string", "stringy");
        config.put("some.otherstring", "stringz");
        config.put("some.int", 42);
        config.put("some.stringlist", Lists.newArrayList("one", "two", "three"));
        ConfigSection section = config.createSection("section");
        section.put("string", "verystringy");

        verifyZeroInteractions(logger);
        assertThat(config.containsDirect("some")).isTrue();
        assertThat(config.contains("some")).isTrue();

        assertThat(some.containsDirect("string")).isTrue();
        assertThat(some.contains("string")).isTrue();

        assertThat(some.contains("otherstring"));
        assertThat(some.containsDirect("otherstring"));

        assertThat(some.containsDirect("int")).isTrue();
        assertThat(some.contains("int")).isTrue();

        assertThat(some.containsDirect("stringlist")).isTrue();
        assertThat(some.contains("stringlist")).isTrue();

        assertThat(config.containsDirect("section"));
        assertThat(config.contains("section")).isTrue();

        assertThat(config.contains("section.string")).isTrue();
        assertThat(section.contains("string")).isTrue();
        assertThat(section.containsDirect("string")).isTrue();
    }

    @Test
    public void createSection() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        ConfigSection section = config.createSection("some.sub.section");

        verifyZeroInteractions(logger);
        assertThat(config.contains("some")).isTrue();
        assertThat(config.contains("some.sub")).isTrue();
        assertThat(config.contains("some.sub.section")).isTrue();
        assertThat(config.contains("some.other.section")).isFalse();
        assertThat(config.containsDirect("some")).isTrue();
        assertThat(section.getParent().containsDirect("section"));
    }

    @Test
    public void sectionCleanup() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        config.createSection("some.sub.section");

        // Creation
        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("some");

        config.put("some", null);

        // Top level cleanup
        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).isEmpty();

        config.createSection("some.sub.section");
        config.put("some.sub.section", null);

        // Staged cleanup
        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("some");
        assertThat(config.contains("some.sub")).isTrue();
        assertThat(config.contains("some.sub.section")).isFalse();

        config.put("some.sub", null);

        // Staged cleanup (2)
        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("some");
        assertThat(config.contains("some")).isTrue();
        assertThat(config.contains("some.sub")).isFalse();

        config.clear();

        // Clearing
        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).isEmpty();
    }

    @Test
    public void sectionPutGet() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        config.put("some.section", "data");

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).isNotEmpty();
        assertThat(config.getKeys()).contains("some");
        assertThat(config.get("some")).isInstanceOf(ConfigSection.class);
        assertThat(config.get("some.section")).isEqualTo("data");

        config.clear();

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).isEmpty();
        assertThat(config.getString("some")).isNull();
    }

    @Test
    public void sectionOverwrite() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        // creatSection overwrites values
        ConfigSection section = config.createSection("some.section");
        section.put("someint", 42);

        verifyZeroInteractions(logger);
        assertThat(config.contains("some.section.someint")).isTrue();

        config.createSection("some.section");

        verifyZeroInteractions(logger);
        assertThat(config.contains("some.section.someint")).isFalse();

        // createSection doesn't overwrite subsections
        config.clear();
        config.createSection("some.area").put("string", "stringy");
        config.put("some.area.answer", 42);
        config.put("some.answer", 10);

        verifyZeroInteractions(logger);

        config.createSection("some.area.notouch");

        verifyZeroInteractions(logger);
        assertThat(config.contains("some.area.notouch"));
        assertThat(config.get("some.area.notouch")).isInstanceOf(ConfigSection.class);
        assertThat(config.getString("some.area.string")).isEqualTo("stringy");
        assertThat(config.getInt("some.area.answer")).isEqualTo(42);
        assertThat(config.get("some.answer")).isEqualTo(10);

        config.createSection("some.area");

        verifyZeroInteractions(logger);
        assertThat(config.getSection("some.area")).isNotNull();
        assertThat(config.getSection("some.area").getKeys()).isEmpty();
        assertThat(config.get("some.answer")).isEqualTo(10);
    }

    @Test
    public void types() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        config.put("some.string", "stringy");
        config.put("some.int", 42);
        config.put("some.stringlist", Lists.newArrayList("one", "two", "three"));
        ConfigSection section = config.createSection("section");
        section.put("string", "verystringy");

        verifyZeroInteractions(logger);
        assertThat(config.contains("some.string")).isTrue();
        assertThat(config.getString("some.string")).isEqualTo("stringy");

        assertThat(config.contains("some.int")).isTrue();
        assertThat(config.getInt("some.int")).isEqualTo(42);

        assertThat(config.contains("some.stringlist")).isTrue();
        assertThat(config.getStrings("some.stringlist")).containsExactly("one", "two", "three");

        assertThat(config.contains("section")).isTrue();
        assertThat(config.getSection("section")).isEqualTo(section);

        assertThat(config.contains("section.string")).isTrue();
        assertThat(config.getString("section.string")).isEqualTo("verystringy");

        assertThat(config.contains("some.dummy")).isFalse();
        assertThat(config.getStrings("some.stringlistdummy")).isEmpty();
    }

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void typeConversion() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        config.put("some.string.1", new Object() {
            @Override
            public String toString() {
                return "stringy";
            }
        });
        config.put("some.string.2", 20);
        config.put("some.string.3", Byte.valueOf((byte) 11));
        config.put("some.int.1", "42");
        config.put("some.int.2", Integer.valueOf(32));
        config.put("some.int.3", Byte.valueOf((byte) 123));
        config.put("some.list.1", new String[]{"one", "two", "three"});
        config.put("some.list.2", Sets.newHashSet("uno", "dos", "tres"));

        verifyZeroInteractions(logger);
        assertThat(config.getString("some.string.1")).isEqualTo("stringy");
        assertThat(config.getString("some.string.2")).isEqualTo("20");
        assertThat(config.getString("some.string.3")).isEqualTo("11");

        assertThat(config.getInt("some.int.1")).isEqualTo(42);
        assertThat(config.getInt("some.int.2")).isEqualTo(32);
        assertThat(config.getInt("some.int.3")).isEqualTo(123);

        assertThat(config.getStrings("some.list.1")).containsExactly("one", "two", "three");
        assertThat(config.getStrings("some.list.2")).containsExactly("uno", "dos", "tres");
    }

    @Test
    public void ordering() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        config.put("first", 10);
        config.put("second", 20);
        config.put("third", 30);

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("first", "second", "third").inOrder();

        config.clear();
        config.put("first", 40);
        config.put("second", 50);
        config.put("third", 60);

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("first", "second", "third").inOrder();

        config.clear();
        config.put("third", 70);
        config.put("second", 80);
        config.put("first", 90);

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("third", "second", "first").inOrder();
    }

}
