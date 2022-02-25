package equidiaryDB.utils

import equidiaryDB.database.Appointments
import equidiaryDB.database.Horses
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun givenHorse(horseName: String, horseHeight: Int, horseWeight: Int, horseBirthDate: LocalDate, horseUuid: String, horseId: Int = 1) {
    transaction {
        Horses.insert {
            it[id] = horseId
            it[name] = horseName
            it[height] = horseHeight
            it[weight] = horseWeight
            it[uuid] = horseUuid
            it[birthDate] = horseBirthDate
        }
    }
}

fun givenAppointment(appDate: LocalDate, appType: String, appComment: String, appHorseId: Int, appUuid: String, appointmentId: Int = 1) {
    transaction {
        Appointments.insert {
            it[id] = appointmentId
            it[date] = appDate
            it[type] = appType
            it[comment] = appComment
            it[uuid] = appUuid
            it[horseId] = appHorseId
        }
    }
}