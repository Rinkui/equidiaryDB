package equidiaryDB.config;

public enum ConfigProperty {
    SCHEMA_DB("schemaDB", true),
    USER_DB("userDB", true),
    PORT_DB("portDB", true),
    HOST_DB("hostDB", true),
    PASSWORD_DB("passwordDB", true);

    private final String name;
    private final boolean mandatory;

    ConfigProperty(String name, boolean mandatory) {
        this.name = name;
        this.mandatory = mandatory;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }
}
