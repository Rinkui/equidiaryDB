package equidiaryDB

import java.nio.file.Path
import java.nio.file.Paths

object TestConstant {
    private const val TEST_FILES_DIRECTORY = "src/test/test_files"
    var CONFIG_PROPERTIES: Path = Paths.get("config/config.properties")
}
