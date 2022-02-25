package equidiaryDB.services

import com.mashape.unirest.http.Unirest.*
import equidiaryDB.WebTestCase
import equidiaryDB.database.Horses
import equidiaryDB.utils.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID.randomUUID

class HorseServiceTest : WebTestCase() {
    private val HORSE_ENDPOINT = "/horse"

    @Test
    fun getHorseNominal() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), randomUUID().toString())

        whenGetHorse("Fleur")
            .isOk()
            .bodyLike(Regex("""\{"name":"Fleur","height":160,"weight":500,"uuid":".{36}","birthDate":"2015-04-22"\}"""))
    }

    @Test
    fun putHorseNominal() {
        whenPutHorse("""{"name":"Fleur","height":160,"weight":500,"uuid":"${randomUUID()}","birthDate":"2015-04-22"}""")
            .isOk()

        val horseResult = transaction { Horses.select { Horses.name eq "Fleur" }.toList()[0] }
        Assertions.assertEquals("Fleur", horseResult[Horses.name])
        Assertions.assertEquals(160, horseResult[Horses.height])
        Assertions.assertEquals(500, horseResult[Horses.weight])
        Assertions.assertEquals(LocalDate.of(2015, 4, 22), horseResult[Horses.birthDate])
    }

    @Test
    fun postHorseNominal() {
        val horseUuid = randomUUID()
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), horseUuid.toString())

        whenPostHorse("""{"name":"Fleur","height":160,"weight":450,"uuid":"$horseUuid","birthDate":"2015-04-22"}""")
            .isOk()

        val horseResult = transaction { Horses.select { Horses.name eq "Fleur" }.toList()[0] }
        Assertions.assertEquals("Fleur", horseResult[Horses.name])
        Assertions.assertEquals(160, horseResult[Horses.height])
        Assertions.assertEquals(450, horseResult[Horses.weight])
        Assertions.assertEquals(LocalDate.of(2015, 4, 22), horseResult[Horses.birthDate])
    }

    @Test
    fun getUnknownHorse() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22), randomUUID().toString())

        whenGetHorse("Unknown").resourceNotFound()
    }

    @Test
    fun missingMandatoryField() {
        whenPutHorse("""{"height":160,"weight":500,"birthDate":"2015-04-22"}""")
            .isBadRequest()
    }

    // WHEN
    private fun whenPutHorse(horseBody: String) = put("$EQUIDIARYDB_PATH$HORSE_ENDPOINT").body(horseBody).asString()
    private fun whenPostHorse(horseBody: String) = post("$EQUIDIARYDB_PATH$HORSE_ENDPOINT").body(horseBody).asString()
    private fun whenGetHorse(horseName: String) = get("$EQUIDIARYDB_PATH$HORSE_ENDPOINT/$horseName").asString()
}
