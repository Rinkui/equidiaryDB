package equidiaryDB.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DataBase(private val connectionDB: Connection) {

    companion object {
        fun createDatabase(hostname: String,
                           port: Int,
                           user: String,
                           password: String,
                           schema: String): DataBase {

            val connection = DriverManager.getConnection("jdbc:mysql://$hostname:$port/$schema?serverTimezone=UTC",
                                                         user,
                                                         password)

            return DataBase(connection)
        }
    }

    /**
     * The function check if the pair user/password is correct and return the userId.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return The userId on equidiaryDB.database OR -1 if the user was not found OR -2 if we have multiple user.
     * @throws SQLException Throw the error which can be occur during the request to equidiaryDB.database.
     */
    @Throws(SQLException::class)
    fun isCorrectUser(username: String?,
                      password: String?): Int {
        var userId = -1
        val request = "SELECT * FROM user WHERE name = ? AND password = ?"
        try {
            connectionDB.prepareStatement(request).use { statement ->
                statement.setString(1, username)
                statement.setString(2, password)
                val result = statement.executeQuery()
                while (result.next()) {
                    if (userId != -1) {
                        return -2
                    }
                    userId = result.getInt("userId")
                }
                return userId
            }
        }
        catch (e: SQLException) {
            throw e
        }
    }
}
