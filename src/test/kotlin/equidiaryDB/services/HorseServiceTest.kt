package equidiaryDB.services

import com.mashape.unirest.http.Unirest.get
import com.mashape.unirest.http.Unirest.post
import equidiaryDB.Horses
import equidiaryDB.WebTestCase
import equidiaryDB.utils.bodyIs
import equidiaryDB.utils.isBadRequest
import equidiaryDB.utils.isOk
import equidiaryDB.utils.resourceNotFound
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class HorseServiceTest : WebTestCase() {
    @Test
    fun getHorseNominal() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22))

        whenGetHorse("Fleur")
            .isOk()
            .bodyIs("""{"name":"Fleur","height":160,"weight":500,"birthDate":"2015-04-22"}""")
    }

    @Test
    fun getUnknownHorse() {
        givenHorse("Fleur", 160, 500, LocalDate.of(2015, 4, 22))

        whenGetHorse("Unknown").resourceNotFound()
    }

    @Test
    fun postHorseNominal() {
        whenPostHorse("""{"name":"Fleur","height":160,"weight":500,"birthDate":"2015-04-22"}""")
            .isOk()

        val horseResult = transaction { Horses.select { Horses.name eq "Fleur" }.toList()[0] }
        Assertions.assertEquals("Fleur", horseResult[Horses.name])
        Assertions.assertEquals(160, horseResult[Horses.height])
        Assertions.assertEquals(500, horseResult[Horses.weight])
        Assertions.assertEquals(LocalDate.of(2015, 4, 22), horseResult[Horses.birthDate])
    }

    @Test
    fun missingMandatoryField() {
        whenPostHorse("""{"height":160,"weight":500,"birthDate":"2015-04-22"}""")
            .isBadRequest()
    }

    private fun givenHorse(horseName: String, horseHeight: Int, horseWeight: Int, horseBirthDate: LocalDate) {
        transaction {
            Horses.insert {
                it[name] = horseName
                it[height] = horseHeight
                it[weight] = horseWeight
                it[birthDate] = horseBirthDate
            }
        }
    }

    // WHEN
    private fun whenPostHorse(horseBody: String) = post("$EQUIDIARYDB_PATH$GET_HORSE_ENDPOINT").body(horseBody).asString()
    private fun whenGetHorse(horseName: String) = get("$EQUIDIARYDB_PATH$GET_HORSE_ENDPOINT/$horseName").asString()
}
