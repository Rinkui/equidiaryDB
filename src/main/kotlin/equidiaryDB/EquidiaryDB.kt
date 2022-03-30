package equidiaryDB

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import equidiaryDB.config.DBConfig
import equidiaryDB.config.NULL_CONFIG
import equidiaryDB.config.getDBConfig
import equidiaryDB.services.AppointmentService
import equidiaryDB.services.HorseService
import equidiaryDB.services.UserService
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.json.JavalinJackson
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.flywaydb.core.Flyway

object EquidiaryDB {
    private const val EQUIDIARYDB_PORT = 7001
    private lateinit var app: Javalin
    val equiLogger: Logger = LogManager.getLogger()

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

        app = Javalin.create { it.jsonMapper(JavalinJackson(objectMapper)) }.start(EQUIDIARYDB_PORT)
        createEndPoints()
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
        app.routes {
            path("/horse") {
                path("{horseName}") {
                    get(HorseService()::getHorse)
                    post(HorseService()::updateHorse)
                    path("/appointments") {
                        get(AppointmentService()::getAppointments)
                        put(AppointmentService()::createAppointment)
                        post(AppointmentService()::updateAppointment)
                    }
                }
                put(HorseService()::createHorse)
            }
            path("/login") {
                post(UserService()::login)
                put(UserService()::createUser)
            }
        }
    }
}
