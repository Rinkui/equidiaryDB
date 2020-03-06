package equidiaryDB.config

import equidiaryDB.GenericTestCase
import equidiaryDB.TestConstant
import equidiaryDB.config.Config.Companion.NULL_CONFIG
import equidiaryDB.config.Config.Companion.createConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class ConfigTest : GenericTestCase() {
    @AfterEach
    @Throws(IOException::class)
    fun tearsDown() {
        Files.deleteIfExists(TestConstant.CONFIG_PROPERTIES)
    }

    @Test
    @Throws(IOException::class)
    fun nullConfigIfFileDoesntExists() {
        givenNoConfigFile()
        val config = whenCreateConfig()
        thenGetNullConfig(config)
    }

    @Test
    @Throws(IOException::class)
    fun portIsInvalid() {
        givenConfigProperties(TestConstant.CONFIG_TEST_INVALID_IP)
        val config = whenCreateConfig()
        thenGetNullConfig(config)
    }

    // GIVEN
    @Throws(IOException::class)
    private fun givenNoConfigFile() {
        Files.deleteIfExists(TestConstant.CONFIG_PROPERTIES)
    }

    @Throws(IOException::class)
    private fun givenConfigProperties(configFile: Path?) {
        Files.copy(configFile, TestConstant.CONFIG_PROPERTIES)
    }

    // WHEN
    @Throws(IOException::class)
    private fun whenCreateConfig(): Config? {
        return createConfig(TestConstant.CONFIG_PROPERTIES)
    }

    // THEN
    private fun thenGetNullConfig(config: Config?) {
        Assertions.assertEquals(NULL_CONFIG, config)
    }
}
