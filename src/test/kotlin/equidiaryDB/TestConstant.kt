package equidiaryDB

import java.nio.file.Path
import java.nio.file.Paths

object TestConstant {
    const val TEST_FILES_DIRECTORY = "src/test/test_files"
    const val MIGRATION_NAME = "migration/V90000__migration_test.sql"
    var MIGRATION_FILE: Path = Paths.get("$TEST_FILES_DIRECTORY/$MIGRATION_NAME")
    var CONFIG_PROPERTIES: Path = Paths.get("config/config.properties")
}
