package equidiaryDB

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import equidiaryDB.config.DBConfig
import equidiaryDB.config.NULL_CONFIG
import equidiaryDB.config.getDBConfig
import equidiaryDB.database.DataBaseE
import equidiaryDB.services.HorseService
import equidiaryDB.services.LoginService
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJackson
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object Horses : IntIdTable("horse") {
    val name = varchar("horse_name", 150)
    val height = integer("height")
    val weight = integer("weight").nullable()
    val birthDate = date("birth_date")
}

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

        val objectMapper = jacksonObjectMapper()
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.registerModule(JavaTimeModule())

        db = DataBaseE.createDatabase(config.host, config.port, config.user, config.password, config.schema, config.url)
        app = Javalin.create { it.jsonMapper(JavalinJackson(objectMapper)) }.start(EQUIDIARYDB_PORT)
        createEndPoints()

        transaction {
            Horses.insert {
                it[name] = "Fleur"
                it[height] = 160
                it[weight] = 500
                it[birthDate] = LocalDate.of(2015, 4, 22)
            }
        }
    }

    private fun applyDataBaseMigrations(config: DBConfig) {
        val flyway = Flyway
            .configure()
            .locations("classpath:migration")
            .dataSource(config.url, config.user, config.password)
            .load()

        flyway.migrate()
    }

    private fun failedToLoadConfig(config: DBConfig?) = config === NULL_CONFIG

    @JvmStatic
    fun stop() {
        app.stop()
    }

    private fun createEndPoints() {
        app.get("/") { ctx: Context -> ctx.result("Hello World") }
        app.get("/users/{name}") { ctx: Context ->
            ctx.result("Hello " + ctx.pathParam("name"))
        }
        app.post("/login") { LoginService() }
        app.get("/horse", HorseService())
    }
}
