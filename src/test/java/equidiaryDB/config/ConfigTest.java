package equidiaryDB.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import static equidiaryDB.TestConstant.CONFIG_TEST;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ConfigTest {

    @Parameterized.Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(getObjects("hostDB"), getObjects("portDB"), getObjects("userDB"), getObjects("passwordDB"), getObjects("schemaDB"));
    }

    private static Object[] getObjects(String property) {
        return new Object[]{property};
    }

    private String property;

    public ConfigTest(String property) {
        this.property = property;
    }

    @Test
    public void nullConfig() throws IOException {
        Path file = Paths.get(CONFIG_TEST);
        Properties properties = new Properties();
        properties.load(new FileInputStream(file.toFile()));
        properties.remove(property);

        assertEquals(NullConfig.INSTANCE, Config.createConfig(properties));
    }
}
