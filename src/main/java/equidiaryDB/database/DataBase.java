package equidiaryDB.database;

import java.sql.*;

public class DataBase
{
    private static Connection connectionDB;
    private static DataBase   db;

    private DataBase(){}

    public static DataBase createDatabase( String hostname,
                                           String port,
                                           String user,
                                           String password,
                                           String schema ) throws SQLException
    {
        if (db == null)
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"
                                                                + hostname
                                                                + ":"
                                                                + port
                                                                + "/"
                                                                + schema
                                                                + "?serverTimezone=UTC", user, password);

            connectionDB = connection;
            db = new DataBase();
        }

        return db;
    }

    /**
     * The function check if the pair user/password is correct and return the userId.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return The userId on equidiaryDB.database OR -1 if the user was not found OR -2 if we have multiple user.
     * @throws SQLException Throw the error which can be occur during the request to equidiaryDB.database.
     */
    public static int isCorrectUser( String username,
                              String password ) throws SQLException
    {
        int userId = -1;

        String request = "SELECT * FROM user WHERE name = ? AND password = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(request))
        {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();

            while (result.next())
            {
                if (userId != -1)
                {
                    return -2;
                }
                userId = result.getInt("userId");
            }
            return userId;
        }
        catch (SQLException e)
        {
            throw e;
        }
    }
}
