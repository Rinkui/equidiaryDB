package equidiaryDB.services

import com.toxicbakery.bcrypt.Bcrypt
import equidiaryDB.database.DatabaseService
import equidiaryDB.security.TokenProvider
import io.javalin.http.Context
import io.javalin.http.Handler
import org.json.JSONObject

class UserService : Handler {
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

    fun createUser(context: Context) {
        val requestBody = JSONObject(context.body())
        val username = requestBody.getString(USERNAME)
        val password = requestBody.getString(PASSWORD)
        val user = DatabaseService.getUser(username)
        if (user.isNotEmpty()) {
            context.status(400)
            context.result("""{"message":"User already exists"}""")
            return
        }
        DatabaseService.createUser(username, Bcrypt.hash(password, 8))
        context.status(200)
    }

    override fun handle(context: Context) {}
}
