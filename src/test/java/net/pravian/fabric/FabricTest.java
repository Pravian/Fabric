package net.pravian.fabric;

import net.pravian.fabric.config.yaml.YamlConfigFileTest;
import net.pravian.fabric.config.ConfigTest;
import net.pravian.fabric.config.serialization.ReadWriteTest;
import net.pravian.fabric.config.serialization.SerializationTest;
import net.pravian.fabric.event.EventManagerTest;
import net.pravian.fabric.event.annotation.AnnotationEventTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ConfigTest.class,
    ReadWriteTest.class,
    SerializationTest.class,
    YamlConfigFileTest.class,
    EventManagerTest.class,
    AnnotationEventTest.class})
public class FabricTest {

}
