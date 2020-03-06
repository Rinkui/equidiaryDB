package equidiaryDB

import java.nio.file.Paths

object TestConstant {
    const val TEST_FILES_DIRECTORY = "src/test/test_files"
    const val TEST_PROPERTIES_DIRECTORY = "$TEST_FILES_DIRECTORY/properties"
    var CONFIG_TEST = Paths.get("$TEST_PROPERTIES_DIRECTORY/configForTest.properties")
    var CONFIG_PROPERTIES = Paths.get("config/config.properties")
    var CONFIG_PROPERTIES_BK = Paths.get("config/config.properties.bk")
    var CONFIG_TEST_INVALID_IP = Paths.get("$TEST_PROPERTIES_DIRECTORY/configInvalidPort.properties")
}
