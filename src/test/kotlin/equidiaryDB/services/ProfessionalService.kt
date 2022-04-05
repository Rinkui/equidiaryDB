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

    private fun whenGetProfessionals() = Unirest.get("$EQUIDIARYDB_PATH/$userUuid/professional").withDefaultHeaders().asString()
}