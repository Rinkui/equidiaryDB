package equidiaryDB.config

enum class ConfigProperty(val property: String,
                          val isMandatory: Boolean) {
    SCHEMA_DB("schemaDB", true),
    USER_DB("userDB", true),
    PORT_DB("portDB", true),
    HOST_DB("hostDB", true),
    PASSWORD_DB("passwordDB", true);

}
