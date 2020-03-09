package equidiaryDB.config

import equidiaryDB.TestConstant.CONFIG_PROPERTIES

object ConfigurationFile {
    val file = CONFIG_PROPERTIES.toFile()

    private val defaultConfig = mapOf(
            "hostDB" to "localhost",
            "portDB" to "5432",
            "userDB" to "equidiaryDB",
            "passwordDB" to "xkrZsVjjpyBq4JHB",
            "schemaDB" to "equidiary")

    private var config = defaultConfig.toMutableMap()

    private fun writeFile() {
        var text = ""

        config.forEach { text += "${it.key}=${it.value}\n" }

        file.writeText(text)
    }

    fun givenProperty(key: String,
                      value: String) {
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
