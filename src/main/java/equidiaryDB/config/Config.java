package equidiaryDB.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static equidiaryDB.config.ConfigProperty.*;

public class Config {
    private final static ConfigProperty[] CONFIG_PROPERTIES = ConfigProperty.values();
    private final Properties properties;

    Config(Properties properties) {
        this.properties = properties;
    }

    public static Config createConfig(Path path) throws IOException {
        Properties prop = new Properties();
        prop.load(Files.newInputStream(path));

        for (ConfigProperty configProperty : CONFIG_PROPERTIES) {
            if (mandatoryPropertyIsNotPresent(prop, configProperty)) {
                return NullConfig.INSTANCE;
            }
        }

        return new Config(prop);
    }

    private static boolean mandatoryPropertyIsNotPresent(Properties prop, ConfigProperty configProperty) {
        return configProperty.isMandatory() && !prop.containsKey(configProperty.getName());
    }

    public String getSchemaDB() {
        return properties.getProperty(SCHEMA_DB.getName());
    }

    public String getPasswordDB() {
        return properties.getProperty(PASSWORD_DB.getName());
    }

    public String getUserDB() {
        return properties.getProperty(USER_DB.getName());
    }

    public String getPortDB() {
        return properties.getProperty(PORT_DB.getName());
    }

    public String getHostDB() {
        return properties.getProperty(HOST_DB.getName());
    }
}
