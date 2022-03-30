package equidiaryDB.services

import com.toxicbakery.bcrypt.Bcrypt
import equidiaryDB.EquidiaryDB.db
import equidiaryDB.database.DatabaseService
import equidiaryDB.security.TokenProvider
import io.javalin.http.Context
import io.javalin.http.Handler
import org.json.JSONObject

class LoginService : Handler {
    private val USERNAME = "username"
    private val PASSWORD = "password"

    fun login(context: Context) {
        val requestBody = JSONObject(context.body())
        val username = requestBody.getString(USERNAME)
        val password = requestBody.getString(PASSWORD)
        val user = DatabaseService.getUser(username)

        if (user.isEmpty() || !Bcrypt.verify(password, user[0].password)) {
            context.status(503)
        }

        context.json(TokenProvider().createToken(username))
    }

    override fun handle(context: Context) {
        val requestBody = JSONObject(context.body())
        val username = requestBody.getString(USERNAME)
        val password = requestBody.getString(PASSWORD)
        val userId = db.isCorrectUser(username, password)
        if (isCorrectUser(userId)) {
            context.result("Hello $username")
        } else {
            context.result("Wrong username or password")
        }
    }

    private fun isCorrectUser(userId: Int): Boolean {
        return userId >= 0
    }
}
