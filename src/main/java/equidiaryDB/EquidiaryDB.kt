package equidiaryDB

import equidiaryDB.config.Config
import equidiaryDB.config.Config.Companion.NULL_CONFIG
import equidiaryDB.database.DataBase
import equidiaryDB.services.LoginService
import io.javalin.Javalin
import io.javalin.http.Context
import org.apache.logging.log4j.LogManager
import org.flywaydb.core.Flyway
import java.nio.file.Paths

object EquidiaryDB {
    private const val EQUIDIARYDB_PORT = 7001
    private val CONFIG_PROPERTIES_PATH = Paths.get("config/config.properties")
    val LOGGER = LogManager.getLogger()
    private lateinit var app: Javalin
    lateinit var db: DataBase

    @JvmStatic
    @Throws(Exception::class)
    fun start() {
        val config: Config = Config.createConfig(CONFIG_PROPERTIES_PATH)
        if (failedToLoadConfig(config)) {
            LOGGER.fatal("Failed to start EquidiaryDB")
            return
        }

        //        applyDataBaseMigrations(config);
        db = DataBase.createDatabase(config.hostDB, config.portDB, config.userDB, config.passwordDB, config.schemaDB)
        app = Javalin.create().start(EQUIDIARYDB_PORT)
        createEndPoints()
    }

    private fun applyDataBaseMigrations(config: Config) {
        val url = "jdbc:mysql://" + config.hostDB + ":" + config.portDB + "/" + config.schemaDB + "?serverTimezone=UTC"
        val db_migration =
                Flyway.configure().locations("db_migration").dataSource(url, config.userDB, config.passwordDB).load()
        db_migration.migrate()
    }

    private fun failedToLoadConfig(config: Config?): Boolean {
        return config === NULL_CONFIG
    }

    @JvmStatic
    fun stop() {
        app!!.stop()
    }

    private fun createEndPoints() {
        app!!["/", { ctx: Context -> ctx.result("Hello World") }]
        app!!["/users/:name", { ctx: Context ->
            ctx.result("Hello " + ctx.pathParam("name"))
        }]
        app!!.post("/login") { obj: Context? -> LoginService() }
    }
}
