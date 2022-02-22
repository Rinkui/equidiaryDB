package equidiaryDB.services

import equidiaryDB.JsonUtils
import equidiaryDB.database.Appointments
import equidiaryDB.database.DatabaseService
import equidiaryDB.domain.Appointment
import io.javalin.http.Context
import io.javalin.http.Handler
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class AppointmentService : Handler {

    fun getAppointments(context: Context) {
        val horseName = context.pathParam("horseName")
        val horses = DatabaseService.getHorses(horseName)
        if (horses.isEmpty()) {
            context.status(404)
            return
        }

        val appointments = DatabaseService.getAppointments(horseName)
        context.json(appointments)
    }

    fun createAppointments(context: Context) {
        val horseName = context.pathParam("horseName")
        val horses = DatabaseService.getHorses(horseName)
        if (horses.isEmpty()) {
            context.status(404)
            return
        }

        val postAppointments: Appointment = try {
            JsonUtils.fromJson(context.body(), Appointment::class.java)
        } catch (e: Throwable) {
            context.status(400)
            return
        }

        transaction {
            Appointments.insert {
                it[date] = postAppointments.date
                it[type] = postAppointments.type
                it[comment] = postAppointments.comment
                it[horseId] = postAppointments.horseId
            }
        }
        context.status(200)
    }

    override fun handle(ctx: Context) {}
}
