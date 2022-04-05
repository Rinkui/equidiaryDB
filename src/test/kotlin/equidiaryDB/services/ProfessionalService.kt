package equidiaryDB.services

import com.mashape.unirest.http.Unirest
import com.toxicbakery.bcrypt.Bcrypt
import equidiaryDB.WebTestCase
import equidiaryDB.utils.*
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

    private fun whenGetProfessionals(user: String = userUuid) = Unirest.get("$EQUIDIARYDB_PATH/$user/professionals").withDefaultHeaders().asString()
    private fun whenPutProfessionals(proBody: String) = Unirest.get("$EQUIDIARYDB_PATH/$userUuid/professionals").withDefaultHeaders().asString()
}