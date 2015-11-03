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

        config.set("some", "data");

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

        config.set("some", null);

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
        some.set("string", "stringy");
        config.set("some.otherstring", "stringz");
        config.set("some.int", 42);
        config.set("some.stringlist", Lists.newArrayList("one", "two", "three"));
        ConfigSection section = config.createSection("section");
        section.set("string", "verystringy");

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

        config.set("some", null);

        // Top level cleanup
        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).isEmpty();

        config.createSection("some.sub.section");
        config.set("some.sub.section", null);

        // Staged cleanup
        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("some");
        assertThat(config.contains("some.sub")).isTrue();
        assertThat(config.contains("some.sub.section")).isFalse();

        config.set("some.sub", null);

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

        config.set("some.section", "data");

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
        section.set("someint", 42);

        verifyZeroInteractions(logger);
        assertThat(config.contains("some.section.someint")).isTrue();

        config.createSection("some.section");

        verifyZeroInteractions(logger);
        assertThat(config.contains("some.section.someint")).isFalse();

        // createSection doesn't overwrite subsections
        config.clear();
        config.createSection("some.area").set("string", "stringy");
        config.set("some.area.answer", 42);
        config.set("some.answer", 10);

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

        config.set("some.string", "stringy");
        config.set("some.int", 42);
        config.set("some.stringlist", Lists.newArrayList("one", "two", "three"));
        config.set("some.boolean", true);
        config.set("some.float", 24d);
        config.set("some.double", 42f);
        ConfigSection section = config.createSection("section");
        section.set("string", "verystringy");

        verifyZeroInteractions(logger);
        assertThat(config.contains("some.string")).isTrue();
        assertThat(config.getString("some.string")).isEqualTo("stringy");

        assertThat(config.contains("some.int")).isTrue();
        assertThat(config.getInt("some.int")).isEqualTo(42);

        assertThat(config.contains("some.stringlist")).isTrue();
        assertThat(config.getStringList("some.stringlist")).containsExactly("one", "two", "three");

        assertThat(config.contains("some.boolean")).isTrue();
        assertThat(config.getBoolean("some.boolean")).isTrue();

        assertThat(config.contains("some.float")).isTrue();
        assertThat(config.getFloat("some.float")).isEquivalentAccordingToCompareTo((float) 24);

        assertThat(config.contains("some.double")).isTrue();
        assertThat(config.getDouble("some.double")).isWithin(0.1).of(42);

        assertThat(config.contains("section")).isTrue();
        assertThat(config.getSection("section")).isEqualTo(section);

        assertThat(config.contains("section.string")).isTrue();
        assertThat(config.getString("section.string")).isEqualTo("verystringy");

        assertThat(config.contains("some.dummy")).isFalse();
        assertThat(config.getStringList("some.stringlistdummy")).isNull();
    }

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void typeConversion() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        config.set("some.string.1", new Object() {
            @Override
            public String toString() {
                return "stringy";
            }
        });
        config.set("some.string.2", 20);
        config.set("some.string.3", Byte.valueOf((byte) 11));
        config.set("some.int.1", "42");
        config.set("some.int.2", Integer.valueOf(32));
        config.set("some.int.3", Byte.valueOf((byte) 123));
        config.set("some.list.1", new String[]{"one", "two", "three"});
        config.set("some.list.2", Sets.newHashSet("uno", "dos", "tres"));
        config.set("some.boolean.1", true);
        config.set("some.boolean.2", false);
        config.set("some.boolean.3", "true");
        config.set("some.boolean.4", "false");
        config.set("some.boolean.5", "some random value");
        config.set("num", 12.4f);

        verifyZeroInteractions(logger);
        assertThat(config.getString("some.string.1")).isEqualTo("stringy");
        assertThat(config.getString("some.string.2")).isEqualTo("20");
        assertThat(config.getString("some.string.3")).isEqualTo("11");

        assertThat(config.getInt("some.int.1")).isEqualTo(42);
        assertThat(config.getInt("some.int.2")).isEqualTo(32);
        assertThat(config.getInt("some.int.3")).isEqualTo(123);

        assertThat(config.getStringList("some.list.1")).containsExactly("one", "two", "three");
        assertThat(config.getStringList("some.list.2")).containsExactly("uno", "dos", "tres");

        assertThat(config.getBoolean("some.boolean.1")).isTrue();
        assertThat(config.getBoolean("some.boolean.2")).isFalse();
        assertThat(config.getBoolean("some.boolean.3")).isTrue();
        assertThat(config.getBoolean("some.boolean.4")).isFalse();
        assertThat(config.getBoolean("some.boolean.5")).isFalse();

        assertThat(config.getByte("num")).isEquivalentAccordingToCompareTo((byte) 12);
        assertThat(config.getShort("num")).isEquivalentAccordingToCompareTo((short) 12);
        assertThat(config.getInt("num")).isEqualTo(Integer.valueOf(12));
        assertThat(config.getLong("num")).isEqualTo((long) 12);
        assertThat(config.getFloat("num")).isEquivalentAccordingToCompareTo(12.4f);
        assertThat(config.getDouble("num")).isWithin(0.1d).of(12.4d);
    }

    @Test
    public void ordering() {
        Logger logger = mock(Logger.class);
        MemoryConfig config = new MemoryConfig(logger);

        config.set("first", 10);
        config.set("second", 20);
        config.set("third", 30);

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("first", "second", "third").inOrder();

        config.clear();
        config.set("first", 40);
        config.set("second", 50);
        config.set("third", 60);

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("first", "second", "third").inOrder();

        config.clear();
        config.set("third", 70);
        config.set("second", 80);
        config.set("first", 90);

        verifyZeroInteractions(logger);
        assertThat(config.getKeys()).containsExactly("third", "second", "first").inOrder();
    }

}
