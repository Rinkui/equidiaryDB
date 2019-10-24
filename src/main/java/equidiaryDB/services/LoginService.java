package equidiaryDB.services;

import equidiaryDB.EquidiaryDB;
import io.javalin.http.Context;
import org.json.JSONObject;

import java.sql.SQLException;

public class LoginService {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public static void login(Context context) throws SQLException {
        JSONObject requestBody = new JSONObject(context.body());
        String username = requestBody.getString(USERNAME);
        String password = requestBody.getString(PASSWORD);

        int userId = EquidiaryDB.db.isCorrectUser(username, password);

        if (isCorrectUser(userId)) {
            context.result("Hello " + username);
        } else {
            context.result("Wrong username or password");
        }
    }

    private static boolean isCorrectUser(int userId) {
        return userId >= 0;
    }
}
