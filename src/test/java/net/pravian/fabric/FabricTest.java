package net.pravian.fabric;

import net.pravian.fabric.config.yaml.YamlConfigFileTest;
import net.pravian.fabric.config.ConfigTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ConfigTest.class,
    YamlConfigFileTest.class})
public class FabricTest {

}
