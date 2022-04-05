package equidiaryDB.utils

import equidiaryDB.database.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun givenHorse(horseName: String, horseHeight: Int, horseWeight: Int, horseBirthDate: LocalDate, horseUuid: String, horseUserUuid: String) = transaction {
    Horses.insert {
        it[uuid] = horseUuid
        it[name] = horseName
        it[height] = horseHeight
        it[weight] = horseWeight
        it[birthDate] = horseBirthDate
        it[userUuid] = horseUserUuid
    }
}

fun givenUser(userUuid: String, user: String, pwd: ByteArray) = transaction {
    Users.insert {
        it[uuid] = userUuid
        it[userName] = user
        it[password] = pwd
    }
}

fun givenAppointment(appDate: LocalDate, appType: String, appComment: String, appHorseId: String, appUuid: String, appointmentId: String = "1") = transaction {
    Appointments.insert {
        it[uuid] = appointmentId
        it[date] = appDate
        it[type] = appType
        it[comment] = appComment
        it[uuid] = appUuid
        it[horseUuid] = appHorseId
    }
}

fun givenProfessional(newProUuid: String, newFirstName: String, newLastName: String, newProfession: String, newUserUuid: String) {
    transaction {
        Professionals.insert {
            it[uuid] = newProUuid
            it[firstName] = newFirstName
            it[lastName] = newLastName
            it[profession] = newProfession
        }
    }
    transaction {
        UserProfessionals.insert {
            it[proUuid] = newProUuid
            it[userUuid] = newUserUuid
        }
    }
}