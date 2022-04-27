package equidiaryDB.database

import equidiaryDB.domain.Appointment
import equidiaryDB.domain.Horse
import equidiaryDB.domain.Professional
import equidiaryDB.domain.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID.randomUUID

object DatabaseService {
    fun getUser(username: String) = transaction {
        Users.select { Users.userName eq username }
            .map { User(it[Users.userName], it[Users.password]) }
    }

    fun getUserByUuid(userUuid: String) = transaction {
        Users.select { Users.uuid eq userUuid }
            .map { User(it[Users.userName], it[Users.password]) }
    }

    fun createUser(name: String, hash: ByteArray) = transaction {
        Users.insert {
            it[uuid] = randomUUID().toString()
            it[userName] = name
            it[password] = hash
        }
    }

    fun getHorse(horseUuid: String) = transaction {
        Horses.select { Horses.uuid eq horseUuid }
            .map { Horse(it[Horses.name], it[Horses.height], it[Horses.weight], it[Horses.uuid], it[Horses.birthDate], it[Horses.userUuid]) }
    }

    fun getHorses() = transaction {
        Horses.selectAll()
            .map { Horse(it[Horses.name], it[Horses.height], it[Horses.weight], it[Horses.uuid], it[Horses.birthDate], it[Horses.userUuid]) }
    }

    //TODO delete
    fun getHorses(horseUuid: String) = transaction {
        Horses.select { Horses.uuid eq horseUuid }
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

    fun getAppointments(horseUuid: String) = transaction {
        (Appointments leftJoin Horses)
            .select { Horses.uuid eq horseUuid }
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

    fun getProfessional(userUuid: String) = transaction {
        UserProfessionals
            .join(Professionals, JoinType.RIGHT) { UserProfessionals.proUuid eq Professionals.uuid }
            .select { UserProfessionals.userUuid eq userUuid }
            .toList()
            .map {
                Professional(
                    it[Professionals.uuid],
                    it[Professionals.firstName],
                    it[Professionals.lastName],
                    it[Professionals.profession])
            }
    }

    fun insertUserProfessional(usrUuid: String, professional: Professional) = transaction {
        Professionals.insert {
            it[uuid] = professional.uuid
            it[firstName] = professional.firstName
            it[lastName] = professional.lastName
            it[profession] = professional.profession
        }
        UserProfessionals.insert {
            it[proUuid] = professional.uuid
            it[userUuid] = usrUuid
        }
    }

    fun updateProfessional(updatedProfessional: Professional) = transaction {
        Professionals.update({ Professionals.uuid eq updatedProfessional.uuid }) {
            it[firstName] = updatedProfessional.firstName
            it[lastName] = updatedProfessional.lastName
            it[profession] = updatedProfessional.profession
        }
    }
}