package equidiaryDB;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestConstant {
    public static final String TEST_FILES_DIRECTORY = "src/test/test_files";
    public static final String TEST_PROPERTIES_DIRECTORY = TEST_FILES_DIRECTORY + "/properties";
    public static Path CONFIG_TEST = Paths.get(TEST_PROPERTIES_DIRECTORY + "/configForTest.properties");
    public static Path CONFIG_PROPERTIES = Paths.get("config/config.properties");
    public static Path CONFIG_PROPERTIES_BK = Paths.get("config/config.properties.bk");
    public static Path CONFIG_TEST_INVALID_IP = Paths.get(TEST_PROPERTIES_DIRECTORY + "/configInvalidPort.properties");
}
