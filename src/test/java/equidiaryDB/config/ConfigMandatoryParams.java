package equidiaryDB.config;

import equidiaryDB.GenericTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import static equidiaryDB.TestConstant.CONFIG_PROPERTIES;
import static equidiaryDB.TestConstant.CONFIG_TEST;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ConfigMandatoryParams extends GenericTestCase
{

    private static final Collection<Object[]> collection = new ArrayList<>();
    private              String               property;
    private              Properties           properties = new Properties();

    @Parameterized.Parameters
    public static Collection<Object[]> params()
    {
        buildTest("hostDB");
        buildTest("portDB");
        buildTest("userDB");
        buildTest("passwordDB");
        buildTest("schemaDB");

        return collection;
    }

    private static void buildTest( String propertyName )
    {
        collection.add(new Object[] { propertyName });
    }

    public ConfigMandatoryParams( String property )
    {
        this.property = property;
    }

    @Before
    public void setUp() throws IOException
    {
        Files.copy(CONFIG_TEST, CONFIG_PROPERTIES);
        properties.load(new FileInputStream(CONFIG_PROPERTIES.toFile()));
        properties.remove(property);
        properties.store(new FileWriter(CONFIG_PROPERTIES.toFile()), null);
    }

    @After
    public void tearDown() throws IOException
    {
        Files.deleteIfExists(CONFIG_PROPERTIES);
    }

    @Test
    public void nullConfig()
    {
        assertEquals(NullConfig.INSTANCE, Config.createConfig(CONFIG_PROPERTIES));
    }
}
