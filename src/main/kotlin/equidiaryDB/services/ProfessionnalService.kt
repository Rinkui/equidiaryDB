package equidiaryDB.services

import equidiaryDB.database.DatabaseService
import io.javalin.http.Context
import io.javalin.http.Handler

class ProfessionnalService : Handler {

    fun getProfessionals(context: Context) {
        context.json(DatabaseService.getProfessional(context.pathParam("userUuid")))
    }

    override fun handle(ctx: Context) {}
}
