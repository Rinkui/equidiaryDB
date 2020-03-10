package equidiaryDB.database

import equidiaryDB.EquidiaryDB
import equidiaryDB.GenericTestCase
import equidiaryDB.TestConstant.MIGRATION_FILE
import equidiaryDB.TestConstant.MIGRATION_NAME
import equidiaryDB.config.ConfigurationFile
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

object FlywaySchemaHistory : Table("flyway_schema_history") {
    val script = varchar("script", 50)
}

class DBMigrationTest : GenericTestCase() {
    @AfterEach
    override fun tearDown() {
        super.tearDown()
        Files.deleteIfExists(Paths.get("migration/$MIGRATION_NAME"))

        transaction {
            FlywaySchemaHistory.deleteWhere { FlywaySchemaHistory.script eq MIGRATION_NAME }
        }
    }

    @Test
    fun `valid migration`() {
        givenMigration()

        whenStartApp()

        thenMigrationIsDone()
    }

    private fun givenMigration() {
        ConfigurationFile.givenDefault()
        Files.copy(MIGRATION_FILE, Paths.get("migration/$MIGRATION_NAME"))
    }

    private fun whenStartApp() {
        EquidiaryDB.start()
    }

    private fun thenMigrationIsDone() {
        val transaction = transaction {
            FlywaySchemaHistory.select { FlywaySchemaHistory.script eq MIGRATION_NAME }.toList()
        }

        assertFalse(transaction.isEmpty())
    }
}

data class FlywayVersion(val script: String)
