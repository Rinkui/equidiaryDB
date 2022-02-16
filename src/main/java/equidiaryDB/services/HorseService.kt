package equidiaryDB.services

import equidiaryDB.EquidiaryDB.equiLogger
import equidiaryDB.Horses
import io.javalin.http.Context
import io.javalin.http.Handler
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

data class Horse(val name: String, val height: Int, val weight: Int?, val birthDate: LocalDate)

class HorseService : Handler {

    override fun handle(context: Context) {
        val select = transaction { Horses.select { Horses.name eq "Fleur" }.toList()[0] }
        val horse = Horse(select[Horses.name], select[Horses.height], select[Horses.weight], select[Horses.birthDate])
        equiLogger.fatal("TOTO")
        equiLogger.fatal(horse)
        context.json(horse)
    }
}
