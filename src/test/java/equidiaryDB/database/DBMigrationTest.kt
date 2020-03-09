package equidiaryDB.database

import equidiaryDB.EquidiaryDB
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import java.nio.file.Paths

object FlywaySchemaHistory : Table("flyway_schema_history") {
    val id = integer("installed_rank").autoIncrement().primaryKey()
    val checksum = integer("checksum")
    val description = varchar("description", 50)
    val executionTime = integer("execution_time")
    val installedBy = varchar("installed_by", 50)
    val installedOn = datetime("installed_on")
    val script = varchar("script", 50)
    val success = bool("success")
    val type = varchar("type", 50)
    val version = integer("version")
}

object Horses : IntIdTable("horse") {
    val name = varchar("horse_name", 150)
    val height = integer("height")
    val weight = integer("weight").nullable()
    val birthDate = date("birth_date")
}


class DBMigrationTest {
    companion object {
        @AfterAll
        fun tearDown() {
            FlywaySchemaHistory.deleteAll()
            Horses.deleteAll()
        }
    }

    @Test
    fun `on fait un test`() {
        val connect = Database.connect(
                "jdbc:postgresql://localhost:5432/equidiary",
                driver = "org.postgresql.Driver",
                user = "equidiaryDB",
                password = "xkrZsVjjpyBq4JHB"
        )

//        val path = Paths.get("db_mi")

        val url = "jdbc:postgresql://localhost:5432/equidiary?serverTimezone=UTC"
        val db_migration = Flyway
                .configure()
                .locations("filesystem:/equidiaryDB/migration")
                .dataSource(url, "equidiaryDB", "xkrZsVjjpyBq4JHB")
                .load()

        db_migration.migrate()

        transaction {
            println(FlywaySchemaHistory.selectAll().map { FlywayVersion(it[FlywaySchemaHistory.script]) })
//            Horses.insert { }
        }
    }

    private fun thenAppStartCorrectly() {}

    private fun whenStartApp() {
        EquidiaryDB.start()
    }

    private fun givenEmptyDataBase() {}
}

data class FlywayVersion(val script: String)
