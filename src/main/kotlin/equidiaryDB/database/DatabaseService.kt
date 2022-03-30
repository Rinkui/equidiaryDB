package equidiaryDB.database

import equidiaryDB.domain.Appointment
import equidiaryDB.domain.Horse
import equidiaryDB.domain.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID.randomUUID

object DatabaseService {
    fun getUser(username: String) = transaction {
        Users.select { Users.userName eq username }
            .map { User(it[Users.userName], it[Users.password]) }
    }

    fun createUser(name: String, hash: ByteArray) = transaction {
        Users.insert {
            it[uuid] = randomUUID().toString()
            it[userName] = name
            it[password] = hash
        }
    }

    fun getHorse(horseName: String) = transaction {
        Horses.select { Horses.name eq horseName }
            .map { Horse(it[Horses.name], it[Horses.height], it[Horses.weight], it[Horses.uuid], it[Horses.birthDate], it[Horses.userUuid]) }
    }

    fun getHorses() = transaction {
        Horses.selectAll()
            .map { Horse(it[Horses.name], it[Horses.height], it[Horses.weight], it[Horses.uuid], it[Horses.birthDate], it[Horses.userUuid]) }
    }

    //TODO delete
    fun getHorses(horseName: String) = transaction {
        Horses.select { Horses.name eq horseName }
            .toList()
            .map { Horse(it[Horses.name], it[Horses.height], it[Horses.weight], it[Horses.uuid], it[Horses.birthDate], it[Horses.userUuid]) }
    }

    fun insertHorse(horse: Horse) = transaction {
        Horses.insert {
            it[name] = horse.name
            it[height] = horse.height
            it[weight] = horse.weight
            it[uuid] = horse.uuid
            it[birthDate] = horse.birthDate
            it[userUuid] = horse.userUuid
        }
    }

    fun updateHorse(horse: Horse) = transaction {
        Horses.update({ Horses.uuid eq horse.uuid }) {
            it[name] = horse.name
            it[height] = horse.height
            it[weight] = horse.weight
            it[birthDate] = horse.birthDate
            it[userUuid] = horse.userUuid
        }
    }

    fun getAppointments(horseName: String) = transaction {
        (Appointments leftJoin Horses)
            .select { Horses.name eq horseName }
            .toList()
            .map {
                Appointment(it[Appointments.date],
                    it[Appointments.type],
                    it[Appointments.comment],
                    it[Appointments.uuid],
                    it[Appointments.horseUuid])
            }
    }

    fun insertAppointment(appointment: Appointment) = transaction {
        Appointments.insert {
            it[date] = appointment.date
            it[type] = appointment.type
            it[comment] = appointment.comment
            it[horseUuid] = appointment.horseUuid
        }
    }

    fun updateAppointment(appointment: Appointment) = transaction {
        Appointments.update({ Appointments.uuid eq appointment.uuid }) {
            it[date] = appointment.date
            it[type] = appointment.type
            it[comment] = appointment.comment
            it[horseUuid] = appointment.horseUuid
        }
    }
}