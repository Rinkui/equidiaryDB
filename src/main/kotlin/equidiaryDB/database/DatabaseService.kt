package equidiaryDB.database

import equidiaryDB.domain.Appointment
import equidiaryDB.domain.Horse
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object DatabaseService {

    fun getHorse(horseName: String) = transaction {
        Horses.select { Horses.name eq horseName }
            .map { Horse(it[Horses.name], it[Horses.height], it[Horses.weight], it[Horses.uuid], it[Horses.birthDate]) }
    }

    fun getHorses() = transaction {
        Horses.selectAll()
            .map { Horse(it[Horses.name], it[Horses.height], it[Horses.weight], it[Horses.uuid], it[Horses.birthDate]) }
    }

    fun getHorses(horseName: String) = transaction {
        Horses.select { Horses.name eq horseName }
            .toList()
            .map { Horse(it[Horses.name], it[Horses.height], it[Horses.weight], it[Horses.uuid], it[Horses.birthDate]) }
    }

    fun insertHorse(horse: Horse) = transaction {
        Horses.insert {
            it[name] = horse.name
            it[height] = horse.height
            it[weight] = horse.weight
            it[uuid] = horse.uuid
            it[birthDate] = horse.birthDate
        }
    }

    fun updateHorse(horse: Horse) = transaction {
        Horses.update({ Horses.uuid eq horse.uuid }) {
            it[name] = horse.name
            it[height] = horse.height
            it[weight] = horse.weight
            it[birthDate] = horse.birthDate
        }
    }

    fun getAppointments(horseName: String) = transaction {
        (Appointments leftJoin Horses)
            .select { Horses.name eq horseName }
            .toList()
            .map { Appointment(it[Appointments.date], it[Appointments.type], it[Appointments.comment], it[Appointments.uuid], it[Appointments.horseId].value) }
    }

    fun insertAppointment(appointment: Appointment) = transaction {
        Appointments.insert {
            it[date] = appointment.date
            it[type] = appointment.type
            it[comment] = appointment.comment
            it[horseId] = appointment.horseId
        }
    }

    fun updateAppointment(appointment: Appointment) = transaction {
        Appointments.update({ Appointments.uuid eq appointment.uuid }) {
            it[date] = appointment.date
            it[type] = appointment.type
            it[comment] = appointment.comment
            it[horseId] = appointment.horseId
        }
    }
}