package equidiaryDB.services

import com.mashape.unirest.http.Unirest.*
import equidiaryDB.WebTestCase
import equidiaryDB.database.Appointments
import equidiaryDB.utils.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID.randomUUID

class AppointmentServiceTest : WebTestCase() {
    private val APPOINTMENT_ENDPOINT = "/horse/{horseName}/appointments"

    @Test
    fun getAppointmentNominal() {
        val horseUuid = randomUUID().toString()
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid, randomUUID().toString())
        givenAppointment(LocalDate.of(2022, 2, 18), "vet", "vaccins", horseUuid, randomUUID().toString())

        whenGetAppointments("Fleur")
            .isOk()
            .bodyLike(Regex("""\[\{"date":"2022-02-18","type":"vet","comment":"vaccins","uuid":".{36}","horseId":1\}\]"""))
    }

    @Test
    fun getEmptyAppointment() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), randomUUID().toString(), randomUUID().toString())

        whenGetAppointments("Fleur")
            .isOk()
            .bodyIs("""[]""")
    }

    @Test
    fun getUnknownHorse() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), randomUUID().toString(), randomUUID().toString())
        givenAppointment(LocalDate.of(2022, 2, 18), "vet", "vaccins", "1", randomUUID().toString())

        whenGetAppointments("Unknown").resourceNotFound()
    }

    @Test
    fun putAppointmentNominal() {
        val horseUuid = randomUUID().toString()
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid, randomUUID().toString())

        whenPutAppointment("Fleur", """{"date":"2022-02-18","type":"podologue","comment":"parage","uuid":"${randomUUID()}","horseId":1}""")

        val appointmentsResult = transaction { Appointments.select { Appointments.horseUuid eq horseUuid }.toList() }
        assertEquals(1, appointmentsResult.size)
        val appointmentResult = appointmentsResult[0]
        assertEquals(LocalDate.of(2022, 2, 18), appointmentResult[Appointments.date])
        assertEquals("parage", appointmentResult[Appointments.comment])
        assertEquals("podologue", appointmentResult[Appointments.type])
        assertEquals(horseUuid, appointmentResult[Appointments.horseUuid])
    }

    @Test
    fun editAppointment() {
        val horseUuid = randomUUID().toString()
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid, randomUUID().toString())
        val appointmentUuid = randomUUID().toString()
        givenAppointment(LocalDate.of(2022, 2, 18), "vet", "vaccins", "1", appointmentUuid)

        whenPostAppointment("Fleur", """{"date":"2022-02-18","type":"podologue","comment":"parage","uuid":"$appointmentUuid","horseId":1}""")

        val appointmentsResult = transaction { Appointments.select { Appointments.horseUuid eq appointmentUuid }.toList() }
        assertEquals(1, appointmentsResult.size)
        val appointmentResult = appointmentsResult[0]
        assertEquals(LocalDate.of(2022, 2, 18), appointmentResult[Appointments.date])
        assertEquals("parage", appointmentResult[Appointments.comment])
        assertEquals("podologue", appointmentResult[Appointments.type])
        assertEquals(horseUuid, appointmentResult[Appointments.horseUuid])
    }

    // WHEN
    private fun whenPutAppointment(horseName: String, appointmentBody: String) =
        put("$EQUIDIARYDB_PATH${APPOINTMENT_ENDPOINT.replace("{horseName}", horseName)}").body(appointmentBody).asString()

    private fun whenPostAppointment(horseName: String, appointmentBody: String) =
        post("$EQUIDIARYDB_PATH${APPOINTMENT_ENDPOINT.replace("{horseName}", horseName)}").body(appointmentBody).asString()

    private fun whenGetAppointments(horseName: String) = get("$EQUIDIARYDB_PATH${APPOINTMENT_ENDPOINT.replace("{horseName}", horseName)}").asString()

}
