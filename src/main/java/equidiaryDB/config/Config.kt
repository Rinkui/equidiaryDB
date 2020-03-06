package equidiaryDB.config

import equidiaryDB.EquidiaryDB
import java.io.FileReader
import java.io.IOException
import java.nio.file.Path
import java.util.*
import java.util.regex.Pattern

open class Config internal constructor(private val properties: Properties) {
    private val isValid: Boolean
        get() {
            for (configProperty in CONFIG_PROPERTIES) {
                if (mandatoryPropertyIsNotPresent(properties, configProperty)) {
                    EquidiaryDB.LOGGER.fatal(configProperty.property + " is a mandatory property.")
                    return false
                }
            }
            return portDBIsValid()
        }

    private fun portDBIsValid(): Boolean {
        return PORT_REGEX.matcher(portDB).matches()
    }

    val schemaDB: String
        get() = properties.getProperty(ConfigProperty.SCHEMA_DB.property)

    val passwordDB: String
        get() = properties.getProperty(ConfigProperty.PASSWORD_DB.property)

    val userDB: String
        get() = properties.getProperty(ConfigProperty.USER_DB.property)

    val portDB: String
        get() = properties.getProperty(ConfigProperty.PORT_DB.property)

    val hostDB: String
        get() = properties.getProperty(ConfigProperty.HOST_DB.property)

    companion object {
        val NULL_CONFIG: Config = Config(Properties())
        private val CONFIG_PROPERTIES = ConfigProperty.values()
        private val PORT_REGEX = Pattern.compile("[0-9]{1,5}")

        @JvmStatic
        fun createConfig(file: Path): Config {
            val properties = Properties()

            try {
                val reader = FileReader(file.toFile())
                properties.load(reader)
                reader.close()
            }
            catch (e: IOException) {
                EquidiaryDB.LOGGER.fatal("config.properties file doesn't exists")
                return NULL_CONFIG
            }
            val config = Config(properties)

            return if (!config.isValid) {
                NULL_CONFIG
            }
            else config
        }

        private fun mandatoryPropertyIsNotPresent(properties: Properties,
                                                  configProperty: ConfigProperty): Boolean {
            return configProperty.isMandatory && !properties.containsKey(configProperty.property)
        }
    }

}
