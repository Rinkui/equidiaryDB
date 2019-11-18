package equidiaryDB;

import equidiaryDB.config.Config;
import equidiaryDB.config.NullConfig;
import equidiaryDB.database.DataBase;
import equidiaryDB.services.LoginService;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

public class EquidiaryDB {
    private static final int EQUIDIARYDB_PORT = 7001;
    private static final String CONFIG_PROPERTIES_PATH = "config/config.properties";
    private static Javalin app;
    public static DataBase db;
    public static final Logger logger = LogManager.getLogger();

    public static void start() throws Exception {
        Path path = Paths.get(CONFIG_PROPERTIES_PATH);

        Config config = Config.createConfig(path);

        if (config == NullConfig.INSTANCE) {
            logger.fatal("One mandatry information is missing on configuration file given in argument.");
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
