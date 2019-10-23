package equidiaryDB.services;

import equidiaryDB.EquidiaryDB;
import io.javalin.http.Context;
import org.json.JSONObject;

import java.sql.SQLException;

public class LoginService {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public static void login(Context context) throws SQLException {
        String body = context.body();
        JSONObject requestBody = new JSONObject(body);
        String username = requestBody.getString(USERNAME);
        String password = requestBody.getString(PASSWORD);

        int userId = EquidiaryDB.db.isCorrectUser(username, password);

        if (userId >= 0) {
            context.result("Hello " + username);
        } else {
            context.result("Wrong username or password");
        }
    }
}
