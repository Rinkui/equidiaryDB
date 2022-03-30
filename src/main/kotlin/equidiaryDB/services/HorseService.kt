package equidiaryDB.services

import equidiaryDB.EquidiaryDB.equiLogger
import equidiaryDB.JsonUtils
import equidiaryDB.database.DatabaseService
import equidiaryDB.domain.Horse
import io.javalin.http.Context
import io.javalin.http.Handler

class HorseService : Handler {

    fun getHorse(context: Context) {
        val horseName = context.pathParam("horseName")
        val select = if (horseName.isNotEmpty()) {
            DatabaseService.getHorse(horseName)
        } else {
            DatabaseService.getHorses()
        }
        if (select.isEmpty()) {
            context.status(404)
            return
        }
        context.json(select[0])
    }

    fun createHorse(context: Context) {
        val horse: Horse = try {
            JsonUtils.fromJson(context.body(), Horse::class.java)
        } catch (e: Throwable) {
            equiLogger.fatal(e.printStackTrace())
            context.status(400)
            return
        }

        DatabaseService.insertHorse(horse)
        context.status(200)
    }

    fun updateHorse(context: Context) {
        val horses = DatabaseService.getHorses(context.pathParam("horseName"))
        if (horses.isEmpty()) {
            context.status(404)
            return
        }

        val updatedHorse = try {
            JsonUtils.fromJson(context.body(), Horse::class.java)
        } catch (e: Throwable) {
            equiLogger.fatal(e.printStackTrace())
            context.status(400)
            return
        }

        DatabaseService.updateHorse(updatedHorse)
        context.status(200)
    }

    override fun handle(ctx: Context) {}
}
