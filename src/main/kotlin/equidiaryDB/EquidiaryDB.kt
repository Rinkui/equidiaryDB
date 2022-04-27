package equidiaryDB

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import equidiaryDB.config.DBConfig
import equidiaryDB.config.NULL_CONFIG
import equidiaryDB.config.getDBConfig
import equidiaryDB.security.TokenProvider
import equidiaryDB.services.AppointmentService
import equidiaryDB.services.HorseService
import equidiaryDB.services.ProfessionalsService
import equidiaryDB.services.UserService
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.ForbiddenResponse
import io.javalin.plugin.json.JavalinJackson
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.flywaydb.core.Flyway

object EquidiaryDB {
    private const val EQUIDIARYDB_PORT = 7001
    private lateinit var app: Javalin
    val tokenProvider = TokenProvider()
    val equiLogger: Logger = LogManager.getLogger() //TODO change for logback

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
        app.before("/user/**") { context ->
            val header = context.header("Authorization")
            if (header.isNullOrEmpty() || !tokenProvider.isTokenValid(header.split(" ")[1])) {
                throw ForbiddenResponse()
            }
        }
        app.routes {
            path("user") {
                path("{userUuid}") {
                    path("horse") {
                        path("{horseUuid}") {
                            get(HorseService()::getHorse)
                            post(HorseService()::updateHorse)
                            path("appointments") {
                                get(AppointmentService()::getAppointments)
                                put(AppointmentService()::createAppointment)
                                post(AppointmentService()::updateAppointment)
                            }
                        }
                        put(HorseService()::createHorse)
                    }
                    path("professionals") {
                        get(ProfessionalsService()::getUserProfessionals)
                        put(ProfessionalsService()::createUserProfessionals)
                        post(ProfessionalsService()::updateUserProfessionals)
                    }
                }
                post(UserService()::login)
                put(UserService()::createUser)
            }
        }
    }
}
