package equidiaryDB.config;

import equidiaryDB.GenericTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static equidiaryDB.TestConstant.*;
import static org.junit.Assert.assertEquals;

public class ConfigTest extends GenericTestCase {

    @After
    public void tearsDown() throws IOException {
        Files.deleteIfExists(CONFIG_PROPERTIES);
    }

    @Test
    public void nullConfigIfFileDoesntExists() throws IOException {
        givenNoConfigFile();

        Config config = whenCreateConfig();

        thenGetNullConfig(config);
    }

    @Test
    public void portIsInvalid() throws IOException {
        givenConfigProperties(CONFIG_TEST_INVALID_IP);

        Config config = whenCreateConfig();

        thenGetNullConfig(config);
    }


    // GIVEN

    private void givenNoConfigFile() throws IOException {
        Files.deleteIfExists(CONFIG_PROPERTIES);
    }

    private void givenConfigProperties(Path configFile) throws IOException {
        Files.copy(configFile, CONFIG_PROPERTIES);
    }

    // WHEN

    private Config whenCreateConfig() throws IOException {
        return Config.createConfig(CONFIG_PROPERTIES);
    }

    // THEN

    private void thenGetNullConfig(Config config) {
        assertEquals(NullConfig.INSTANCE, config);
    }
}
