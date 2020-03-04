package equidiaryDB;

import equidiaryDB.config.Config;
import equidiaryDB.config.NullConfig;
import equidiaryDB.database.DataBase;
import equidiaryDB.services.LoginService;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

import java.nio.file.Path;
import java.nio.file.Paths;

public class EquidiaryDB
{
    private static final int EQUIDIARYDB_PORT = 7001;
    private static final Path CONFIG_PROPERTIES_PATH = Paths.get("config/config.properties");
    public static final Logger LOGGER = LogManager.getLogger();
    private static Javalin app;

    public static void start() throws Exception
    {
        Config config = Config.createConfig(CONFIG_PROPERTIES_PATH);

        if (failedToLoadConfig(config))
        {
            LOGGER.fatal("Failed to start EquidiaryDB");
            return;
        }

//        applyDataBaseMigrations(config);

        DataBase.createDatabase(config.getHostDB(),
                config.getPortDB(),
                config.getUserDB(),
                config.getPasswordDB(),
                config.getSchemaDB());

        app = Javalin.create().start(EQUIDIARYDB_PORT);

        createEndPoints();
    }

    private static void applyDataBaseMigrations(Config config)
    {
        String url = "jdbc:mysql://"
                + config.getHostDB()
                + ":"
                + config.getPortDB()
                + "/"
                + config.getSchemaDB()
                + "?serverTimezone=UTC";

        Flyway db_migration = Flyway.configure()
                .locations("db_migration")
                .dataSource(url, config.getUserDB(), config.getPasswordDB())
                .load();

        db_migration.migrate();
    }

    private static boolean failedToLoadConfig(Config config)
    {
        return config == NullConfig.INSTANCE;
    }

    public static void stop()
    {
        app.stop();
    }

    private static void createEndPoints()
    {
        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/users/:name", ctx -> ctx.result("Hello " + ctx.pathParam("name")));
        app.post("/login", LoginService::login);
    }
}
