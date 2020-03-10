package equidiaryDB

import equidiaryDB.config.DBConfig
import equidiaryDB.config.NULL_CONFIG
import equidiaryDB.config.getDBConfig
import equidiaryDB.database.DataBaseE
import equidiaryDB.services.LoginService
import io.javalin.Javalin
import io.javalin.http.Context
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.flywaydb.core.Flyway
import java.nio.file.Paths

object EquidiaryDB {
    private const val EQUIDIARYDB_PORT = 7001
    private lateinit var app: Javalin
    val equiLogger: Logger = LogManager.getLogger()
    lateinit var db: DataBaseE

    @JvmStatic
    fun start() {
        val config = getDBConfig()
        if (failedToLoadConfig(config)) {
            equiLogger.fatal("Failed to start EquidiaryDB")
            return
        }

        applyDataBaseMigrations(config)

        db = DataBaseE.createDatabase(config.host, config.port, config.user, config.password, config.schema)
        app = Javalin.create().start(EQUIDIARYDB_PORT)
        createEndPoints()
    }

    private fun applyDataBaseMigrations(config: DBConfig) {
        val url = "jdbc:postgresql://${config.host}:${config.port}/${config.schema}?serverTimezone=UTC"
        println(url)
        val flyway = Flyway
                .configure()
                .locations("filesystem:/equidiaryDB/migration")
                .dataSource(url, config.user, config.password)
                .load()

        flyway.migrate()
    }

    private fun failedToLoadConfig(config: DBConfig?) = config === NULL_CONFIG

    @JvmStatic
    fun stop() {
        app.stop()
    }

    private fun createEndPoints() {
        app["/", { ctx: Context -> ctx.result("Hello World") }]
        app["/users/:name", { ctx: Context ->
            ctx.result("Hello " + ctx.pathParam("name"))
        }]
        app.post("/login", LoginService())
    }
}
