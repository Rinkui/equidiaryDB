package equidiaryDB

import equidiaryDB.config.ConfigurationFile
import equidiaryDB.database.*
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
            //TODO le faire dans le code op !!!!!! 
            Database.connect(
                "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;MODE=MySQL",
                driver = "org.h2.Driver",
                user = "sa",
                password = "sa"
            )
        }
    }

    @AfterEach
    open fun tearDown() {
        transaction {
            HorseProfessionals.deleteAll()
            UserProfessionals.deleteAll()
            Professionals.deleteAll()
            Appointments.deleteAll()
            Horses.deleteAll()
            Users.deleteAll()
        }
    }
}
