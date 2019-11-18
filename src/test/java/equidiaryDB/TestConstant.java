package equidiaryDB;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestConstant {
    public static final String TEST_FILES_DIRECTORY = "src/test/test_files";
    public static final String PROPERTIES_DIRECTORY = TEST_FILES_DIRECTORY + "/properties";
    public static String CONFIG_TEST = PROPERTIES_DIRECTORY + "/configForTest.properties";
    public static Path CONFIG_PROPERTIES = Paths.get("config/config.properties");
}
