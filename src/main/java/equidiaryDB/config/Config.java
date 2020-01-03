package equidiaryDB.config;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.regex.Pattern;

import static equidiaryDB.EquidiaryDB.LOGGER;
import static equidiaryDB.config.ConfigProperty.*;

public class Config
{
    private final static ConfigProperty[] CONFIG_PROPERTIES = ConfigProperty.values();
    private final Properties properties;
    private static final Pattern PORT_REGEX = Pattern.compile("[0-9]{1,5}");

    Config(Properties properties)
    {
        this.properties = properties;
    }

    public static Config createConfig(Path file)
    {
        Properties properties = new Properties();
        try
        {
            FileReader reader = new FileReader(file.toFile());
            properties.load(reader);
            reader.close();
        }
        catch (IOException e)
        {
            LOGGER.fatal("config.properties file doesn't exists");
            return NullConfig.INSTANCE;
        }

        Config config = new Config(properties);

        if (!config.isValid())
        {
            return NullConfig.INSTANCE;
        }

        return config;
    }

    private boolean isValid()
    {
        for (ConfigProperty configProperty : CONFIG_PROPERTIES)
        {
            if (mandatoryPropertyIsNotPresent(properties, configProperty))
            {
                LOGGER.fatal(configProperty.getName() + " is a mandatory property.");
                return false;
            }
        }

        return portDBIsValid();
    }

    private boolean portDBIsValid()
    {
        return PORT_REGEX.matcher(getPortDB()).matches();
    }

    private static boolean mandatoryPropertyIsNotPresent(Properties properties,
                                                         ConfigProperty configProperty)
    {
        return configProperty.isMandatory() && !properties.containsKey(configProperty.getName());
    }

    public String getSchemaDB()
    {
        return properties.getProperty(SCHEMA_DB.getName());
    }

    public String getPasswordDB()
    {
        return properties.getProperty(PASSWORD_DB.getName());
    }

    public String getUserDB()
    {
        return properties.getProperty(USER_DB.getName());
    }

    public String getPortDB()
    {
        return properties.getProperty(PORT_DB.getName());
    }

    public String getHostDB()
    {
        return properties.getProperty(HOST_DB.getName());
    }
}
