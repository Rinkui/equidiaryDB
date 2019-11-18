package equidiaryDB.config;

import equidiaryDB.GenericTestCase;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

import static equidiaryDB.TestConstant.CONFIG_PROPERTIES;
import static org.junit.Assert.assertEquals;

public class ConfigTest extends GenericTestCase {

    @Test
    public void toto() throws IOException {
        Files.deleteIfExists(CONFIG_PROPERTIES);

        Config config = Config.createConfig(CONFIG_PROPERTIES);

        assertEquals(NullConfig.INSTANCE, config);
    }
}
