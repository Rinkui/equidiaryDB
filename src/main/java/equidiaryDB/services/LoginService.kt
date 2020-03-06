package equidiaryDB.services

import equidiaryDB.EquidiaryDB.db
import io.javalin.http.Context
import io.javalin.http.Handler
import org.json.JSONObject

class LoginService : Handler {
    private val USERNAME = "username"
    private val PASSWORD = "password"

    override fun handle(context: Context) {
        val requestBody = JSONObject(context.body())
        val username = requestBody.getString(USERNAME)
        val password = requestBody.getString(PASSWORD)
        val userId = db.isCorrectUser(username, password)
        if (isCorrectUser(userId)) {
            context.result("Hello $username")
        }
        else {
            context.result("Wrong username or password")
        }
    }

    private fun isCorrectUser(userId: Int): Boolean {
        return userId >= 0
    }
}
