package equidiaryDB.config;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.regex.Pattern;

import static equidiaryDB.EquidiaryDB.logger;
import static equidiaryDB.config.ConfigProperty.*;

public class Config {
    private final static ConfigProperty[] CONFIG_PROPERTIES = ConfigProperty.values();
    private final Properties properties;
    private static final Pattern PORT_REGEX = Pattern.compile("[0-9]{1,5}");

    Config(Properties properties) {
        this.properties = properties;
    }

    public static Config createConfig(Path file) throws IOException {
        if( !file.toFile().exists()){
            logger.fatal("config.properties doesn't exists");
            return NullConfig.INSTANCE;
        }

        FileReader fileReader = new FileReader(file.toFile());
        Properties properties = new Properties();
        properties.load(fileReader);

        for (ConfigProperty configProperty : CONFIG_PROPERTIES) {
            if (mandatoryPropertyIsNotPresent(properties, configProperty)) {
                logger.fatal(configProperty.getName() + " is a mandatory property.");
                return NullConfig.INSTANCE;
            }
        }

        if ( propertyIsInvalid(properties) ){
            return NullConfig.INSTANCE;
        }

        return new Config(properties);
    }

    private static boolean propertyIsInvalid(Properties configProperty) {
//        String name = configProperty.getName();
//        if( name.equals("portDB") ){
//            return !PORT_REGEX.matcher(name).matches();
//        }
        return false;
    }

    private static boolean mandatoryPropertyIsNotPresent(Properties properties, ConfigProperty configProperty) {
        return configProperty.isMandatory() && !properties.containsKey(configProperty.getName());
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
