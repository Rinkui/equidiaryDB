package equidiaryDB.services

import com.mashape.unirest.http.Unirest.*
import com.toxicbakery.bcrypt.Bcrypt
import equidiaryDB.WebTestCase
import equidiaryDB.database.Appointments
import equidiaryDB.utils.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID.randomUUID

class AppointmentServiceTest : WebTestCase() {
    private val APPOINTMENT_ENDPOINT = "/$username/horse/{horseName}/appointments"
    private var userUuid: String = ""

    @BeforeEach
    override fun setup() {
        super.setup()
        userUuid = randomUUID().toString()
        givenUser(userUuid, username, Bcrypt.hash("marie", 8))
    }

    @Test
    fun getAppointmentNominal() {
        val horseUuid = randomUUID().toString()
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid, userUuid)
        givenAppointment(LocalDate.of(2022, 2, 18), "vet", "vaccins", horseUuid, randomUUID().toString())

        whenGetAppointments(horseUuid)
            .isOk()
            .bodyLike(Regex("""\[\{"date":"2022-02-18","type":"vet","comment":"vaccins","uuid":".{36}","horseUuid":".{36}"\}\]"""))
    }

    @Test
    fun getEmptyAppointment() {
        val horseUuid = randomUUID().toString()
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid, userUuid)

        whenGetAppointments(horseUuid)
            .isOk()
            .withBody("""[]""")
    }

    @Test
    fun getUnknownHorse() {
        val horseUuid = randomUUID().toString()
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid, userUuid)
        givenAppointment(LocalDate.of(2022, 2, 18), "vet", "vaccins", horseUuid, randomUUID().toString())

        whenGetAppointments("Unknown").isResourceNotFound()
    }

    @Test
    fun putAppointmentNominal() {
        val horseUuid = randomUUID().toString()
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid, userUuid)

        whenPutAppointment(horseUuid, """{"date":"2022-02-18","type":"podologue","comment":"parage","uuid":"${randomUUID()}","horseUuid":"$horseUuid"}""")

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
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid, userUuid)
        val appointmentUuid = randomUUID().toString()
        givenAppointment(LocalDate.of(2022, 2, 18), "vet", "vaccins", horseUuid, appointmentUuid)

        whenPostAppointment(horseUuid, """{"date":"2022-02-18","type":"podologue","comment":"parage","uuid":"$appointmentUuid","horseUuid":"$horseUuid"}""")

        val appointmentsResult = transaction { Appointments.select { Appointments.uuid eq appointmentUuid }.toList() }
        assertEquals(1, appointmentsResult.size)
        val appointmentResult = appointmentsResult[0]
        assertEquals(LocalDate.of(2022, 2, 18), appointmentResult[Appointments.date])
        assertEquals("parage", appointmentResult[Appointments.comment])
        assertEquals("podologue", appointmentResult[Appointments.type])
        assertEquals(horseUuid, appointmentResult[Appointments.horseUuid])
    }

    // WHEN
    private fun whenPutAppointment(horseName: String, appointmentBody: String) =
        put("$EQUIDIARYDB_PATH${APPOINTMENT_ENDPOINT.replace("{horseName}", horseName)}").withDefaultHeaders().body(appointmentBody).asString()

    private fun whenPostAppointment(horseName: String, appointmentBody: String) =
        post("$EQUIDIARYDB_PATH${APPOINTMENT_ENDPOINT.replace("{horseName}", horseName)}").withDefaultHeaders().body(appointmentBody).asString()

    private fun whenGetAppointments(horseName: String) =
        get("$EQUIDIARYDB_PATH${APPOINTMENT_ENDPOINT.replace("{horseName}", horseName)}").withDefaultHeaders().asString()

}
