package equidiaryDB.services

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import equidiaryDB.EquidiaryDB.start
import equidiaryDB.EquidiaryDB.stop
import equidiaryDB.GenericTestCase
import equidiaryDB.TestConstant
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files

class LoginServiceTest : GenericTestCase() {
    @BeforeEach
    fun setup() {
        Files.deleteIfExists(TestConstant.CONFIG_PROPERTIES)
        Files.copy(TestConstant.CONFIG_TEST, TestConstant.CONFIG_PROPERTIES)
        start()
    }

    @AfterEach
    fun teardown() {
        stop()
        response = null
    }

    @Test
    fun loginNominal() {
        val loginBody = givenUserWithPasswordBody("marie", "marie")
        whenLogin(loginBody)
        thenResponseIs("Hello marie")
    }

    @Test
    fun loginWithNonExistingUsernameInDB() {
        val loginBody = givenUserWithPasswordBody("wrong", "marie")
        whenLogin(loginBody)
        thenResponseIs("Wrong username or password")
    }

    @Test
    fun loginWithWrongPasswordInDB() {
        val loginBody = givenUserWithPasswordBody("wrong", "marie")
        whenLogin(loginBody)
        thenResponseIs("Wrong username or password")
    }

    // GIVEN
    private fun givenUserWithPasswordBody(userName: String,
                                          password: String): String {
        return "{\"username\":$userName;\"password\":$password}"
    }

    // WHEN
    @Throws(UnirestException::class)
    private fun whenLogin(loginBody: String) {
        response = Unirest.post(EQUIDIARYDB_PATH + LOGIN_ENDPOINT).body(loginBody).asString()
    }

    // THEN
    private fun thenResponseIs(expected: String) {
        Assertions.assertEquals(expected, response!!.body)
    }

    companion object {
        private const val EQUIDIARYDB_PORT = "7001"
        private const val EQUIDIARYDB_IP = "localhost"
        private const val EQUIDIARYDB_PATH = "http://$EQUIDIARYDB_IP:$EQUIDIARYDB_PORT"
        private const val LOGIN_ENDPOINT = "/login"
        private var response: HttpResponse<String>? = null
    }
}
