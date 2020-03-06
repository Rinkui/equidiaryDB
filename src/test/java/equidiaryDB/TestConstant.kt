package equidiaryDB

import java.nio.file.Path
import java.nio.file.Paths

object TestConstant {
    private const val TEST_FILES_DIRECTORY = "src/test/test_files"
    private const val TEST_PROPERTIES_DIRECTORY = "$TEST_FILES_DIRECTORY/properties"
    var CONFIG_TEST: Path = Paths.get("$TEST_PROPERTIES_DIRECTORY/configForTest.properties")
    var CONFIG_PROPERTIES: Path = Paths.get("config/config.properties")
    var CONFIG_PROPERTIES_BK: Path = Paths.get("config/config.properties.bk")
    var CONFIG_TEST_INVALID_IP: Path = Paths.get("$TEST_PROPERTIES_DIRECTORY/configInvalidPort.properties")
}
