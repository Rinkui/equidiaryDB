package equidiaryDB.database

import equidiaryDB.domain.Appointment
import equidiaryDB.domain.Horse
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseService {

    fun getHorses(horseName: String) = transaction {
        Horses.select { Horses.name eq horseName }
            .toList()
            .map { Horse(it[Horses.name], it[Horses.height], it[Horses.weight], it[Horses.uuid], it[Horses.birthDate]) }
    }

    fun getAppointments(horseName: String) = transaction {
        (Appointments leftJoin Horses)
            .select { Horses.name eq horseName }
            .toList()
            .map { Appointment(it[Appointments.date], it[Appointments.type], it[Appointments.comment], it[Appointments.uuid], it[Appointments.horseId].value) }
    }
}