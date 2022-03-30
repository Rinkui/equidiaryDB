package equidiaryDB.services

import com.mashape.unirest.http.Unirest
import com.toxicbakery.bcrypt.Bcrypt
import equidiaryDB.WebTestCase
import equidiaryDB.database.Users
import equidiaryDB.utils.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class UserServiceTest : WebTestCase() {
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

    @Test
    fun createUserNominal() {
        whenCreateUser("""{"username":"marie","password":"password"}""")
            .isOk()

        assertEquals(1, transaction { Users.select { Users.userName eq "marie" }.toList() }.size)
    }

    @Test
    fun createExistingUserError() {
        givenUser(UUID.randomUUID().toString(), "marie", Bcrypt.hash("marie", 8))

        whenCreateUser("""{"username":"marie","password":"password"}""")
            .isBadRequest()
            .withBody("""{"message":"User already exists"}""")

    }

    // WHEN
    private fun whenLogin(loginBody: String) = Unirest.post("$EQUIDIARYDB_PATH$LOGIN_ENDPOINT").body(loginBody).asString()
    private fun whenCreateUser(loginBody: String) = Unirest.put("$EQUIDIARYDB_PATH$LOGIN_ENDPOINT").body(loginBody).asString()

    companion object {
        private const val LOGIN_ENDPOINT = "/login"
    }
}
