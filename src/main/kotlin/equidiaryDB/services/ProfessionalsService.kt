package equidiaryDB.services

import equidiaryDB.EquidiaryDB.equiLogger
import equidiaryDB.JsonUtils.fromJson
import equidiaryDB.database.DatabaseService
import equidiaryDB.domain.Professional
import io.javalin.http.Context
import io.javalin.http.Handler

class ProfessionalsService : Handler {

    fun getUserProfessionals(context: Context) {
        val userUuid = context.getUserUuid()
        if (DatabaseService.getUserByUuid(userUuid).isEmpty()) {
            context.status(404)
        }
        context.json(DatabaseService.getProfessional(userUuid))
    }

    fun createUserProfessionals(context: Context) {
        val userUuid = context.getUserUuid()
        val professional: Professional = try {
            fromJson(context.body(), Professional::class.java)
        } catch (e: Throwable) {
            equiLogger.fatal(e.printStackTrace())
            context.status(400)
            return
        }

        DatabaseService.insertUserProfessional(userUuid, professional)
        context.status(200)
    }

    fun updateUserProfessionals(context: Context) {
        val professional = DatabaseService.getProfessional(context.getUserUuid())
        if (professional.isEmpty()) {
            equiLogger.error("Unknown professional : ${context.body()}")
            context.status(404)
            return
        }

        val updatedProfessional = try {
            fromJson(context.body(), Professional::class.java)
        } catch (e: Throwable) {
            equiLogger.fatal(e.printStackTrace())
            context.status(400)
            return
        }

        DatabaseService.updateProfessional(updatedProfessional)
        context.status(200)
    }

    override fun handle(ctx: Context) {}
}
