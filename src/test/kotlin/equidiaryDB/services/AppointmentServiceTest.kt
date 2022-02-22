package equidiaryDB.services

import com.mashape.unirest.http.Unirest.get
import com.mashape.unirest.http.Unirest.put
import equidiaryDB.WebTestCase
import equidiaryDB.database.Appointments
import equidiaryDB.database.Horses
import equidiaryDB.utils.bodyIs
import equidiaryDB.utils.isOk
import equidiaryDB.utils.resourceNotFound
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AppointmentServiceTest : WebTestCase() {
    private val APPOINTMENT_ENDPOINT = "/horse/{horseName}/appointments"

    @Test
    fun getAppointmentNominal() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22))
        givenAppointment(LocalDate.of(2022, 2, 18), "vet", "vaccins", 1)

        whenGetAppointments("Fleur")
            .isOk()
            .bodyIs("""[{"date":"2022-02-18","type":"vet","comment":"vaccins","horseId":1}]""")
    }

    @Test
    fun getEmptyAppointment() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22))

        whenGetAppointments("Fleur")
            .isOk()
            .bodyIs("""[]""")
    }

    @Test
    fun getUnknownHorse() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22))
        givenAppointment(LocalDate.of(2022, 2, 18), "vet", "vaccins", 1)

        whenGetAppointments("Unknown").resourceNotFound()
    }

    @Test
    fun putAppointmentNominal() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22))
        givenAppointment(LocalDate.of(2022, 2, 18), "vet", "vaccins", 1)

        whenPutAppointment("Fleur", """{"date":"2022-02-18","type":"podologue","comment":"parage","horseId":1}""")

        val appointmentsResult = transaction { Appointments.select { Appointments.horseId eq 1 }.toList() }
        assertEquals(1, appointmentsResult.size)
        val appointmentResult = appointmentsResult[0]
        assertEquals(LocalDate.of(2022, 2, 18), appointmentResult[Appointments.date])
        assertEquals("parage", appointmentResult[Appointments.comment])
        assertEquals("podologue", appointmentResult[Appointments.type])
        assertEquals(1, appointmentResult[Appointments.horseId].value)
    }

    @Test
    internal fun editAppointment() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22))

        whenPutAppointment("Fleur", """{"date":"2022-02-18","type":"podologue","comment":"parage","horseId":1}""")

        val appointmentsResult = transaction { Appointments.select { Appointments.horseId eq 1 }.toList() }
        assertEquals(1, appointmentsResult.size)
        val appointmentResult = appointmentsResult[0]
        assertEquals(LocalDate.of(2022, 2, 18), appointmentResult[Appointments.date])
        assertEquals("parage", appointmentResult[Appointments.comment])
        assertEquals("podologue", appointmentResult[Appointments.type])
        assertEquals(1, appointmentResult[Appointments.horseId].value)
    }

    private fun givenHorse(horseName: String, horseHeight: Int, horseWeight: Int, horseBirthDate: LocalDate, horseId: Int = 1) {
        transaction {
            Horses.insert {
                it[id] = horseId
                it[name] = horseName
                it[height] = horseHeight
                it[weight] = horseWeight
                it[birthDate] = horseBirthDate
            }
        }
    }

    private fun givenAppointment(appDate: LocalDate, appType: String, appComment: String, appHorseId: Int, appointmentId: Int = 1) {
        transaction {
            Appointments.insert {
                it[id] = appointmentId
                it[date] = appDate
                it[type] = appType
                it[comment] = appComment
                it[horseId] = appHorseId
            }
        }
    }

    // WHEN
    private fun whenPutAppointment(horseName: String, appointmentBody: String) =
        put("$EQUIDIARYDB_PATH${APPOINTMENT_ENDPOINT.replace("{horseName}", horseName)}").body(appointmentBody).asString()

    private fun whenGetAppointments(horseName: String) = get("$EQUIDIARYDB_PATH${APPOINTMENT_ENDPOINT.replace("{horseName}", horseName)}").asString()

}
