package equidiaryDB.services

import com.mashape.unirest.http.Unirest.*
import com.toxicbakery.bcrypt.Bcrypt
import equidiaryDB.WebTestCase
import equidiaryDB.database.Horses
import equidiaryDB.utils.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID.randomUUID

class HorseServiceTest : WebTestCase() {
    private val HORSE_ENDPOINT = "/$username/horse"
    private var userUuid: String = ""

    @BeforeEach
    override fun setup() {
        super.setup()
        userUuid = randomUUID().toString()
        givenUser(userUuid, "marie", Bcrypt.hash("marie", 8))
    }

    @Test
    fun getHorseNominal() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), randomUUID().toString(), userUuid)

        whenGetHorse("Fleur")
            .isOk()
            .bodyLike(Regex("""\{"name":"Fleur","height":160,"weight":500,"uuid":".{36}","birthDate":"2015-04-22","userUuid":".{36}"\}"""))
    }

    @Test
    fun putHorseNominal() {
        whenPutHorse("""{"name":"Fleur","height":160,"weight":500,"uuid":"${randomUUID()}","birthDate":"2015-04-22","userUuid":"$userUuid"}""")
            .isOk()

        val horseResult = transaction { Horses.select { Horses.name eq "Fleur" }.toList()[0] }
        assertEquals("Fleur", horseResult[Horses.name])
        assertEquals(160, horseResult[Horses.height])
        assertEquals(500, horseResult[Horses.weight])
        assertEquals(LocalDate.of(2015, 4, 22), horseResult[Horses.birthDate])
    }

    @Test
    fun postHorseNominal() {
        val horseUuid = randomUUID()
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid.toString(), userUuid)

        whenPostHorse("Fleur", """{"name":"Fleur","height":160,"weight":450,"uuid":"$horseUuid","birthDate":"2015-04-22","userUuid":"$userUuid"}""")
            .isOk()

        val horseResult = transaction { Horses.select { Horses.name eq "Fleur" }.toList()[0] }
        assertEquals("Fleur", horseResult[Horses.name])
        assertEquals(160, horseResult[Horses.height])
        assertEquals(450, horseResult[Horses.weight])
        assertEquals(LocalDate.of(2015, 4, 22), horseResult[Horses.birthDate])
    }

    @Test
    fun getUnknownHorse() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), randomUUID().toString(), userUuid)

        whenGetHorse("Unknown").resourceNotFound()
    }

    @Test
    fun missingMandatoryField() {
        whenPutHorse("""{"height":160,"weight":500,"birthDate":"2015-04-22"}""")
            .isBadRequest()
    }

    // WHEN
    private fun whenPutHorse(horseBody: String) = put("$EQUIDIARYDB_PATH$HORSE_ENDPOINT").withDefaultHeaders().body(horseBody).asString()
    private fun whenPostHorse(horseName: String, horseBody: String) =
        post("$EQUIDIARYDB_PATH$HORSE_ENDPOINT/$horseName").withDefaultHeaders().body(horseBody).asString()

    private fun whenGetHorse(horseName: String) = get("$EQUIDIARYDB_PATH$HORSE_ENDPOINT/$horseName").withDefaultHeaders().asString()
}
