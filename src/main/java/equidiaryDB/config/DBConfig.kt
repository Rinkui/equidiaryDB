package equidiaryDB.config

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.Key
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType
import equidiaryDB.EquidiaryDB.equiLogger
import java.nio.file.Paths

private val portKey = Key("portDB", intType)
private val hostKey = Key("hostDB", stringType)
private val userKey = Key("userDB", stringType)
private val passwordKey = Key("passwordDB", stringType)
private val schemaKey = Key("schemaDB", stringType)
private val urlKey = Key("urlDB", stringType)

private val CONFIG_PROPERTIES_PATH = Paths.get("config/config.properties")
val NULL_CONFIG = DBConfig()

data class DBConfig(
    val port: Int = 0,
    val host: String = "",
    val user: String = "",
    val password: String = "",
    val schema: String = "",
    val url: String = "",
)

fun getDBConfig(): DBConfig {
    equiLogger.info("Loading database properties form $CONFIG_PROPERTIES_PATH")
    return try {
        val config = ConfigurationProperties.fromFile(CONFIG_PROPERTIES_PATH.toFile())
        DBConfig(port = port(config),
            host = host(config),
            user = user(config),
            password = password(config),
            schema = schema(config),
            url = url(config))
    } catch (e: Exception) {
        equiLogger.warn("Unable to get server configuration from configuration file : ${e.message}")
        DBConfig()
    }
}

private fun port(config: ConfigurationProperties) = config[portKey]
private fun host(config: ConfigurationProperties) = config[hostKey]
private fun user(config: ConfigurationProperties) = config[userKey]
private fun password(config: ConfigurationProperties) = config[passwordKey]
private fun schema(config: ConfigurationProperties) = config[schemaKey]
private fun url(config: ConfigurationProperties) = config[urlKey]
