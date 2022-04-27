package equidiaryDB.services

import com.mashape.unirest.http.Unirest.*
import com.toxicbakery.bcrypt.Bcrypt
import equidiaryDB.WebTestCase
import equidiaryDB.database.Professionals
import equidiaryDB.database.UserProfessionals
import equidiaryDB.utils.*
import org.jetbrains.exposed.sql.JoinType.RIGHT
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID

class ProfessionalService : WebTestCase() {
    private var userUuid: String = ""

    @BeforeEach
    override fun setup() {
        super.setup()
        userUuid = randomUUID().toString()
        givenUser(userUuid, "marie", Bcrypt.hash("marie", 8))
    }

    @Test
    fun getProfessionalByUser() {
        givenProfessional(randomUUID().toString(), "Audrey", "Bur", "vet", userUuid)

        whenGetProfessionals()
            .isOk()
            .bodyLike(Regex("""\{"uuid":".{36}","firstName":"Audrey","lastName":"Bur","profession":"vet"\}"""))
    }

    @Test
    fun getProfessionalByUnknownUser() {
        whenGetProfessionals("Unknown")
            .isResourceNotFound()
    }

    @Test
    fun associateProfessionalWithUser() {
        val proUuid = randomUUID().toString()

        whenPutProfessionals("""{"uuid":"$proUuid","firstName":"Audrey","lastName":"Bur","profession":"vet"}""")
            .isOk()

        val proResult = transaction {
            UserProfessionals.join(Professionals, RIGHT)
                .select { (UserProfessionals.proUuid eq proUuid) and (UserProfessionals.userUuid eq userUuid) }
                .toList()[0]
        }
        assertEquals("Audrey", proResult[Professionals.firstName])
        assertEquals("Bur", proResult[Professionals.lastName])
        assertEquals("vet", proResult[Professionals.profession])
    }

    @Test
    fun `associate pro with user with missing mandatory field`() {
        whenPutProfessionals("""{"firstName":"Audrey","lastName":"Bur","profession":"vet"}""")
            .isBadRequest()
    }

    @Test
    fun updateProfessionalWithUser() {
        val proUuid = randomUUID().toString()
        givenProfessional(proUuid, "Audrey", "Bur", "vet", userUuid)

        whenPostProfessionals("""{"uuid":"$proUuid","firstName":"Clémence","lastName":"Bur","profession":"vet"}""")
            .isOk()

        val proResult = transaction {
            UserProfessionals.join(Professionals, RIGHT)
                .select { (UserProfessionals.proUuid eq proUuid) and (UserProfessionals.userUuid eq userUuid) }
                .toList()[0]
        }
        assertEquals("Clémence", proResult[Professionals.firstName])
        assertEquals("Bur", proResult[Professionals.lastName])
        assertEquals("vet", proResult[Professionals.profession])
    }

    private fun whenGetProfessionals(user: String = userUuid) = get("$EQUIDIARYDB_PATH/$user/professionals").withDefaultHeaders().asString()
    private fun whenPutProfessionals(proBody: String) = put("$EQUIDIARYDB_PATH/$userUuid/professionals").withDefaultHeaders().body(proBody).asString()
    private fun whenPostProfessionals(proBody: String) = post("$EQUIDIARYDB_PATH/$userUuid/professionals").withDefaultHeaders().body(proBody).asString()
}