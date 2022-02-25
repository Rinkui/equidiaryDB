package equidiaryDB.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Horses : IntIdTable("horse") {
    val name = varchar("horse_name", 150)
    val height = integer("height")
    val weight = integer("weight").nullable()
    val birthDate = date("birth_date")
    val uuid = varchar("uuid", 50)
}

object Appointments : IntIdTable("appointment") {
    val date = date("appointment_date")
    val type = varchar("appointment_type", 50)
    val comment = varchar("comment", 512)
    val uuid = varchar("uuid", 50)
    val horseId = reference("horseId", Horses)
    //file name (comme ça on peut faire une vision fichier avec les noms d'origines
    //chemin sur le serveur Uuid UUID.randomUUID().toString()
}

object EquidiaryUsers : Table("equidiary_user") {
    val userName = varchar("user_name", 100)
    val horseId = reference("id", Horses)
}