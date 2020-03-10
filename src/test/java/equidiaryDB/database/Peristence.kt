package equidiaryDB.database

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object Horses : IntIdTable("horse") {
    val name = varchar("horse_name", 150)
    val height = integer("height")
    val weight = integer("weight").nullable()
    val birthDate = date("birth_date")
}

object Appointments : IntIdTable("appointment") {
    val date = date("appointment_date")
    val type = varchar("type", 50)
    val comment = varchar("comment", 512)
    val horseId = reference("id", Horses)
}

object EquidiaryUsers : Table("equidiary_user") {
    val userName = varchar("user_name", 100)
    val horseId = reference("id", Horses)
}