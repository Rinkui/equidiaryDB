package equidiaryDB.config

import equidiaryDB.TestConstant.CONFIG_PROPERTIES

object ConfigurationFile {
    private val file = CONFIG_PROPERTIES.toFile()

    private val defaultConfig = mapOf(
        "hostDB" to "localhost",
        "portDB" to "5432",
        "userDB" to "sa",
        "passwordDB" to "sa",
        "schemaDB" to "equidiary",
        "urlDB" to "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;MODE=MySQL")

    private var config = defaultConfig.toMutableMap()

    private fun writeFile() {
        var text = ""

        config.forEach { text += "${it.key}=${it.value}\n" }

        file.writeText(text)
    }

    fun getProperty(key: String) = config.getOrDefault(key, "")

    fun givenProperty(
        key: String,
        value: String,
    ) {
        config.putIfAbsent(key, value)
        config.replace(key, value)
        writeFile()
    }

    fun delete() {
        if (file.exists()) {
            file.delete()
        }
    }

    fun givenNoKey(keyName: String) {
        config.remove(keyName)
        writeFile()
    }

    fun givenDefault() {
        delete()
        config = defaultConfig.toMutableMap()
        writeFile()
    }
}
