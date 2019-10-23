package equidiaryDB;

import equidiaryDB.Database.DataBase;
import equidiaryDB.config.Config;
import equidiaryDB.config.NullConfig;
import equidiaryDB.services.LoginService;
import io.javalin.Javalin;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

public class EquidiaryDB {
    private static final int EQUIDIARYDB_PORT = 7001;
    private static Javalin app;
    public static DataBase db;

    public static void start() throws IOException, SQLException {
        Config config = Config.createConfig(Paths.get("config/config.properties"));

        if (config == NullConfig.INSTANCE) {
            System.err.println("One mandatry information is missing on configuration file given in argument.");
            return;
        }

        db = DataBase.createDatabase(config.getHostDB(), config.getPortDB(), config.getUserDB(), config.getPasswordDB(), config.getSchemaDB());

        app = Javalin.create().start(EQUIDIARYDB_PORT);

        createEndPoints();
    }

    public static void stop() {
        app.stop();
    }

    private static void createEndPoints() {
        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/users/:name", ctx -> ctx.result("Hello " + ctx.pathParam("name")));
        app.post("/login", LoginService::login);
    }
}
