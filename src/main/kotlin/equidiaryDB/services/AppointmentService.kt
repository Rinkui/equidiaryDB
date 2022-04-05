package equidiaryDB.services

import equidiaryDB.EquidiaryDB.equiLogger
import equidiaryDB.JsonUtils
import equidiaryDB.database.DatabaseService
import equidiaryDB.domain.Appointment
import io.javalin.http.Context
import io.javalin.http.Handler

class AppointmentService : Handler {

    fun getAppointments(context: Context) {
        val horses = DatabaseService.getHorses(context.getHorseUuid())
        if (horses.isEmpty()) {
            equiLogger.error("None horse were found named : ${context.getHorseUuid()}")
            context.status(404)
            return
        }

        val appointments = DatabaseService.getAppointments(context.getHorseUuid())
        context.json(appointments)
    }

    fun createAppointment(context: Context) {
        val horses = DatabaseService.getHorses(context.getHorseUuid())
        if (horses.isEmpty()) {
            equiLogger.error("None horse were found named : ${context.getHorseUuid()}")
            context.status(404)
            return
        }

        val appointments: Appointment = try {
            JsonUtils.fromJson(context.body(), Appointment::class.java)
        } catch (e: Throwable) {
            context.status(400)
            return
        }

        DatabaseService.insertAppointment(appointments)
        context.status(200)
    }

    fun updateAppointment(context: Context) {
        val horses = DatabaseService.getHorses(context.getHorseUuid())
        if (horses.isEmpty()) {
            equiLogger.error("None horse were found named : ${context.getHorseUuid()}")
            context.status(404)
            return
        }

        val appointment: Appointment = try {
            JsonUtils.fromJson(context.body(), Appointment::class.java)
        } catch (e: Throwable) {
            equiLogger.error(e.printStackTrace())
            context.status(400)
            return
        }

        DatabaseService.updateAppointment(appointment)
        context.status(200)
    }
    
    override fun handle(ctx: Context) {}
}
