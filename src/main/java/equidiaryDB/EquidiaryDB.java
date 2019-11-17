package equidiaryDB;

import equidiaryDB.config.Config;
import equidiaryDB.config.NullConfig;
import equidiaryDB.database.DataBase;
import equidiaryDB.services.LoginService;
import io.javalin.Javalin;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class EquidiaryDB {
    private static final int EQUIDIARYDB_PORT = 7001;
    private static final String CONFIG_PROPERTIES_PATH = "config/config.properties";
    private static Javalin app;
    public static DataBase db;

    public static void start() throws Exception {
        Path path = Paths.get(CONFIG_PROPERTIES_PATH);

        Config config = loadConfig(path);

        if (config == NullConfig.INSTANCE) {
            System.err.println("One mandatry information is missing on configuration file given in argument.");
            return;
        }

        db = DataBase.createDatabase(config.getHostDB(), config.getPortDB(), config.getUserDB(), config.getPasswordDB(), config.getSchemaDB());

        app = Javalin.create().start(EQUIDIARYDB_PORT);

        createEndPoints();
    }

    private static Config loadConfig(Path path) throws IOException {
        Properties properties = new Properties();
        FileInputStream inStream = new FileInputStream(path.toFile());
        properties.load(inStream);
        return Config.createConfig(properties);
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
