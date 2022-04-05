package equidiaryDB.services

import equidiaryDB.database.DatabaseService
import io.javalin.http.Context
import io.javalin.http.Handler

class ProfessionalsService : Handler {

    fun getProfessionals(context: Context) {
        val userUuid = context.pathParam("userUuid")
        if (DatabaseService.getUserByUuid(userUuid).isEmpty()) {
            context.status(404)
        }
        context.json(DatabaseService.getProfessional(userUuid))
    }

    override fun handle(ctx: Context) {}
}
