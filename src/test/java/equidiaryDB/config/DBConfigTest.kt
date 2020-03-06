package equidiaryDB.config

import equidiaryDB.GenericTestCase
import equidiaryDB.TestConstant.CONFIG_PROPERTIES
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.nio.file.Files

class DBConfigTest : GenericTestCase() {
    @AfterEach
    fun tearsDown() {
        Files.deleteIfExists(CONFIG_PROPERTIES)
    }

    @Test
    fun `given no config file when create config then nullConfig`() {
        givenNoConfigFile()
        val config = whenCreateConfig()
        thenGetNullConfig(config)
    }

    @Test
    fun `given invalid port when create config then nullConfig`() {
        ConfigurationFile.givenProperty("portDB", "toto")
        val config = whenCreateConfig()
        thenGetNullConfig(config)
    }

    @ParameterizedTest
    @ValueSource(strings = ["hostDB", "portDB", "userDB", "passwordDB", "schemaDB"])
    fun `given missing property when create config then nullConfig`(property: String) {
        ConfigurationFile.givenNoKey(property)
        val config = whenCreateConfig()
        thenGetNullConfig(config)
    }

    // GIVEN
    private fun givenNoConfigFile() {
        Files.deleteIfExists(CONFIG_PROPERTIES)
    }

    // WHEN
    private fun whenCreateConfig(): DBConfig? {
        return getDBConfig()
    }

    // THEN
    private fun thenGetNullConfig(config: DBConfig?) {
        Assertions.assertEquals(NULL_CONFIG, config)
    }
}
