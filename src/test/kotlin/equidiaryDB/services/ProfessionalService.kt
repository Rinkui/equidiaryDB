package equidiaryDB.services

import com.mashape.unirest.http.Unirest
import com.toxicbakery.bcrypt.Bcrypt
import equidiaryDB.WebTestCase
import equidiaryDB.utils.givenUser
import equidiaryDB.utils.withDefaultHeaders
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ProfessionalService : WebTestCase() {
    private val PRO_ENDPOINT = "/professional"
    private var userUuid: String = ""

    @BeforeEach
    override fun setup() {
        super.setup()
        userUuid = UUID.randomUUID().toString()
        givenUser(userUuid, "marie", Bcrypt.hash("marie", 8))
    }

    @Test
    fun getProfessionalByUser() {

    }

    private fun whenGetProfessionals(horseName: String) = Unirest.get("$EQUIDIARYDB_PATH$PRO_ENDPOINT/$horseName").withDefaultHeaders().asString()
}