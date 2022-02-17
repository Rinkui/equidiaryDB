package equidiaryDB.services

import equidiaryDB.Horses
import equidiaryDB.JsonUtils
import equidiaryDB.domain.Horse
import io.javalin.http.Context
import io.javalin.http.Handler
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class HorseService : Handler {

    fun getHorse(context: Context) {
        val select = transaction { Horses.select { Horses.name eq context.pathParam("horseName") }.toList() }
        if (select.isEmpty()) {
            context.status(404)
            return
        }
        val horseResult = select[0]
        val horse = Horse(horseResult[Horses.name], horseResult[Horses.height], horseResult[Horses.weight], horseResult[Horses.birthDate])
        context.json(horse)
    }

    fun createHorse(context: Context) {
        val postHorse: Horse = try {
            JsonUtils.fromJson(context.body(), Horse::class.java)
        } catch (e: Throwable) {
            context.status(400)
            return
        }
        transaction {
            Horses.insert {
                it[name] = postHorse.name
                it[height] = postHorse.height
                it[weight] = postHorse.weight
                it[birthDate] = postHorse.birthDate
            }
        }
        context.status(200)
    }

    override fun handle(ctx: Context) {}
}
