package equidiaryDB.services

import com.mashape.unirest.http.Unirest
import com.toxicbakery.bcrypt.Bcrypt
import equidiaryDB.WebTestCase
import equidiaryDB.utils.bodyLike
import equidiaryDB.utils.givenUser
import equidiaryDB.utils.isForbidden
import equidiaryDB.utils.isOk
import org.junit.jupiter.api.Test
import java.util.*

class LoginServiceTest : WebTestCase() {
    @Test
    fun loginNominal() {
        givenUser(UUID.randomUUID().toString(), "marie", Bcrypt.hash("marie", 8))

        whenLogin("""{"username":"marie","password":"marie"}""")
            .isOk()
            .bodyLike(Regex("""\{"jwt":".{151}"\}"""))
    }

    @Test
    fun loginWithNonExistingUsernameInDB() {

        whenLogin("""{"username":"marie","password":"marie"}""")
            .isForbidden()
    }

    @Test
    fun loginWithWrongPasswordInDB() {
        givenUser(UUID.randomUUID().toString(), "marie", Bcrypt.hash("marie", 8))

        whenLogin("""{"username":"marie","password":"wrongPassword"}""")
            .isForbidden()
    }

    // WHEN
    private fun whenLogin(loginBody: String) = Unirest.post("$EQUIDIARYDB_PATH$LOGIN_ENDPOINT").body(loginBody).asString()

    companion object {
        private const val LOGIN_ENDPOINT = "/login"
    }
}
