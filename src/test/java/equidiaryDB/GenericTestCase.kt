package equidiaryDB

import equidiaryDB.config.ConfigurationFile
import equidiaryDB.database.Appointments
import equidiaryDB.database.EquidiaryUsers
import equidiaryDB.database.Horses
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach

open class GenericTestCase {
    companion object {
        init {
            ConfigurationFile.givenDefault()
            connectToDB()
        }

        private fun connectToDB() {
            val host = ConfigurationFile.getProperty("hostDB")
            val port = ConfigurationFile.getProperty("portDB")
            val schema = ConfigurationFile.getProperty("schemaDB")
            val user = ConfigurationFile.getProperty("userDB")
            val password = ConfigurationFile.getProperty("passwordDB")
            Database.connect(
                    "jdbc:postgresql://$host:$port/$schema",
                    driver = "org.postgresql.Driver",
                    user = user,
                    password = password
            )
        }
    }

    @AfterEach
    open fun tearDown(){
        transaction {
            Horses.deleteAll()
            Appointments.deleteAll()
            EquidiaryUsers.deleteAll()
        }
    }
}
